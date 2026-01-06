@file:JvmName("Sample2_1")

package gdx.liftoff

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
import gdx.liftoff.data.libraries.official.Box2D
import gdx.liftoff.data.libraries.official.Box2DLights
import gdx.liftoff.data.libraries.official.Freetype
import gdx.liftoff.data.platforms.Android
import gdx.liftoff.data.platforms.Core
import gdx.liftoff.data.platforms.GWT
import gdx.liftoff.data.platforms.IOS
import gdx.liftoff.data.platforms.Lwjgl3
import gdx.liftoff.data.platforms.TeaVM
import gdx.liftoff.data.platforms.Platform
import gdx.liftoff.data.project.AdvancedProjectData
import gdx.liftoff.data.project.BasicProjectData
import gdx.liftoff.data.project.ExtensionsData
import gdx.liftoff.data.project.LanguagesData
import gdx.liftoff.data.project.Project
import gdx.liftoff.data.project.ProjectLogger
import gdx.liftoff.data.templates.official.ClassicTemplate
import gdx.liftoff.data.templates.official.KotlinClassicTemplate
import gdx.liftoff.data.templates.Template
import java.io.File
import java.util.Optional
import kotlin.system.exitProcess

/** Determines which platforms, extensions, templates, etc. are used by the project generator. */
enum class Preset {
    /** All official platforms and extensions, Java version. */
    JAVA {
        override val projectName: String
            get() = "gdx-liftoff-java-demo"
        override val rootPackage: String
            get() = "gdx.liftoff.java"
        override val platforms: List<Platform>
            get() = listOf(Core(), Lwjgl3(), Android(), IOS(), GWT(), TeaVM())
        override val languages: List<Language>
            get() = listOf(Java())
        override val officialExtensions: Optional<List<Library>>
            get() = Optional.of(listOf(Box2D(), Box2DLights(), Freetype()))
        override val thirdPartyExtensions: List<Library> = emptyList()
        override val template: Template
            get() = ClassicTemplate()
    },

    /** All official platforms and extensions, Kotlin version. */
    KOTLIN {
        override val projectName: String
            get() = "gdx-liftoff-kotlin-demo"
        override val rootPackage: String
            get() = "gdx.liftoff.kotlin"
        override val platforms: List<Platform>
            get() = listOf(Core(), Lwjgl3(), Android(), IOS(), GWT(), TeaVM())
        override val languages: List<Language>
            get() = listOf(Kotlin())
        override val officialExtensions: Optional<List<Library>>
            get() = Optional.of(listOf(Box2D(), Box2DLights(), Freetype()))
        override val thirdPartyExtensions: List<Library> = emptyList()
        override val template: Template
            get() = KotlinClassicTemplate()
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

fun getPreset(arguments: Array<String>): Preset =
    when {
        arguments.isEmpty() -> Preset.JAVA
        else -> {
            val name = arguments.first()
            try {
                Preset.valueOf(name.uppercase())
            } catch (_: IllegalArgumentException) {
                Preset.JAVA
            }
        }
    }

fun main(arguments: Array<String>) {
    GdxNativesLoader.load()
    Gdx.files = Lwjgl3Files()

    val preset = getPreset(arguments)
    val officialExtensions = preset.officialExtensions.orElse(emptyList())
    val basicData = BasicProjectData(
        name = preset.projectName,
        rootPackage = preset.rootPackage,
        mainClass = "Main",
        destination = FileHandle(File("build/dist/${preset.projectName}")),
        androidSdk = FileHandle(File(".")),
    )

    val defaultJavaVersion = Java().version
    val defaultGwtVersion = "2.2.7"

    val advancedData = AdvancedProjectData(
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

    val extensions = ExtensionsData(
        officialExtensions = officialExtensions,
        thirdPartyExtensions = preset.thirdPartyExtensions,
    )

    val project = Project(
        basic = basicData,
        advanced = advancedData,
        platforms = preset.platforms.associateBy { it.id },
        languages = preset.languagesData,
        extensions = extensions,
        template = preset.template,
    )

    project.generate()
    project.includeGradleWrapper(NullLogger, executeGradleTasks = false)
    exitProcess(0)
}

/** No-op logger for interfacing with the project generator. */
object NullLogger : ProjectLogger {
    override fun log(message: String) {}
    override fun logNls(bundleLine: String) {}
}