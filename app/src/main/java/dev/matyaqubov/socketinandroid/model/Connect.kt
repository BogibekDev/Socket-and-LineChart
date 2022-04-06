package dev.matyaqubov.socketinandroid.model


data class Connect(
	val data: DataSend? = null,
	val event: String? = null
)
data class DataSend(
	val channel: String? = null
)
