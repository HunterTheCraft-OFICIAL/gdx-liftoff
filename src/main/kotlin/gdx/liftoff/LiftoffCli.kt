package gdx.liftoff

import gdx.liftoff.data.platforms.Platform
import gdx.liftoff.data.languages.Language
import gdx.liftoff.data.libraries.Library
import gdx.liftoff.data.templates.Template

object LiftoffCli {

    @JvmStatic
    fun main(args: Array<String>) {
        if (args.isEmpty()) {
            printHelp()
            return
        }

        when (args[0]) {
            "list" -> handleList(args.drop(1))
            "dump" -> handleDump(args.drop(1))
            else -> printHelp()
        }
    }

    private fun handleList(args: List<String>) {
        if (args.isEmpty()) {
            printHelp()
            return
        }

        when (args[0]) {
            "platforms" -> listPlatforms()
            "languages" -> listLanguages()
            "templates" -> listTemplates()
            "libraries" -> {
                if (args.size < 2) {
                    println("Specify 'official' or 'unofficial'")
                    return
                }
                when (args[1]) {
                    "official" -> listOfficialLibraries()
                    "unofficial" -> listUnofficialLibraries()
                    else -> println("Unknown library group: ${args[1]}")
                }
            }
            else -> printHelp()
        }
    }

    private fun handleDump(args: List<String>) {
        if (args.isEmpty()) {
            printHelp()
            return
        }

        when (args[0]) {
            "schema" -> dumpSchema()
            else -> printHelp()
        }
    }

    // -------------------------
    // LIST COMMANDS
    // -------------------------

    private fun listPlatforms() {
        println("PLATFORMS:")
        Listing.platforms.forEach { p: Platform ->
            println("- ${p.id}")
        }
    }

    private fun listLanguages() {
        println("LANGUAGES:")
        Listing.languages.forEach { l: Language ->
            println("- ${l.id} (version=${l.version})")
        }
        println("- java (default)")
    }

    private fun listTemplates() {
        println("TEMPLATES:")
        Listing.templates.forEach { t: Template ->
            println("- ${t.id}")
        }
    }

    private fun listOfficialLibraries() {
        println("OFFICIAL LIBRARIES:")
        Listing.officialLibraries.forEach { l: Library ->
            println("- ${l.id}")
        }
    }

    private fun listUnofficialLibraries() {
        println("UNOFFICIAL LIBRARIES:")
        Listing.unofficialLibraries.forEach { l: Library ->
            println("- ${l.id}")
        }
    }

    // -------------------------
    // DUMP COMMAND
    // -------------------------

    private fun dumpSchema() {
        println("{")

        println("  \"platforms\": [")
        Listing.platforms.joinToString(",\n") { "    \"${it.id}\"" }
            .also { println(it) }
        println("  ],")

        println("  \"languages\": [")
        (Listing.languages.map { it.id } + "java")
            .joinToString(",\n") { "    \"$it\"" }
            .also { println(it) }
        println("  ],")

        println("  \"templates\": [")
        Listing.templates.joinToString(",\n") { "    \"${it.id}\"" }
            .also { println(it) }
        println("  ],")

        println("  \"libraries\": {")
        println("    \"official\": [")
        Listing.officialLibraries.joinToString(",\n") { "      \"${it.id}\"" }
            .also { println(it) }
        println("    ],")

        println("    \"unofficial\": [")
        Listing.unofficialLibraries.joinToString(",\n") { "      \"${it.id}\"" }
            .also { println(it) }
        println("    ]")
        println("  }")

        println("}")
    }

    // -------------------------
    // HELP
    // -------------------------

    private fun printHelp() {
        println(
            """
            Liftoff CLI – Headless Probe Mode

            Commands:
              list platforms
              list languages
              list templates
              list libraries official
              list libraries unofficial
              dump schema

            Examples:
              java -jar liftoff-cli.jar list platforms
              java -jar liftoff-cli.jar dump schema
            """.trimIndent()
        )
    }
}
