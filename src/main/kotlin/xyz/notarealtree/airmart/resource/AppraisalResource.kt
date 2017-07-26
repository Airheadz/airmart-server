package xyz.notarealtree.airmart.resource

import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.google.common.collect.ImmutableList
import xyz.notarealtree.airmart.AirMartConfiguration
import xyz.notarealtree.airmart.model.Appraisal
import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/appraisal")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class AppraisalResource(val config: AirMartConfiguration) {
    @GET
    @Path("/appraise")
    fun getAllBaskets(): Appraisal {
        val (_, response, _) = "http://evepraisal.com/appraisal"
                .httpPost(ImmutableList.of(
                        Pair("raw_textarea", "Damage Control II x5\n Damage Control I x1"),
                        Pair("market", "jita"))).response()
        val header = response.httpResponseHeaders["X-Appraisal-Id"].toString()
        val appraisalId = header.substring(1, header.length - 1)
        val (_, jsonResponse, _) = "http://evepraisal.com/a/$appraisalId.json".httpGet().response()

        println(jsonResponse)

        return Appraisal(1.0)
    }
}