@file:JvmName("Sample2")

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
import gdx.liftoff.data.libraries.official.*
import gdx.liftoff.data.platforms.*
import gdx.liftoff.data.project.*
import gdx.liftoff.data.templates.official.ClassicTemplate
import gdx.liftoff.data.templates.official.KotlinClassicTemplate
import kotlin.system.exitProcess
import java.io.File

/** Lista de plataformas oficiais */
val officialPlatforms = listOf(Core(), Lwjgl3(), Android(), IOS(), GWT(), TeaVM())

/** Lista de extensões oficiais */
val officialExtensions = listOf(Box2D(), Box2DLights(), Freetype())

fun generateProject(
    projectName: String,
    rootPackage: String,
    languages: List<Language>,
    template: ProjectTemplate,
    destinationFolder: File
) {
    val basicData = BasicProjectData(
        name = projectName,
        rootPackage = rootPackage,
        mainClass = "Main",
        destination = FileHandle(destinationFolder),
        androidSdk = FileHandle(File("."))
    )

    val javaVersion = Java().version
    val gwtVersion = "2.2.7"

    val advancedData = AdvancedProjectData(
        version = Configuration.VERSION,
        gdxVersion = Version.VERSION,
        javaVersion = javaVersion,
        gwtPluginVersion = gwtVersion,
        serverJavaVersion = javaVersion,
        desktopJavaVersion = javaVersion,
        generateSkin = true,
        generateReadme = true,
        gradleTasks = arrayListOf()
    )

    val project = Project(
        basic = basicData,
        advanced = advancedData,
        platforms = officialPlatforms.associateBy { it.id },
        languages = LanguagesData(languages.toMutableList(), languages.associate { it.id to it.version }),
        extensions = ExtensionsData(officialExtensions, emptyList()),
        template = template
    )

    project.generate()
    project.includeGradleWrapper(NullLogger, executeGradleTasks = false)
}

/** Headless CLI entrypoint */
fun main(args: Array<String>) {
    GdxNativesLoader.load()
    Gdx.files = Lwjgl3Files()

    // Projeto Java
    generateProject(
        projectName = "gdx-full-java",
        rootPackage = "gdx.full.java",
        languages = listOf(Java()),
        template = ClassicTemplate(),
        destinationFolder = File("build/dist/java-project")
    )

    // Projeto Kotlin
    generateProject(
        projectName = "gdx-full-kotlin",
        rootPackage = "gdx.full.kotlin",
        languages = listOf(Kotlin()),
        template = KotlinClassicTemplate(),
        destinationFolder = File("build/dist/kotlin-project")
    )

    exitProcess(0)
}

/** No-op logger */
object NullLogger : ProjectLogger {
    override fun log(message: String) {}
    override fun logNls(bundleLine: String) {}
}

/** Para compatibilidade de tipos de template */
typealias ProjectTemplate = Template