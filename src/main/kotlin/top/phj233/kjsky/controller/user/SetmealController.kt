package top.phj233.kjsky.controller.user

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import top.phj233.kjsky.common.ApiResponse
import top.phj233.kjsky.common.ResponseUtil
import top.phj233.kjsky.common.constant.StatusConstant
import top.phj233.kjsky.model.dto.DishItemVO
import top.phj233.kjsky.module.dto.SetmealQueryDTO
import top.phj233.kjsky.module.dto.SetmealVO
import top.phj233.kjsky.service.SetmealService

/**
 * 用户端/套餐控制器
 * @author phj233
 * @since 2025/7/5 10:05
 * @version
 */
@RestController("userSetmealController")
@RequestMapping("/user/setmeal")
class SetmealController(val setmealService: SetmealService) {
    /**
     * 查询套餐列表
     * @param categoryId 分类ID
     * @return ApiResponse<List<SetmealVO>>
     */
    @GetMapping("/list")
//    @Cacheable(cacheNames = ["setmealCache"], key = "#categoryId")
    fun list(categoryId: Long): ApiResponse<List<SetmealVO>> {
        return ResponseUtil.success(
            setmealService.querySetmeal(
                SetmealQueryDTO(
                    categoryId = categoryId,
                    status = StatusConstant.ENABLE
                )
            )
        )
    }


    /**
     * 根据套餐id查询包含的菜品列表
     * @param id 套餐ID
     * @return
     */
    @GetMapping("/dish/{id}")
    fun getSetmealDishes(@PathVariable id: Long): ApiResponse<List<DishItemVO>> {
        return ResponseUtil.success(setmealService.findSetmealDishesById(id))
    }
}
