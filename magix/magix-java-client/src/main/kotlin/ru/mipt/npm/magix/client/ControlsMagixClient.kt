package ru.mipt.npm.magix.client

import hep.dataforge.magix.api.MagixEndpoint
import hep.dataforge.magix.api.MagixMessage
import hep.dataforge.magix.api.MagixMessageFilter
import hep.dataforge.magix.service.RSocketMagixEndpoint
import hep.dataforge.magix.service.withTcp
import kotlinx.coroutines.jdk9.asPublisher
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.JsonElement
import java.util.concurrent.Flow

public class ControlsMagixClient<T>(
    private val endpoint: MagixEndpoint,
    private val filter: MagixMessageFilter,
    private val serializer: KSerializer<T>,
) : MagixClient<T> {

    override fun broadcast(msg: MagixMessage<T>): Unit = runBlocking {
        endpoint.broadcast(serializer, msg)
    }

    override fun subscribe(): Flow.Publisher<MagixMessage<T>> = endpoint.subscribe(serializer, filter).asPublisher()

    public companion object {

        public fun rSocketTcp(host: String, port: Int): ControlsMagixClient<JsonElement> {
            val endpoint = runBlocking {
                RSocketMagixEndpoint.withTcp(host, port)
            }
            return ControlsMagixClient(endpoint, MagixMessageFilter(), JsonElement.serializer())
        }

        public fun rSocketWs(host: String, port: Int, path: String = "/rsocket"): ControlsMagixClient<JsonElement> {
            val endpoint = runBlocking {
                RSocketMagixEndpoint.withWebSockets(host, port, path)
            }
            return ControlsMagixClient(endpoint, MagixMessageFilter(), JsonElement.serializer())
        }
    }
}