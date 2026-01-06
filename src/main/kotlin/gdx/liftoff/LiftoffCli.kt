@file:JvmName("LiftoffCli")

package gdx.liftoff

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Version
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Files
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.utils.GdxNativesLoader
import gdx.liftoff.data.languages.Java
import gdx.liftoff.data.languages.Kotlin
import gdx.liftoff.data.project.*
import gdx.liftoff.data.templates.Template
import java.io.File
import kotlin.system.exitProcess

/** CLI entry point for Liftoff – generates projects headless. */
object LiftoffCli {

    @JvmStatic
    fun main(args: Array<String>) {
        if (args.isEmpty()) {
            println("Usage: LiftoffCli <command> [options]")
            println("Commands: dump schema | generate <preset>")
            exitProcess(0)
        }

        val command = args.first().lowercase()
        val commandArgs = args.drop(1).toTypedArray()

        when (command) {
            "dump" -> handleDump(commandArgs)
            "generate" -> handleGenerate(commandArgs)
            else -> {
                println("Unknown command: $command")
                exitProcess(1)
            }
        }
    }

    /** Handles 'dump schema' command */
    private fun handleDump(args: Array<String>) {
        if (args.isEmpty() || args.first() != "schema") {
            println("Usage: dump schema")
            exitProcess(1)
        }

        // Simples dump JSON do schema interno (poderia ser estendido para carregar arquivo)
        val schemaJson = SchemaBuilder.buildSchemaJson()
        val outputDir = File("out")
        if (!outputDir.exists()) outputDir.mkdirs()
        val outputFile = File(outputDir, "liftoff-schema.json")
        outputFile.writeText(schemaJson)
        println("Schema dumped to ${outputFile.absolutePath}")
        exitProcess(0)
    }

    /** Handles 'generate <preset>' command */
    private fun handleGenerate(args: Array<String>) {
        val preset = getPreset(args)

        // Inicializa Gdx headless
        GdxNativesLoader.load()
        Gdx.files = Lwjgl3Files()

        // Dados básicos do projeto
        val basicData = BasicProjectData(
            name = preset.projectName,
            rootPackage = preset.rootPackage,
            mainClass = "Main",
            destination = FileHandle(File("build/dist/${preset.projectName}")),
            androidSdk = FileHandle(File("."))
        )

        val defaultJavaVersion = Java().version
        val advancedData = AdvancedProjectData(
            version = Configuration.VERSION,
            gdxVersion = Version.VERSION,
            javaVersion = defaultJavaVersion,
            gwtPluginVersion = "2.2.7",
            serverJavaVersion = defaultJavaVersion,
            desktopJavaVersion = defaultJavaVersion,
            generateSkin = preset.addSkin,
            generateReadme = true,
            gradleTasks = arrayListOf()
        )

        val extensions = ExtensionsData(
            officialExtensions = preset.officialExtensions.orElse(Listing.officialLibraries),
            thirdPartyExtensions = preset.thirdPartyExtensions
        )

        // Cria projeto
        val project = Project(
            basic = basicData,
            advanced = advancedData,
            platforms = preset.platforms.associateBy { it.id },
            languages = preset.languagesData,
            extensions = extensions,
            template = preset.template
        )

        project.generate()
        project.includeGradleWrapper(NullLogger, executeGradleTasks = false)
        println("Project '${preset.projectName}' generated at ${basicData.destination.file().absolutePath}")
        exitProcess(0)
    }

    /** Resolves preset from arguments */
    private fun getPreset(arguments: Array<String>): Preset =
        if (arguments.isEmpty()) Preset.DEFAULT
        else try {
            Preset.valueOf(arguments.first().uppercase())
        } catch (_: IllegalArgumentException) {
            Preset.DEFAULT
        }
}

/** No-op logger for headless project generation */
object NullLogger : ProjectLogger {
    override fun log(message: String) {}
    override fun logNls(bundleLine: String) {}
}