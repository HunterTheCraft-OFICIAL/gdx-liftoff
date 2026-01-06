@file:JvmName("LiftoffCli")

package gdx.liftoff

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Files
import com.badlogic.gdx.utils.GdxNativesLoader
import gdx.liftoff.LiftoffConfig.Companion.presets
import gdx.liftoff.LiftoffConfig.Companion.defaults
import gdx.liftoff.data.project.Project
import gdx.liftoff.data.project.ProjectLogger
import java.io.File
import kotlin.system.exitProcess

fun main(arguments: Array<String>) {
    // Inicializa Gdx headless
    GdxNativesLoader.load()
    Gdx.files = Lwjgl3Files()

    // Seleciona preset
    val presetName = if (arguments.isEmpty()) "DEFAULT" else arguments.first().uppercase()
    val config = presets[presetName] ?: presets["DEFAULT"]!!

    // Dados básicos do projeto
    val basicData = gdx.liftoff.data.project.BasicProjectData(
        name = config.projectName,
        rootPackage = config.packageName,
        mainClass = "Main",
        destination = com.badlogic.gdx.files.FileHandle(File("build/dist/${config.projectName}")),
        androidSdk = com.badlogic.gdx.files.FileHandle(File("."))
    )

    // Dados avançados do projeto
    val advancedData = gdx.liftoff.data.project.AdvancedProjectData(
        version = defaults["gdxVersion"] as String,
        gdxVersion = defaults["gdxVersion"] as String,
        javaVersion = defaults["javaVersion"] as String,
        gwtPluginVersion = defaults["gwtPluginVersion"] as String,
        serverJavaVersion = defaults["javaVersion"] as String,
        desktopJavaVersion = defaults["javaVersion"] as String,
        generateSkin = config.addSkin,
        generateReadme = defaults["generateReadme"] as Boolean,
        gradleTasks = arrayListOf(),
    )

    // Dados de extensões
    val extensions = gdx.liftoff.data.project.ExtensionsData(
        officialExtensions = config.officialLibraries.map { gdx.liftoff.data.libraries.Library(it) },
        thirdPartyExtensions = config.unofficialLibraries.map { gdx.liftoff.data.libraries.Library(it) },
    )

    // Cria projeto
    val project = Project(
        basic = basicData,
        advanced = advancedData,
        platforms = config.platforms.associateBy { it },
        languages = gdx.liftoff.data.project.LanguagesData(
            config.languages.toMutableList(),
            config.languages.associateWith { "LATEST" }
        ),
        extensions = extensions,
        template = gdx.liftoff.data.templates.Template(config.template)
    )

    // Gera projeto e inclui wrapper Gradle
    project.generate()
    project.includeGradleWrapper(NullLogger, executeGradleTasks = false)

    exitProcess(0)
}

// Logger único para todo o CLI
object NullLogger : ProjectLogger {
    override fun log(message: String) {}
    override fun logNls(bundleLine: String) {}
}