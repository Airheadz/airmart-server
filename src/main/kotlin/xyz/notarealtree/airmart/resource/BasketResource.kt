package xyz.notarealtree.airmart.resource

import com.google.common.base.Preconditions
import org.slf4j.LoggerFactory
import java.util.*
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path("/basket")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class BasketResource {

    val log = LoggerFactory.getLogger("BasketResource")
    val baskets = hashMapOf<String, Basket>()

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
    @Path("/add/{basketId}")
    fun addToBasket(@QueryParam("basketId") basketId: String, items: List<Pair<String, Int>>) {
        Preconditions.checkArgument(baskets.containsKey(basketId), "Specified basket $basketId does not exist.")
        baskets.computeIfPresent(basketId, { _, basket -> basket.addItems(items)})
    }

}