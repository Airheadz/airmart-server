package xyz.notarealtree.airmart.resource

import com.google.common.base.Preconditions
import com.google.common.hash.Hashing
import org.slf4j.LoggerFactory
import xyz.notarealtree.airmart.AirMartConfiguration
import xyz.notarealtree.airmart.model.BasketResponse
import xyz.notarealtree.airmart.model.Order
import xyz.notarealtree.airmart.service.DbManager
import java.util.*
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path("/basket")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class BasketResource(val config: AirMartConfiguration, val dbManager: DbManager) {

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

    @GET
    @Path("/{basketId}")
    fun getBasket(@PathParam("basketId") basketId: String) : BasketResponse {
        return BasketResponse(1.0, 1.0, "1")
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