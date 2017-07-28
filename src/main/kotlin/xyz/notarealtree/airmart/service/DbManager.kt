package xyz.notarealtree.airmart.service

import com.lambdaworks.redis.api.sync.RedisStringCommands

class DbManager(val connection: RedisStringCommands<String, String>) {

}