package xyz.notarealtree.airmart.resource

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.kittinunf.fuel.Fuel
import com.google.common.base.Preconditions
import com.google.common.collect.ImmutableMap
import com.google.common.hash.Hashing
import org.slf4j.LoggerFactory
import xyz.notarealtree.airmart.AirMartConfiguration
import xyz.notarealtree.airmart.model.Order
import java.util.*
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path("/basket")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class BasketResource(val config: AirMartConfiguration) {

    val log = LoggerFactory.getLogger("BasketResource")
    val baskets = hashMapOf<String, Basket>()
    var appraiser = AppraisalResource(config)

    @GET
    @Path("/all")
    fun getAllBaskets(): MutableMap<String, Basket> {
        return baskets
    }

    @GET
    @Path("/create")
    fun createBasket(): String {
        log.info("Created basket")
        val basketId = UUID.randomUUID().toString()
        baskets.put(basketId, Basket(basketId))
        return basketId
    }

    @POST
    @Path("/{basketId}/add")
    fun addToBasket(@PathParam("basketId") basketId: String, items: Map<String, Int>) {
        Preconditions.checkArgument(baskets.containsKey(basketId), "Specified basket $basketId does not exist.")
        baskets.computeIfPresent(basketId, { _, basket -> basket.addItems(items)})
    }

    @POST
    @Path("/{basketId}/checkout/customer/{customer}/location/{location}")
    fun checkout(@PathParam("basketId") basketId: String,
                 @PathParam("customer") customer: String,
                 @PathParam("location") location: String) : Order {
        Preconditions.checkArgument(baskets.containsKey(basketId), "Specified basket $basketId does not exist.")
        val items = baskets[basketId].toString()
        val orderId = Hashing.sha256().hashString(basketId, Charsets.UTF_8).toString()

        val orderText = formatOrderText(customer, location, orderId, items)

        var mapper = ObjectMapper()
        val writeValueAsString = mapper.writeValueAsString(ImmutableMap.of("content", "```$orderText```"))

        Fuel.post(config.discordWebhookUrl)
                .header(Pair("Content-type", "application/json"))
                .header(Pair("User-Agent", config.discordUserAgent))
                .body(writeValueAsString)
                .response()
                // TODO Post to webhook?
        return Order(orderId)
    }

    private fun formatOrderText(customer: String, location: String, orderId: String, items: String): String {
        val appraisal = appraiser.getAppraisal(items)
        return "An order has been placed!\n" +
                "OrderId: $orderId\n" +
                "Customer: $customer\n" +
                "Location: $location\n" +
                "Items: \n\n$items\n\n" +
                "Total Volume: ${appraisal.totals.volume}m3\n" +
                "Total Price: ${appraisal.totals.sell} ISK"
    }

}