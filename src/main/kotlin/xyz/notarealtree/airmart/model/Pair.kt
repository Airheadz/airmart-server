package xyz.notarealtree.airmart.model

import com.fasterxml.jackson.annotation.JsonProperty

class Pair {
    @JsonProperty("first")
    public var first: String= ""

    @JsonProperty("second")
    public var second: Int= 0
}