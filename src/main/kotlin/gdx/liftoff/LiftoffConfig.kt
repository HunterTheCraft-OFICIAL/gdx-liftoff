package gdx.liftoff

data class LiftoffConfig(
    val projectName: String,
    val packageName: String,
    val platforms: List<String>,
    val languages: List<String>,
    val template: String,
    val officialLibraries: List<String>,
    val unofficialLibraries: List<String>,
) {
    companion object {

        val DEFAULT = LiftoffConfig(
            projectName = "liftoff-game",
            packageName = "com.example.liftoff",
            platforms = listOf(
                "core",
                "lwjgl3",
            ),
            languages = listOf(
                "kotlin",
            ),
            template = "classic",
            officialLibraries = listOf(
                "gdx-ai",
            ),
            unofficialLibraries = listOf(
                "ktx-app",
            ),
        )

        val PRESETS = mapOf(
            "DEFAULT" to LiftoffConfig(
                projectName = "gdx-liftoff-demo",
                packageName = "gdx.liftoff",
                platforms = listOf("core", "lwjgl3", "android", "ios", "gwt"),
                languages = listOf(),
                template = "classic",
                officialLibraries = listOf(),
                unofficialLibraries = listOf()
            ),
            "KOTLIN" to LiftoffConfig(
                projectName = "gdx-liftoff-demo-kotlin",
                packageName = "gdx.liftoff",
                platforms = listOf("core", "lwjgl3", "android", "ios", "teavm"),
                languages = listOf("kotlin"),
                template = "kotlin-classic",
                officialLibraries = listOf(),
                unofficialLibraries = listOf()
            ),
            "KTX" to LiftoffConfig(
                projectName = "ktx-demo",
                packageName = "ktx.demo",
                platforms = listOf("core", "lwjgl3", "android", "ios"),
                languages = listOf("kotlin"),
                template = "ktx",
                officialLibraries = listOf(),
                unofficialLibraries = listOf("ktx-*")
            ),
            "KTX_WEB" to LiftoffConfig(
                projectName = "ktx-demo-web",
                packageName = "ktx.demo",
                platforms = listOf("core", "lwjgl3", "android", "ios", "teavm"),
                languages = listOf("kotlin"),
                template = "ktx",
                officialLibraries = listOf(),
                unofficialLibraries = listOf("ktx-* without async, artemis, script")
            ),
            "ANDROID_DEV" to LiftoffConfig(
                projectName = "gdx-android-dev-demo",
                packageName = "gdx.android",
                platforms = listOf("core", "lwjgl3", "android"),
                languages = listOf(),
                template = "classic",
                officialLibraries = listOf("box2d", "box2dlights", "freetype"),
                unofficialLibraries = listOf("shapedrawer", "tenpatch", "stripe")
            ),
            "GWT_DEV" to LiftoffConfig(
                projectName = "gdx-gwt-dev-demo",
                packageName = "gdx.gwt",
                platforms = listOf("core", "lwjgl3", "gwt"),
                languages = listOf(),
                template = "classic",
                officialLibraries = listOf("box2d"),
                unofficialLibraries = listOf("shapedrawer", "tenpatch", "stripe", "formic", "regexodus")
            ),
            "TEA_DEV" to LiftoffConfig(
                projectName = "gdx-gwt-dev-demo",
                packageName = "gdx.gwt",
                platforms = listOf("core", "lwjgl3", "teavm"),
                languages = listOf("kotlin"),
                template = "kotlin-classic",
                officialLibraries = listOf("box2d"),
                unofficialLibraries = listOf("shapedrawer", "tenpatch", "stripe")
            )
        )
    }
}
