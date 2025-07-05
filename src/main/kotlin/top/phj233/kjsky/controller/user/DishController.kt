package top.phj233.kjsky.controller.user

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import top.phj233.kjsky.common.ApiResponse
import top.phj233.kjsky.common.ResponseUtil
import top.phj233.kjsky.module.dto.DishVO
import top.phj233.kjsky.service.DishService

/**
 * 用户端/菜品控制器
 * @author phj233
 * @since 2025/7/5 10:05
 * @version
 */
@RestController("userDishController")
@RequestMapping("/user/dish")
class DishController(
    val dishService: DishService
) {

    /**
     * 查询菜品-根据分类ID
     * @param categoryId 分类ID
     * @return ApiResponse<List<DishVO>>
     */
    @GetMapping("/list")
    fun list(categoryId: Long): ApiResponse<List<DishVO>> {
       return ResponseUtil.success(dishService.findDishListFromCacheOrDB(categoryId))
    }
}
