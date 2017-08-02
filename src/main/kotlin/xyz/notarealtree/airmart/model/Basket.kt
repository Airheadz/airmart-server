package xyz.notarealtree.airmart.model

class Basket(val basketId: String) {
    val items = mutableMapOf<String, Int>()

    fun addItems(newItems: Map<String, Int>): Basket {
        newItems.forEach({ (key, value) ->
            run {
                items.computeIfPresent(key, { _, v -> value + v })
                items.putIfAbsent(key, value)
            }
        })
        return this
    }

    override fun toString(): String {
        val output = items.toSortedMap().map({ entry -> entry.key + " x" + entry.value }).joinToString(separator = "\n")
        return output
    }
}