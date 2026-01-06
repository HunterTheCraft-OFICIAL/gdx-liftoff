@file:JvmName("LiftoffCli")

package gdx.liftoff.cli

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Version
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Files
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.utils.GdxNativesLoader
import gdx.liftoff.config.Configuration
import gdx.liftoff.data.languages.Java
import gdx.liftoff.data.languages.Kotlin
import gdx.liftoff.data.languages.Language
import gdx.liftoff.data.libraries.Library
import gdx.liftoff.data.platforms.*
import gdx.liftoff.data.project.*
import gdx.liftoff.data.templates.Template
import gdx.liftoff.data.templates.official.ClassicTemplate
import gdx.liftoff.data.templates.official.KotlinClassicTemplate
import java.io.File
import java.util.Optional
import kotlin.system.exitProcess

/**
 * Headless CLI entry-point for gdx-liftoff.
 *
 * This class intentionally avoids any UI interaction.
 * It reuses existing Liftoff infrastructure exactly as-is.
 */
object LiftoffCli {

  @JvmStatic
  fun main(args: Array<String>) {
    log("Starting gdx-liftoff headless CLI")

    // Required for libGDX file handling in headless mode
    GdxNativesLoader.load()
    Gdx.files = Lwjgl3Files()

    val options = CliOptions.parse(args)

    log("Resolved options:")
    log("  projectName = ${options.projectName}")
    log("  packageName = ${options.packageName}")
    log("  outputDir   = ${options.outputDir}")
    log("  preset      = ${options.preset}")

    val preset = options.resolvePreset()

    val basicData =
      BasicProjectData(
        name = options.projectName ?: preset.projectName,
        rootPackage = options.packageName ?: preset.rootPackage,
        mainClass = "Main",
        destination = FileHandle(File(options.outputDir)),
        androidSdk = FileHandle(File(".")),
      )

    val defaultJavaVersion = Java().version
    val defaultGwtVersion = "2.2.7"

    val advancedData =
      AdvancedProjectData(
        version = Configuration.VERSION,
        gdxVersion = Version.VERSION,
        javaVersion = defaultJavaVersion,
        gwtPluginVersion = defaultGwtVersion,
        serverJavaVersion = defaultJavaVersion,
        desktopJavaVersion = defaultJavaVersion,
        generateSkin = preset.addSkin,
        generateReadme = true,
        gradleTasks = arrayListOf(),
      )

    val extensions =
      ExtensionsData(
        officialExtensions = preset.officialExtensions.orElse(Listing.officialLibraries),
        thirdPartyExtensions = preset.thirdPartyExtensions,
      )

    val project =
      Project(
        basic = basicData,
        advanced = advancedData,
        platforms = preset.platforms.associateBy { it.id },
        languages = preset.languagesData,
        extensions = extensions,
        template = preset.template,
      )

    log("Generating project...")
    project.generate()

    log("Including Gradle Wrapper...")
    project.includeGradleWrapper(NullLogger, executeGradleTasks = false)

    log("Project generation finished successfully.")
    exitProcess(0)
  }

  private fun log(message: String) {
    println("[LIFTOFF-CLI] $message")
  }
}

/**
 * Minimal CLI options parser.
 * This will evolve incrementally.
 */
private data class CliOptions(
  val preset: String?,
  val projectName: String?,
  val packageName: String?,
  val outputDir: String,
) {

  fun resolvePreset(): Preset {
    return try {
      preset?.let { Preset.valueOf(it.uppercase()) } ?: Preset.DEFAULT
    } catch (_: Exception) {
      Preset.DEFAULT
    }
  }

  companion object {
    fun parse(args: Array<String>): CliOptions {
      fun valueOf(flag: String): String? {
        val index = args.indexOf(flag)
        return if (index >= 0 && index + 1 < args.size) args[index + 1] else null
      }

      return CliOptions(
        preset = valueOf("--preset"),
        projectName = valueOf("--name"),
        packageName = valueOf("--package"),
        outputDir = valueOf("--output") ?: "build/dist/cli",
      )
    }
  }
}

/**
 * Presets reused directly from Sample.kt logic.
 * This guarantees parity with existing behavior.
 */
private enum class Preset {
  DEFAULT {
    override val projectName = "gdx-liftoff-cli"
    override val rootPackage = "gdx.liftoff"
    override val platforms = listOf(Core(), Lwjgl3(), Android(), IOS(), GWT())
    override val languages: List<Language> = emptyList()
    override val officialExtensions = Optional.empty<List<Library>>()
    override val thirdPartyExtensions: List<Library> = emptyList()
    override val template: Template = ClassicTemplate()
  },

  KOTLIN {
    override val projectName = "gdx-liftoff-cli-kotlin"
    override val rootPackage = "gdx.liftoff"
    override val platforms = listOf(Core(), Lwjgl3(), Android(), IOS(), TeaVM())
    override val languages = listOf(Kotlin())
    override val officialExtensions = Optional.empty<List<Library>>()
    override val thirdPartyExtensions: List<Library> = emptyList()
    override val template: Template = KotlinClassicTemplate()
  };

  abstract val projectName: String
  abstract val rootPackage: String
  abstract val platforms: List<Platform>
  abstract val languages: List<Language>
  abstract val officialExtensions: Optional<List<Library>>
  abstract val thirdPartyExtensions: List<Library>
  abstract val template: Template
  open val addSkin: Boolean = true

  val languagesData: LanguagesData
    get() = LanguagesData(languages.toMutableList(), languages.associate { it.id to it.version })
}

/**
 * No-op logger to avoid touching existing logging infrastructure.
 */
private object NullLogger : ProjectLogger {
  override fun log(message: String) {}
  override fun logNls(bundleLine: String) {}
}