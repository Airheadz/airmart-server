package xyz.notarealtree.airmart

import io.dropwizard.Application
import io.dropwizard.setup.Environment
import xyz.notarealtree.airmart.resource.BasketResource

class AirMart: Application<AirMartConfiguration>() {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            AirMart().run(*args)
        }
    }

    override fun run(configuration: AirMartConfiguration?, environment: Environment?) {
        val resource = BasketResource()
        environment?.jersey()?.register(resource)
    }

}