package xyz.notarealtree.airmart

import com.lambdaworks.redis.RedisClient
import com.lambdaworks.redis.api.sync.RedisStringCommands
import io.dropwizard.Application
import io.dropwizard.setup.Environment
import xyz.notarealtree.airmart.checks.RedisHealthCheck
import xyz.notarealtree.airmart.resource.AppraisalResource
import xyz.notarealtree.airmart.resource.BasketResource
import xyz.notarealtree.airmart.resource.CharacterResource
import xyz.notarealtree.airmart.resource.FittingsResource
import xyz.notarealtree.airmart.service.ApiProxy
import xyz.notarealtree.airmart.service.DbManager
import xyz.notarealtree.airmart.service.FittingManager
import org.eclipse.jetty.servlets.CrossOriginFilter
import javax.servlet.DispatcherType
import java.util.EnumSet





class AirMart: Application<AirMartConfiguration>() {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            AirMart().run(*args)
        }
    }

    override fun run(configuration: AirMartConfiguration, environment: Environment?) {
        val dbManager = DbManager(redisClient(configuration))
        val proxy = ApiProxy(configuration)
        val fittingsManager = FittingManager(configuration)
        environment?.jersey()?.register(BasketResource(configuration, dbManager, proxy))
        environment?.jersey()?.register(AppraisalResource(configuration))
        environment?.jersey()?.register(CharacterResource(configuration))
        environment?.jersey()?.register(FittingsResource(fittingsManager))

        environment?.healthChecks()?.register("redis", RedisHealthCheck(redisClient(configuration)))

        val cors = environment?.servlets()?.addFilter("CORS", CrossOriginFilter::class.java)
        cors?.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*")
        cors?.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "X-Requested-With,Content-Type,Accept,Origin,Authorization")
        cors?.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "OPTIONS,GET,PUT,POST,DELETE,HEAD")
        cors?.setInitParameter(CrossOriginFilter.ALLOW_CREDENTIALS_PARAM, "true")

        // Add URL mapping
        cors?.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType::class.java), true, "/*")
    }

    // TODO: check whether this has problems with concurrent calls
    private fun redisClient(configuration: AirMartConfiguration) : RedisStringCommands<String, String> {
        val redisClient = RedisClient.create(configuration.redisClientUrl)
        val connection = redisClient.connect()
        val synchronousConnection = connection.sync()
        return synchronousConnection
    }

}