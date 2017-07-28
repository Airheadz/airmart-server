package xyz.notarealtree.airmart

import com.lambdaworks.redis.RedisClient
import com.lambdaworks.redis.api.sync.RedisStringCommands
import io.dropwizard.Application
import io.dropwizard.setup.Environment
import xyz.notarealtree.airmart.resource.AppraisalResource
import xyz.notarealtree.airmart.resource.BasketResource
import xyz.notarealtree.airmart.service.DbManager

class AirMart: Application<AirMartConfiguration>() {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            AirMart().run(*args)
        }
    }

    override fun run(configuration: AirMartConfiguration, environment: Environment?) {
        val dbManager = DbManager(redisClient(configuration))
        environment?.jersey()?.register(BasketResource(configuration, dbManager))
        environment?.jersey()?.register(AppraisalResource(configuration))
    }

    // TODO: check whether this has problems with concurrent calls
    private fun redisClient(configuration: AirMartConfiguration) : RedisStringCommands<String, String> {
        val redisClient = RedisClient.create(configuration.redisClientUrl)
        val connection = redisClient.connect()
        val synchronousConnection = connection.sync()
        return synchronousConnection
    }

}