package build365.gst.emsigner

import org.java_websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.server.WebSocketServer
import java.net.InetSocketAddress

class WSServer(
    address: InetSocketAddress,
    val handleConnect: (WebSocket) -> Unit,
    val handleMessage: (WebSocket, String) -> Unit
) : WebSocketServer(address) {
    override fun onOpen(p0: WebSocket, p1: ClientHandshake?) {
        handleConnect(p0)
    }

    override fun onClose(p0: WebSocket, p1: Int, p2: String?, p3: Boolean) {

    }

    override fun onMessage(p0: WebSocket, p1: String) {
        //when asked to encrypt
        //bring app to font end
        handleMessage(p0, p1)
    }

    override fun onError(p0: WebSocket, p1: Exception?) {

    }

    override fun onStart() {

    }
}