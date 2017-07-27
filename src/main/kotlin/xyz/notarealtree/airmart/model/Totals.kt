package xyz.notarealtree.airmart.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class Totals(val sell: Double, val volume: Double)