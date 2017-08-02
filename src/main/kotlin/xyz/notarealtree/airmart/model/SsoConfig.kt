package xyz.notarealtree.airmart.model

import com.fasterxml.jackson.annotation.JsonProperty

class SsoConfig {
    @JsonProperty("clientId")
    var clientId: String = ""

    @JsonProperty("clientSecret")
    var clientSecret: String = ""

    @JsonProperty("callback")
    var callback: String = ""

    @JsonProperty("scopes")
    var scopes = "publicData"
}