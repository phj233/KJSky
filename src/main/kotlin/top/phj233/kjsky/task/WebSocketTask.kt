package top.phj233.kjsky.task

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import top.phj233.kjsky.websocket.WebSocketServer
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * WebSocket任务类，用于处理WebSocket相关的任务
 * @author phj233
 * @since 2025/7/4 16:02
 * @version
 */
@Component
class WebSocketTask(val webSocketServer: WebSocketServer) {
    /**
     * 通过WebSocket每隔3秒向客户端发送消息
     */
    @Scheduled(cron = "0/3 * * * * ?")
    fun sendMessageToClient() {
        webSocketServer.sendToAllClient("这是来自服务端的消息：${DateTimeFormatter.ofPattern("HH:mm:ss")
                .format(LocalDateTime.now())}") }

}
