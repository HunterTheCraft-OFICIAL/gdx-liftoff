package gdx.liftoff

import com.badlogic.gdx.files.FileHandle
import java.io.File

// Importa do Sample.kt
import gdx.liftoff.Sample.NullLogger
import gdx.liftoff.Sample.ClassicTemplate
import gdx.liftoff.Sample.KotlinClassicTemplate
import gdx.liftoff.Sample.KtxTemplate

class LiftoffCli {

    fun createProject(config: LiftoffConfig, outputDir: File) {
        val outputHandle: FileHandle = com.badlogic.gdx.Gdx.files.absolute(outputDir.absolutePath)

        // Seleciona o template do Sample.kt
        val template = when (config.template) {
            "classic" -> ClassicTemplate
            "kotlin-classic" -> KotlinClassicTemplate
            "ktx" -> KtxTemplate
            else -> ClassicTemplate
        }

        val project = SampleProject(
            name = config.projectName,
            packageName = config.packageName,
            platforms = config.platforms.toMutableList(),
            languages = config.languages.toMutableList(),
            template = template,
            output = outputHandle,
            logger = NullLogger
        )

        project.generate()
    }
}

class SampleProject(
    val name: String,
    val packageName: String,
    val platforms: MutableList<String>,
    val languages: MutableList<String>,
    val template: Any,
    val output: FileHandle,
    val logger: ProjectLogger
) {
    fun generate() {
        logger.log("Generating project $name at ${output.path()}")
        // lógica de criação do projeto
    }
}

interface ProjectLogger {
    fun log(message: String)
}
