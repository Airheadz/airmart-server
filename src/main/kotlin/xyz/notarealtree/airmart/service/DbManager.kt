package xyz.notarealtree.airmart.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.google.common.base.Preconditions
import com.lambdaworks.redis.api.sync.RedisStringCommands
import xyz.notarealtree.airmart.model.Order

class DbManager(val connection: RedisStringCommands<String, String>) {
    val mapper = jacksonObjectMapper()

    fun addOrder(order: Order) {
        val serialized = mapper.writeValueAsString(order)
        connection.set(order.orderId, serialized)
    }

    fun getOrder(orderId: String): Order {
        val redisValue = connection.get(orderId)
        Preconditions.checkState(redisValue != null, "No value exists for orderId $orderId")
        return mapper.readValue<Order>(redisValue)
    }
}