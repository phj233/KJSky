package top.phj233.kjsky.websocket

import jakarta.websocket.OnClose
import jakarta.websocket.OnMessage
import jakarta.websocket.OnOpen
import jakarta.websocket.Session
import jakarta.websocket.server.PathParam
import jakarta.websocket.server.ServerEndpoint
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import top.phj233.kjsky.common.ResponseUtil

/**
 * WebSocket服务
 */
@Component
@ServerEndpoint("/ws/{sid}")
class WebSocketServer {
    val logger : Logger = LoggerFactory.getLogger(this::class.java)
    /**
     * 连接建立成功调用的方法
     * @param session WebSocket会话对象
     * @param sid 会话ID
     */
    @OnOpen
    fun onOpen(session: Session?, @PathParam("sid") sid: String?) {
        logger.info("客户端连接成功，sid: $sid")
        sessionMap.put(sid, session)
    }

    /**
     * 收到客户端消息后调用的方法
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    fun onMessage(message: String?, @PathParam("sid") sid: String?) {
        logger.info("收到客户端消息，sid: $sid, message: $message")
        // 这里可以处理接收到的消息
    }

    /**
     * 连接关闭调用的方法
     * @param sid
     */
    @OnClose
    fun onClose(@PathParam("sid") sid: String?) {
        logger.info("客户端连接关闭，sid: $sid")
        sessionMap.remove(sid)
    }

    /**
     * 群发
     * @param message
     */
    fun sendToAllClient(message: String?) {
        val sessions: MutableCollection<Session?> = sessionMap.values
        val data = hashMapOf(
            "msg" to message,
            "timestamp" to System.currentTimeMillis().toString(10)
        )
        for (session in sessions) {
            try {
                //服务器向客户端发送消息
                session?.asyncRemote?.sendObject(ResponseUtil.success(data))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    companion object {
        //存放会话对象
        private val sessionMap: MutableMap<String?, Session?> = HashMap<String?, Session?>()
    }
}
