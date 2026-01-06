package gdx.liftoff

import gdx.liftoff.Listing

/**
 * Liftoff Headless CLI (Probe Mode)
 *
 * This CLI does NOT generate projects.
 * It only resolves and validates configuration data
 * to help CI / workflows understand available options.
 */
object LiftoffCli {

    @JvmStatic
    fun main(args: Array<String>) {
        println("== Liftoff Headless CLI :: Probe Mode ==")
        println()

        val config = LiftoffConfig.DEFAULT

        println(">> Loaded DEFAULT configuration")
        dumpConfig(config)
        println()

        println(">> Resolving platforms")
        val platforms = config.platforms.mapNotNull {
            Listing.platformsByName[it]
        }
        platforms.forEach { println(" - ${it.id}") }
        println()

        println(">> Resolving languages")
        val languages = Listing.chooseLanguages(config.languages)
        languages.forEach { println(" - ${it.id} (${it.version})") }
        println()

        println(">> Resolving template")
        val template = Listing.templatesByName[config.template]
        if (template == null) {
            error("Template '${config.template}' not found")
        } else {
            println(" - ${template.id}")
        }
        println()

        println(">> Resolving official libraries")
        val officialLibs = Listing.chooseOfficialLibraries(config.officialLibraries)
        if (officialLibs.isEmpty()) {
            println(" - (none)")
        } else {
            officialLibs.forEach { println(" - ${it.id}") }
        }
        println()

        println(">> Resolving unofficial libraries")
        val unofficialLibs = Listing.chooseUnofficialLibraries(config.unofficialLibraries)
        if (unofficialLibs.isEmpty()) {
            println(" - (none)")
        } else {
            unofficialLibs.forEach { println(" - ${it.id}") }
        }
        println()

        println(">> Probe completed successfully")
    }

    private fun dumpConfig(config: LiftoffConfig) {
        println("Project Name        : ${config.projectName}")
        println("Package Name        : ${config.packageName}")
        println("Platforms           : ${config.platforms}")
        println("Languages           : ${config.languages}")
        println("Template            : ${config.template}")
        println("Official Libraries  : ${config.officialLibraries}")
        println("Unofficial Libraries: ${config.unofficialLibraries}")
    }
}
