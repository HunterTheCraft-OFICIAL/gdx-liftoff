package gdx.liftoff

data class LiftoffConfig(
    val projectName: String,
    val rootPackage: String,
    val platforms: List<String>,
    val languages: List<String>,
    val template: String,
    val officialExtensions: List<String>,
    val thirdPartyExtensions: List<String>,
    val addSkin: Boolean = true
) {
    companion object {
        val DEFAULT_PRESETS = mapOf(
            "DEFAULT" to LiftoffConfig(
                projectName = "gdx-liftoff-demo",
                rootPackage = "gdx.liftoff",
                platforms = listOf("core", "lwjgl3", "android", "ios", "gwt"),
                languages = emptyList(),
                template = "classic",
                officialExtensions = emptyList(),
                thirdPartyExtensions = emptyList()
            ),
            "KOTLIN" to LiftoffConfig(
                projectName = "gdx-liftoff-demo-kotlin",
                rootPackage = "gdx.liftoff",
                platforms = listOf("core", "lwjgl3", "android", "ios", "teavm"),
                languages = listOf("kotlin"),
                template = "kotlin-classic",
                officialExtensions = emptyList(),
                thirdPartyExtensions = emptyList()
            ),
            "KTX" to LiftoffConfig(
                projectName = "ktx-demo",
                rootPackage = "ktx.demo",
                platforms = listOf("core", "lwjgl3", "android", "ios"),
                languages = listOf("kotlin"),
                template = "ktx",
                officialExtensions = emptyList(),
                thirdPartyExtensions = listOf("ktx-*"),
                addSkin = false
            ),
            "KTX_WEB" to LiftoffConfig(
                projectName = "ktx-demo-web",
                rootPackage = "ktx.demo",
                platforms = listOf("core", "lwjgl3", "android", "ios", "teavm"),
                languages = listOf("kotlin"),
                template = "ktx",
                officialExtensions = emptyList(),
                thirdPartyExtensions = listOf("ktx-* without async, artemis, script"),
                addSkin = false
            ),
            "ANDROID_DEV" to LiftoffConfig(
                projectName = "gdx-android-dev-demo",
                rootPackage = "gdx.android",
                platforms = listOf("core", "lwjgl3", "android"),
                languages = emptyList(),
                template = "classic",
                officialExtensions = listOf("box2d", "box2dlights", "freetype"),
                thirdPartyExtensions = listOf("shapedrawer", "tenpatch", "stripe")
            ),
            "GWT_DEV" to LiftoffConfig(
                projectName = "gdx-gwt-dev-demo",
                rootPackage = "gdx.gwt",
                platforms = listOf("core", "lwjgl3", "gwt"),
                languages = emptyList(),
                template = "classic",
                officialExtensions = listOf("box2d"),
                thirdPartyExtensions = listOf("shapedrawer", "tenpatch", "stripe", "formic", "regexodus")
            ),
            "TEA_DEV" to LiftoffConfig(
                projectName = "gdx-gwt-dev-demo",
                rootPackage = "gdx.gwt",
                platforms = listOf("core", "lwjgl3", "teavm"),
                languages = listOf("kotlin"),
                template = "kotlin-classic",
                officialExtensions = listOf("box2d"),
                thirdPartyExtensions = listOf("shapedrawer", "tenpatch", "stripe")
            )
        )

        val DEFAULT_CONFIG = LiftoffConfig(
            projectName = "liftoff-game",
            rootPackage = "com.example.liftoff",
            platforms = listOf("core", "lwjgl3"),
            languages = listOf("kotlin"),
            template = "classic",
            officialExtensions = listOf("gdx-ai"),
            thirdPartyExtensions = listOf("ktx-app")
        )
    }
}