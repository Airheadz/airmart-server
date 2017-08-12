package xyz.notarealtree.airmart.resource

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.google.common.base.Preconditions
import xyz.notarealtree.airmart.AirMartConfiguration
import xyz.notarealtree.airmart.model.CharacterResponse
import xyz.notarealtree.airmart.model.LoginResponse
import xyz.notarealtree.airmart.model.TokenResponse
import java.net.URI
import java.net.URLEncoder
import java.util.*
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/character")
@Produces(MediaType.APPLICATION_JSON)
class CharacterResource(val configuration: AirMartConfiguration) {
    val knownStates = mutableSetOf<String>()
    val auth = String(Base64.getEncoder().encode("${configuration.ssoConfig.clientId}:${configuration.ssoConfig.clientSecret}".toByteArray()))
    val callbackUrlEncoded = URLEncoder.encode(configuration.ssoConfig.callback)
    val scopes = URLEncoder.encode(configuration.ssoConfig.scopes)

    @Path("/login")
    @GET
    fun login(): Response {
        val state = UUID.randomUUID().toString()
        val ssoUrl = URI("https://login.eveonline.com/oauth/authorize/?response_type=code" +
                "&redirect_uri=$callbackUrlEncoded&client_id=${configuration.ssoConfig.clientId}" +
                "&scope=$scopes&state=$state")
        knownStates.add(state)
        return Response.seeOther(ssoUrl).build()
    }

    @Path("/token")
    @GET
    fun token(@QueryParam("code") code: String, @QueryParam("state") state: String): LoginResponse {
        println("$code >> $state")
        Preconditions.checkArgument(state in knownStates, "Unknown state, request was tampered with")
        knownStates.remove(state)
        println("Basic $auth")

        val (_, response, _) = "https://login.eveonline.com/oauth/token"
                .httpPost(listOf(
                        Pair("grant_type", "authorization_code"),
                        Pair("code", code)))
                .header(Pair("Content-type", "application/x-www-form-urlencoded"))
                .header(Pair("Authorization", "Basic $auth"))
                .response()
        println(response)
        val tokenResponse = jacksonObjectMapper().readValue<TokenResponse>(String(response.data))

        val (_, characterResponse, _) = "https://login.eveonline.com/oauth/verify"
                .httpGet()
                .header(Pair("Authorization", "Bearer ${tokenResponse.access_token}"))
                .response()

        val parsedResponse = jacksonObjectMapper().readValue<CharacterResponse>(String(characterResponse.data))
        val loginResponse = LoginResponse(parsedResponse.CharacterName, tokenResponse.access_token)
        return loginResponse
    }
}