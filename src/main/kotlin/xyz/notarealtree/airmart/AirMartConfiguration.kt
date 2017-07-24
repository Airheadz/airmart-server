package xyz.notarealtree.airmart

import com.fasterxml.jackson.annotation.JsonProperty
import io.dropwizard.Configuration

public class AirMartConfiguration : Configuration() {
    @JsonProperty("template")
    public var template: String=""

    @JsonProperty("defaultName")
    public var defaultName: String="Stranger"
}