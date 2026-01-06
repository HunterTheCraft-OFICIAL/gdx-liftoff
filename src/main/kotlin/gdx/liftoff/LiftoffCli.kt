package gdx.liftoff

import gdx.liftoff.data.project.*
import gdx.liftoff.data.platforms.*
import gdx.liftoff.data.languages.*
import gdx.liftoff.data.templates.*
import java.io.File
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    val presetName = args.firstOrNull()?.uppercase() ?: "DEFAULT"
    val config = LiftoffConfig.DEFAULT_PRESETS[presetName] ?: LiftoffConfig.DEFAULT_CONFIG

    // Criação de dados básicos do projeto
    val basicData = BasicProjectData(
        name = config.projectName,
        rootPackage = config.rootPackage,
        mainClass = "Main",
        destination = File("build/dist/${config.projectName}"),
        androidSdk = File(".")
    )

    // Dados avançados do projeto
    val advancedData = AdvancedProjectData(
        version = "1.0.0",
        gdxVersion = "LATEST",
        javaVersion = "17",
        gwtPluginVersion = "2.2.7",
        serverJavaVersion = "17",
        desktopJavaVersion = "17",
        generateSkin = config.addSkin,
        generateReadme = true,
        gradleTasks = arrayListOf()
    )

    // Mapear plataformas e linguagens
    val platforms = config.platforms.mapNotNull { platformId ->
        when (platformId.lowercase()) {
            "core" -> Core()
            "lwjgl3" -> Lwjgl3()
            "android" -> Android()
            "ios" -> IOS()
            "gwt" -> GWT()
            "teavm" -> TeaVM()
            else -> null
        }
    }.associateBy { it.id }

    val languages = config.languages.mapNotNull { lang ->
        when (lang.lowercase()) {
            "kotlin" -> Kotlin()
            "java" -> Java()
            else -> null
        }
    }

    val extensions = ExtensionsData(
        officialExtensions = config.officialExtensions.map { name -> Listing.officialLibraries.find { it.id == name } }.filterNotNull(),
        thirdPartyExtensions = config.thirdPartyExtensions.map { name -> Listing.unofficialLibraries.find { it.id == name } }.filterNotNull()
    )

    val template: Template = when (config.template.lowercase()) {
        "ktx" -> KtxTemplate()
        "kotlin-classic" -> KotlinClassicTemplate()
        else -> ClassicTemplate()
    }

    val project = Project(
        basic = basicData,
        advanced = advancedData,
        platforms = platforms,
        languages = LanguagesData(languages.toMutableList(), languages.associate { it.id to it.version }),
        extensions = extensions,
        template = template
    )

    project.generate()
    project.includeGradleWrapper(NullLogger, executeGradleTasks = false)

    println("✅ Projeto '${config.projectName}' gerado com sucesso!")
    exitProcess(0)
}

object NullLogger : ProjectLogger {
    override fun log(message: String) {}
    override fun logNls(bundleLine: String) {}
}