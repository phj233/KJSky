package top.phj233.kjsky.controller.admin

import cn.dev33.satoken.annotation.SaCheckRole
import org.springframework.web.bind.annotation.*
import top.phj233.kjsky.common.ApiResponse
import top.phj233.kjsky.common.ResponseUtil
import top.phj233.kjsky.common.constant.MessageConstant
import top.phj233.kjsky.service.ShopService

/**
 * 管理端/店铺控制器
 * @author phj233
 * @since 2025/7/2 10:44
 * @version
 */
@RestController
@RequestMapping("/admin/shop")
@SaCheckRole("employee")
class ShopController(val shopService: ShopService) {
    /**
     * 设置店铺的营业状态
     * @param status
     * @return ApiResponse<String>
     */
    @PutMapping("/{status}")
    fun setStatus(@PathVariable status: Int): ApiResponse<String> {
        shopService.setStatus(status)
        return ResponseUtil.success(MessageConstant.SUCCESS)
    }

    /**
     * 获取店铺的营业状态
     * @return ApiResponse<Int>
     */
    @GetMapping("/status")
    fun getStatus(): ApiResponse<Int> {
        val status = shopService.getStatus()
        return ResponseUtil.success(status)
    }
}

