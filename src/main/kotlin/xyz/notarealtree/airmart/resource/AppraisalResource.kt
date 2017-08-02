package xyz.notarealtree.airmart.resource

import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.google.common.collect.ImmutableList
import xyz.notarealtree.airmart.AirMartConfiguration
import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

import com.fasterxml.jackson.module.kotlin.*
import xyz.notarealtree.airmart.model.EvePraisalResponse

@Path("/appraisal")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class AppraisalResource(val config: AirMartConfiguration) {
    @GET
    @Path("/appraise")
    fun getAppraisal(items: String): EvePraisalResponse {
        val (_, response, _) = "http://evepraisal.com/appraisal"
                .httpPost(listOf(
                        Pair("raw_textarea", items),
                        Pair("market", "jita"))).response()
        val header = response.httpResponseHeaders["X-Appraisal-Id"].toString()
        val appraisalId = header.substring(1, header.length - 1)
        val (_, jsonResponse, _) = "http://evepraisal.com/a/$appraisalId.json".httpGet().response()

        val evePraisal = jacksonObjectMapper().readValue<EvePraisalResponse>(String(jsonResponse.data))

        return evePraisal
    }
}