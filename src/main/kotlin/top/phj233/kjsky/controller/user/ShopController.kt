package top.phj233.kjsky.controller.user

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import top.phj233.kjsky.common.ApiResponse
import top.phj233.kjsky.common.ResponseUtil
import top.phj233.kjsky.config.KJSkyProperties
import top.phj233.kjsky.service.ShopService

/**
 * 用户端/商店控制器
 * @author phj233
 * @since 2025/7/5 10:06
 * @version
 */
@RestController("userShopController")
@RequestMapping("/user/shop")
class ShopController(val shopService: ShopService) {

    /**
     * 获取店铺的营业状态
     * @return ApiResponse<Int>
     */
    @GetMapping("/status")
    fun getStatus(): ApiResponse<Int> {
        return ResponseUtil.success(shopService.getStatus())
    }

    /**
     * 获取店铺的信息
     * @return ApiResponse<String>
     */
    @GetMapping("/getMerchantInfo")
    fun getMerchantInfo(): ApiResponse<KJSkyProperties.ShopProperties> {
        return ResponseUtil.success(shopService.getMerchantInfo())
    }

}
