package gdx.liftoff

class LiftoffCli {

  fun run(args: Array<String>) {
    println(
      """
      Liftoff Headless CLI – Probe Mode

      This is a diagnostic entrypoint.
      No project generation is performed.
      """.trimIndent(),
    )

    if (args.isEmpty()) {
      println("No arguments provided.")
      return
    }

    println("Arguments:")
    args.forEachIndexed { index, arg ->
      println("  [$index] $arg")
    }
  }
}

fun main(args: Array<String>) {
  LiftoffCli().run(args)
}