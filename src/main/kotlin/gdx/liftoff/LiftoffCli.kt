package gdx.liftoff

import java.io.File
import kotlin.system.exitProcess

object LiftoffCli {

    @JvmStatic
    fun main(args: Array<String>) {
        if (args.isEmpty()) {
            printHelp()
            exitProcess(0)
        }

        val options = parseArgs(args)

        if (options.containsKey("help")) {
            printHelp()
            exitProcess(0)
        }

        val outputDir = options["output"] ?: run {
            println("Erro: diretório de saída não informado.")
            exitProcess(1)
        }

        val projectName = options["name"] ?: "MyGdxGame"
        val packageName = options["package"] ?: "com.mygdx.game"

        println("Liftoff CLI (Headless)")
        println("Projeto: $projectName")
        println("Pacote: $packageName")
        println("Saída: $outputDir")

        val dir = File(outputDir)
        if (!dir.exists()) {
            dir.mkdirs()
        }

        // Prova de vida do modo headless
        File(dir, "liftoff-cli.txt").writeText(
            """
            Projeto gerado com sucesso.
            Nome: $projectName
            Pacote: $packageName
            """.trimIndent() + "\n",
        )

        println("Projeto base criado com sucesso.")
    }

    private fun parseArgs(args: Array<String>): Map<String, String> {
        val map = mutableMapOf<String, String>()
        var index = 0

        while (index < args.size) {
            val arg = args[index]

            if (arg.startsWith("--")) {
                val key = arg.removePrefix("--")

                val next = args.getOrNull(index + 1)
                if (next != null && !next.startsWith("--")) {
                    map[key] = next
                    index += 2
                } else {
                    map[key] = "true"
                    index += 1
                }
            } else {
                index += 1
            }
        }

        return map
    }

    private fun printHelp() {
        println(
            """
            Liftoff CLI (modo headless)

            Uso:
              liftoff --output <dir> [opções]

            Opções:
              --name <nome>        Nome do projeto
              --package <pacote>   Pacote base
              --output <dir>       Diretório de saída
              --help               Exibe esta ajuda
            """.trimIndent() + "\n",
        )
    }
}
