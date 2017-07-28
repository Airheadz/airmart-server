package xyz.notarealtree.airmart.resource

import xyz.notarealtree.airmart.model.Order
import xyz.notarealtree.airmart.service.DbManager
import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/order")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class OrderResource(val dbManager: DbManager) {
    @GET
    @Path("/{orderId}")
    fun getOrder(orderId: String): Order {
        return dbManager.getOrder(orderId)
    }
}