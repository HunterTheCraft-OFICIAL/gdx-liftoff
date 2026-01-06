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
  }
}