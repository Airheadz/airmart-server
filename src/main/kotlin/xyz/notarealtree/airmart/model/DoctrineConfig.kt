package xyz.notarealtree.airmart.model

data class DoctrineConfig(
        val name: String,
        val type: String,
        val colourClass: String,
        val data: Map<String, Any>
)