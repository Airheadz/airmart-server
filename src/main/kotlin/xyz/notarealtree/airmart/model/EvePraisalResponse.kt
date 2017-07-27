package xyz.notarealtree.airmart.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class EvePraisalResponse(val totals: Totals)