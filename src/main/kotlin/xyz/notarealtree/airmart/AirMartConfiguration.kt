package xyz.notarealtree.airmart

import com.fasterxml.jackson.annotation.JsonProperty
import io.dropwizard.Configuration

class AirMartConfiguration : Configuration() {
    @JsonProperty("discordWebhookUrl")
    var discordWebhookUrl: String=""

    @JsonProperty("discordUserAgent")
    var discordUserAgent: String="Stranger"
}