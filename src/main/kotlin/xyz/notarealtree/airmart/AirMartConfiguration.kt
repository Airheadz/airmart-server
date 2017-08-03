package xyz.notarealtree.airmart

import com.fasterxml.jackson.annotation.JsonProperty
import io.dropwizard.Configuration
import xyz.notarealtree.airmart.model.SsoConfig

class AirMartConfiguration : Configuration() {
    @JsonProperty("discordWebhookUrl")
    var discordWebhookUrl: String=""

    @JsonProperty("discordUserAgent")
    var discordUserAgent: String="Stranger"

    @JsonProperty("redisClientUrl")
    var redisClientUrl: String = "redis://localhost"

    @JsonProperty("sso")
    var ssoConfig: SsoConfig = SsoConfig()

    @JsonProperty("fittingsBasePath")
    val fittingsBasePath: String = ""
}
