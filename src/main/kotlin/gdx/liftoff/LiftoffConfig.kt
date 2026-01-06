package gdx.liftoff

data class LiftoffConfig(
    val projectName: String,
    val packageName: String,
    val platforms: List<String>,
    val languages: List<String>,
    val template: String,
    val officialLibraries: List<String>,
    val unofficialLibraries: List<String>,
    val addSkin: Boolean = true
) {
    companion object {
        val DEFAULT = LiftoffConfig(
            projectName = "gdx-liftoff-demo",
            packageName = "gdx.liftoff",
            platforms = listOf("core", "lwjgl3", "android", "ios", "gwt"),
            languages = emptyList(),
            template = "classic",
            officialLibraries = emptyList(),
            unofficialLibraries = emptyList(),
            addSkin = true
        )

        val KOTLIN = LiftoffConfig(
            projectName = "gdx-liftoff-demo-kotlin",
            packageName = "gdx.liftoff",
            platforms = listOf("core", "lwjgl3", "android", "ios", "teavm"),
            languages = listOf("kotlin"),
            template = "kotlin-classic",
            officialLibraries = emptyList(),
            unofficialLibraries = emptyList(),
            addSkin = true
        )

        val KTX = LiftoffConfig(
            projectName = "ktx-demo",
            packageName = "ktx.demo",
            platforms = listOf("core", "lwjgl3", "android", "ios"),
            languages = listOf("kotlin"),
            template = "ktx",
            officialLibraries = emptyList(),
            unofficialLibraries = listOf("ktx-*"),
            addSkin = false
        )

        val KTX_WEB = LiftoffConfig(
            projectName = "ktx-demo-web",
            packageName = "ktx.demo",
            platforms = listOf("core", "lwjgl3", "android", "ios", "teavm"),
            languages = listOf("kotlin"),
            template = "ktx",
            officialLibraries = emptyList(),
            unofficialLibraries = listOf("ktx-* without async, artemis, script"),
            addSkin = false
        )

        val ANDROID_DEV = LiftoffConfig(
            projectName = "gdx-android-dev-demo",
            packageName = "gdx.android",
            platforms = listOf("core", "lwjgl3", "android"),
            languages = emptyList(),
            template = "classic",
            officialLibraries = listOf("box2d", "box2dlights", "freetype"),
            unofficialLibraries = listOf("shapedrawer", "tenpatch", "stripe"),
            addSkin = true
        )

        val GWT_DEV = LiftoffConfig(
            projectName = "gdx-gwt-dev-demo",
            packageName = "gdx.gwt",
            platforms = listOf("core", "lwjgl3", "gwt"),
            languages = emptyList(),
            template = "classic",
            officialLibraries = listOf("box2d"),
            unofficialLibraries = listOf("shapedrawer", "tenpatch", "stripe", "formic", "regexodus"),
            addSkin = true
        )

        val TEA_DEV = LiftoffConfig(
            projectName = "gdx-gwt-dev-demo",
            packageName = "gdx.gwt",
            platforms = listOf("core", "lwjgl3", "teavm"),
            languages = listOf("kotlin"),
            template = "kotlin-classic",
            officialLibraries = listOf("box2d"),
            unofficialLibraries = listOf("shapedrawer", "tenpatch", "stripe"),
            addSkin = true
        )

        // Lista de presets para referência dinâmica no CLI
        val PRESETS: Map<String, LiftoffConfig> = mapOf(
            "DEFAULT" to DEFAULT,
            "KOTLIN" to KOTLIN,
            "KTX" to KTX,
            "KTX_WEB" to KTX_WEB,
            "ANDROID_DEV" to ANDROID_DEV,
            "GWT_DEV" to GWT_DEV,
            "TEA_DEV" to TEA_DEV
        )
    }
}

/**
 * Retorna um preset válido com base no nome, ou DEFAULT caso inválido.
 */
fun getPreset(name: String?): LiftoffConfig {
    if (name == null) return LiftoffConfig.DEFAULT
    return LiftoffConfig.PRESETS[name.uppercase()] ?: LiftoffConfig.DEFAULT
}