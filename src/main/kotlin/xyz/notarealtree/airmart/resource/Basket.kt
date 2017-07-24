package xyz.notarealtree.airmart.resource

class Basket(val basketId: String) {
    val items = mutableMapOf<String, Int>()

    fun addItems(newItems: List<Pair<String, Int>>): Basket {
        newItems.forEach({ (first, second) ->
            run {
                items.computeIfPresent(first, { _, v -> add(second, v) })
                items.putIfAbsent(first, second)
            }
        })
        return this
    }

    fun add(first: Int, second: Int) : Int{
        return first + second
    }
}