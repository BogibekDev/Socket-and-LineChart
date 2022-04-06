package dev.matyaqubov.socketinandroid.model

data class BitCoin(
    val channel: String,
    val data: Data,
    val event: String
)

data class Data(
    val amount: Double,
    val amount_str: String,
    val buy_order: Long,
    val id: Int,
    val microtimestamp: String,
    val price: Double,
    val price_str: String,
    val sell_order_id: Long,
    val timestamp: String,
    val type: Int,

    )