package top.phj233.kjsky.controller.admin

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.web.bind.annotation.*
import top.phj233.kjsky.common.ApiResponse
import top.phj233.kjsky.common.ResponseUtil
import top.phj233.kjsky.common.constant.MessageConstant
import top.phj233.kjsky.common.constant.StatusConstant.KEY

/**
 * 店铺控制器
 * @author phj233
 * @since 2025/7/2 10:44
 * @version
 */
@RestController
@RequestMapping("/admin/shop")
class ShopController(val stringRedisTemplate: StringRedisTemplate) {

    val log: Logger = LoggerFactory.getLogger(this::class.java)
    /**
     * 设置店铺的营业状态
     * @param status
     * @return ApiResponse<String>
     */
    @PutMapping("/{status}")
    fun setStatus(@PathVariable status: Int): ApiResponse<String> {
        log.info("设置店铺的营业状态为：{}", if (status == 1) "营业中" else "打烊中")
        stringRedisTemplate.opsForValue().set(KEY, status.toString())
        log.info("店铺的营业状态已更新到Redis")
        return ResponseUtil.success(MessageConstant.SUCCESS)
    }

    /**
     * 获取店铺的营业状态
     * @return ApiResponse<Int>
     */
    @GetMapping("/status")
    fun getStatus(): ApiResponse<Int> {
        val status = stringRedisTemplate.opsForValue().get(KEY)?.toIntOrNull()
        log.info("获取到店铺的营业状态为：{}", if (status == 1) "营业中" else "打烊中")
        return ResponseUtil.success(status)
    }
}

