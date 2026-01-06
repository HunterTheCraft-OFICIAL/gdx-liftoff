package gdx.liftoff

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import gdx.liftoff.templates.ClassicTemplate
import gdx.liftoff.templates.KotlinClassicTemplate
import gdx.liftoff.templates.KtxTemplate
import java.io.File

object NullLogger : ProjectLogger {
    override fun log(message: String) {}
}

class LiftoffCli {

    fun createProject(config: LiftoffConfig, outputDir: File) {
        // Converte File para FileHandle
        val handle: FileHandle = Gdx.files.absolute(outputDir.absolutePath)

        // Seleção do template
        val template = when (config.template) {
            "classic" -> ClassicTemplate
            "kotlin-classic" -> KotlinClassicTemplate
            "ktx" -> KtxTemplate
            else -> ClassicTemplate
        }

        // Inicialização do projeto com dados do config
        val project = SampleProject(
            name = config.projectName,
            packageName = config.packageName,
            platforms = config.platforms.toMutableList(),
            languages = config.languages.toMutableList(),
            template = template,
            output = handle,
            logger = NullLogger
        )

        project.generate()
    }
}

interface ProjectLogger {
    fun log(message: String)
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
        // Lógica de criação do projeto (copiar arquivos, gerar gradle, etc)
    }
}
