package xyz.notarealtree.airmart.checks

import com.codahale.metrics.health.HealthCheck
import com.lambdaworks.redis.api.sync.RedisStringCommands

class RedisHealthCheck(val conn : RedisStringCommands<String, String>): HealthCheck() {
    override fun check(): Result {
        try {
            conn.set("status", "alive")
            return Result.healthy()
        } catch (e: Exception) {
            return Result.unhealthy("Exception $e occurred when trying to set redis values")
        }
    }
}