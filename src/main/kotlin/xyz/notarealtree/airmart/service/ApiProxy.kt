package xyz.notarealtree.airmart.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.kittinunf.fuel.Fuel
import com.google.common.collect.ImmutableMap
import xyz.notarealtree.airmart.AirMartConfiguration

class ApiProxy(val config: AirMartConfiguration) {
    val mapper = ObjectMapper()

    fun postMessageToDiscord(message: String) {
        val valueAsString = mapper.writeValueAsString(ImmutableMap.of("content", "```$message```"))
        Fuel.post(config.discordWebhookUrl)
                .header(Pair("Content-type", "application/json"))
                .header(Pair("User-Agent", config.discordUserAgent))
                .body(valueAsString)
                .response()
    }

}