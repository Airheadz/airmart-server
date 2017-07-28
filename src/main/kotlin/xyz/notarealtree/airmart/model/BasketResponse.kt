package xyz.notarealtree.airmart.model

data class BasketResponse(
        val totalVolume: Double,
        val totalPrice: Double,
        val basketAsString: String)