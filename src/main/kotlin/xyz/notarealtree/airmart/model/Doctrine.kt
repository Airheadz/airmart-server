package xyz.notarealtree.airmart.model

data class Doctrine(
        val name: String,
        val type: String,
        val colourClass: String,
        val fittings: List<Fitting>
)