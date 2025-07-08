package top.phj233.kjsky.common

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.io.Serializable

data class ApiResponse<T>(
    val code: Int,
    val msg: String,
    val data: T?
): Serializable {
    companion object {
        private const val serialVersionUID: Long = 1L
    }
}
object ResponseUtil {

    /**
     * 构建一个成功的响应
     * @param data 数据
     * @param <T> 数据类型
     * @return ResponseEntity<ApiResponse<T>>
     */
    fun <T> success(data: T?): ApiResponse<T> {
        return ApiResponse(1, "success", data)
    }

    /**
     * 构建一个成功的响应
     */
    fun <T> success(): ApiResponse<T> {
        return ApiResponse(1, "success", null)
    }

    /**
     * 构建一个错误的响应
     *
     * @param code    错误码
     * @param message 错误信息
     * @param <T>     数据类型
     * @return ResponseEntity<ApiResponse<T>>
     */
    fun <T> error(code: Int, message: String): ResponseEntity<ApiResponse<T>> {
        return ResponseEntity.status(HttpStatus.valueOf(code)).body(ApiResponse(code, message, null))
    }

    /**
     * 构建一个自定义的响应
     *
     * @param code    状态码
     * @param message 消息
     * @param data    数据
     * @param <T>     数据类型
     * @return ResponseEntity<ApiResponse<T>>
     */
    fun <T> custom(code: Int, message: String, data: T?): ResponseEntity<ApiResponse<T>> {
        return ResponseEntity.status(HttpStatus.valueOf(code)).body(ApiResponse(code, message, data))
    }
}
