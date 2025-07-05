package top.phj233.kjsky.controller.user

import org.springframework.web.bind.annotation.*
import top.phj233.kjsky.common.ApiResponse
import top.phj233.kjsky.common.ResponseUtil
import top.phj233.kjsky.common.constant.MessageConstant
import top.phj233.kjsky.model.ShoppingCart
import top.phj233.kjsky.model.dto.ShoppingCartDTO
import top.phj233.kjsky.service.ShoppingCartService

/**
 * 用户端/购物车控制器
 * @author phj233
 * @since 2025/7/5 10:06
 * @version
 */
@RestController("userShoppingCartController")
@RequestMapping("/user/shoppingCart")
class ShoppingCartController(val shoppingCartService: ShoppingCartService) {
    /**
     * 添加购物车
     * @param shoppingCartDTO 购物车DTO
     * @return ApiResponse<String>
     */
    @PostMapping("/add")
    fun add(@RequestBody shoppingCartDTO: ShoppingCartDTO): ApiResponse<String> {
        shoppingCartService.addShoppingCart(shoppingCartDTO)
        return ResponseUtil.success(MessageConstant.SUCCESS)
    }

    /**
     * 查看购物车
     * @return ApiResponse<List<ShoppingCart>>
     */
    @GetMapping("/list")
    fun list(): ApiResponse<List<ShoppingCart>> {
        return ResponseUtil.success(shoppingCartService.findShoppingCart())
    }


    /**
     * 清空购物车
     * @return ApiResponse<String>
     */
    @DeleteMapping("/clean")
    fun clean(): ApiResponse<String> {
        shoppingCartService.clearShoppingCart()
        return ResponseUtil.success(MessageConstant.SUCCESS)
    }


    /**
     * 删除购物车中一个商品
     * @param shoppingCartDTO 购物车DTO
     * @return ApiResponse<String>
     */
    @PostMapping("/sub")
    fun sub(@RequestBody shoppingCartDTO: ShoppingCartDTO): ApiResponse<String> {
        shoppingCartService.deleteShoppingCartItem(shoppingCartDTO)
        return ResponseUtil.success(MessageConstant.SUCCESS)
    }
}
