package gdx.liftoff

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.backends.headless.HeadlessApplication
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration
import gdx.liftoff.Listing
import org.json.JSONObject

object LiftoffCli {

    @JvmStatic
    fun main(args: Array<String>) {
        HeadlessApplication(object : com.badlogic.gdx.ApplicationAdapter() {}, HeadlessApplicationConfiguration())

        if (args.isEmpty() || args[0] != "dump") {
            error("Usage: LiftoffCli dump schema")
        }

        if (args.size >= 2 && args[1] == "schema") {
            dumpSchema()
        }
    }

    private fun dumpSchema() {
        val root = JSONObject()

        root.put("platforms", Listing.platforms.map { it.id })
        root.put("languages", Listing.languages.map { it.id })
        root.put("templates", Listing.templates.map { it.id })

        val libraries = JSONObject()
        libraries.put("official", Listing.officialLibraries.map { it.id })
        libraries.put("unofficial", Listing.unofficialNames.toList())

        root.put("libraries", libraries)

        println(root.toString(2))
    }
}