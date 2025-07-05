package top.phj233.kjsky.controller.notify

import cn.hutool.http.ContentType
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import top.phj233.kjsky.common.WeChatPayUtil
import top.phj233.kjsky.config.KJSkyProperties
import top.phj233.kjsky.service.OrderService
import java.io.BufferedReader
import java.nio.charset.StandardCharsets

/**
 * 支付回调/回调控制器
 * @author phj233
 * @since 2025/7/4 14:56
 * @version
 */
@RestController
@RequestMapping("/notify")
class PayNotifyController(
    val orderService: OrderService,
    val kjSkyProperties: KJSkyProperties,
    private val weChatPayUtil: WeChatPayUtil
) {

    val log : Logger = LoggerFactory.getLogger(this::class.java)

    /**
     * 支付成功回调
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    @GetMapping("/paySuccess")
    fun paySuccess(request: HttpServletRequest, response: HttpServletResponse) {
        //读取数据
        val body = readData(request)
        log.info("支付成功回调：{}", body)
        //数据解密
        val plainText: String = decryptData(body)
        log.info("解密后的文本：{}", plainText)

        val jsonObject: JsonNode = ObjectMapper().readTree(plainText)
        // 获取商户平台订单号和微信支付交易号
        val outTradeNo: String = jsonObject.get("out_trade_no").textValue()
        val transactionId: String = jsonObject.get("transaction_id").textValue()
        log.info("商户平台订单号：{}", outTradeNo)
        log.info("微信支付交易号：{}", transactionId)
        //业务处理，修改订单状态、来单提醒
        orderService.paySuccess(outTradeNo)
        //给微信响应
        responseToWeixin(response)
    }

    /**
     * 读取数据
     * @param request HttpServletRequest
     * @return String
     * @throws Exception
     */
    private fun readData(request: HttpServletRequest): String {
        val reader: BufferedReader = request.reader
        val result = StringBuilder()
        var line: String? = null
        while ((reader.readLine().also { line = it }) != null) {
            if (result.isNotEmpty()) {
                result.append("\n")
            }
            result.append(line)
        }
        return result.toString()
    }

    /**
     * 数据解密
     * @param body 加密的请求体
     * @return 解密后的字符串
     */
    private fun decryptData(body: String) :String{
        val resource = ObjectMapper().readTree(body)
        val ciphertext: String? = resource.get("ciphertext").textValue()
        val nonce: String = resource.get("nonce").textValue()
        val associatedData: String = resource.get("associated_data").textValue()
        // TODO: 这里需要使用商户平台的私钥进行解密
        return "fake decrypted data"
    // weChatPayUtil.decryptData(ciphertext, nonce, associatedData, kjSkyProperties.wechatPayPrivateKey)
    }

    /**
     * 给微信响应
     * @param response HttpServletResponse
     */
    private fun responseToWeixin(response: HttpServletResponse) {
        response.status = 200
        val map = mutableMapOf<String, String>()
        map.put("code", "SUCCESS")
        map.put("message", "SUCCESS")
        response.setHeader("Content-type", ContentType.JSON.toString())
        response.outputStream.write(ObjectMapper().writeValueAsString(map).toByteArray(StandardCharsets.UTF_8))
        response.flushBuffer()
    }
}
