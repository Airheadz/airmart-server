package xyz.notarealtree.airmart.model

class Basket(val basketId: String) {
    val items = mutableMapOf<String, Int>()
    val regex = Regex("(x\\s?[0-9].*|[0-9].*\\s?x)")

    fun addItems(newItems: List<String>): Basket {
        for (newItem in newItems) {
            var count = 1
            var name = newItem
            val match = regex.find(newItem, 0)
            if (match != null) {
                count = match.value.replace(" ", "").replace("x", "").replace("X", "").toInt()
                name = name.removeRange(match.range)
            }
            items.computeIfPresent(name, { _, value -> value + count})
            items.putIfAbsent(name, count)
        }
        return this
    }

    override fun toString(): String {
        val output = items.toSortedMap().map({ entry -> entry.key + " x" + entry.value }).joinToString(separator = "\n")
        return output
    }
}