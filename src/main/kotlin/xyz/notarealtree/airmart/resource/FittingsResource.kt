package xyz.notarealtree.airmart.resource

import xyz.notarealtree.airmart.model.Doctrine
import xyz.notarealtree.airmart.service.FittingManager
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/fittings")
@Produces(MediaType.APPLICATION_JSON)
class FittingsResource(val manager: FittingManager) {

    @GET
    @Path("/")
    fun getAllFittings(): List<Doctrine> {
        return manager.getFittings()
    }
}