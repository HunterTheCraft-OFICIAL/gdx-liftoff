package gdx.liftoff

import gdx.liftoff.data.languages.Language
import gdx.liftoff.data.libraries.Library
import gdx.liftoff.data.platforms.Platform
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
    for (p: Platform in Listing.platforms) {
      println("- ${p.id}")
    }
  }

  private fun listLanguages() {
    println("LANGUAGES:")
    for (l: Language in Listing.languages) {
      println("- ${l.id} (version=${l.version})")
    }
    println("- java (default)")
  }

  private fun listTemplates() {
    println("TEMPLATES:")
    for (t: Template in Listing.templates) {
      println("- ${t.id}")
    }
  }

  private fun listOfficialLibraries() {
    println("OFFICIAL LIBRARIES:")
    for (l: Library in Listing.officialLibraries) {
      println("- ${l.id}")
    }
  }

  private fun listUnofficialLibraries() {
    println("UNOFFICIAL LIBRARIES:")
    for (l: Library in Listing.unofficialLibraries) {
      println("- ${l.id}")
    }
  }

  // -------------------------
  // DUMP COMMAND
  // -------------------------

  private fun dumpSchema() {
    println("{")

    println("  \"platforms\": [")
    val platforms = Listing.platforms
    for (i in platforms.indices) {
      val suffix = if (i < platforms.size - 1) "," else ""
      println("    \"${platforms[i].id}\"$suffix")
    }
    println("  ],")

    println("  \"languages\": [")
    val languages = Listing.languages.map { it.id } + "java"
    for (i in languages.indices) {
      val suffix = if (i < languages.size - 1) "," else ""
      println("    \"${languages[i]}\"$suffix")
    }
    println("  ],")

    println("  \"templates\": [")
    val templates = Listing.templates
    for (i in templates.indices) {
      val suffix = if (i < templates.size - 1) "," else ""
      println("    \"${templates[i].id}\"$suffix")
    }
    println("  ],")

    println("  \"libraries\": {")
    println("    \"official\": [")
    val official = Listing.officialLibraries
    for (i in official.indices) {
      val suffix = if (i < official.size - 1) "," else ""
      println("      \"${official[i].id}\"$suffix")
    }
    println("    ],")

    println("    \"unofficial\": [")
    val unofficial = Listing.unofficialLibraries
    for (i in unofficial.indices) {
      val suffix = if (i < unofficial.size - 1) "," else ""
      println("      \"${unofficial[i].id}\"$suffix")
    }
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