package gdx.liftoff

/**
 * Configuration model for Liftoff Headless / CLI mode.
 *
 * This file defines the baseline defaults that MUST always
 * generate a valid LibGDX project, even with zero user input.
 */
data class LiftoffConfig(
    val projectName: String,
    val packageName: String,
    val platforms: Set<String>,
    val languages: Set<String>,
    val template: String,
    val officialLibraries: Set<String>,
    val unofficialLibraries: Set<String>
) {

    companion object {

        /**
         * Immutable baseline configuration.
         *
         * This is the safety net for headless execution:
         * - no UI
         * - no flags
         * - no workflow input
         */
        val DEFAULT = LiftoffConfig(
            projectName = "my-gdx-game",
            packageName = "com.mygdx.game",

            platforms = setOf(
                "core",
                "lwjgl3"
            ),

            languages = setOf(
                "kotlin"
            ),

            template = "kotlin-classic",

            officialLibraries = emptySet(),
            unofficialLibraries = emptySet()
        )
    }
}
