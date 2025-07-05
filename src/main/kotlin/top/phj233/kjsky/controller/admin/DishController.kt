package top.phj233.kjsky.controller.admin

import cn.dev33.satoken.annotation.SaCheckRole
import org.springframework.data.domain.Page
import org.springframework.web.bind.annotation.*
import top.phj233.kjsky.common.ApiResponse
import top.phj233.kjsky.common.ResponseUtil
import top.phj233.kjsky.common.constant.MessageConstant
import top.phj233.kjsky.module.dto.DishDTO
import top.phj233.kjsky.module.dto.DishPageQueryDTO
import top.phj233.kjsky.module.dto.DishVO
import top.phj233.kjsky.service.DishService

/**
 * 管理端/菜品控制器
 * @author phj233
 * @since 2025/6/30 19:54
 * @version
 */
@RestController
@RequestMapping("/admin/dish")
@SaCheckRole("employee")
class DishController(val dishService: DishService) {
    /**
     * 菜品分页查询
     * @param dishPageQueryDTO 菜品分页查询DTO
     * @return ApiResponse<Page<DishVO>> 分页查询结果
     */
    @GetMapping("/page")
    fun page(dishPageQueryDTO: DishPageQueryDTO): ApiResponse<Page<DishVO>> {
        return ResponseUtil.success(dishService.pageQuery(dishPageQueryDTO))
    }

    /**
     * 新增菜品
     * @param dishDTO 菜品DTO
     * @return ApiResponse<DishVO> 新增的菜品视图对象
     */
    @PostMapping
    fun save (@RequestBody dishDTO: DishDTO): ApiResponse<DishVO> {
        return ResponseUtil.success(dishService.saveDish(dishDTO))
    }

    /**
     * 菜品批量删除
     * @param ids List<Long> 菜品ID列表
     * @return ApiResponse<String> 删除结果
     */
    @DeleteMapping
    fun delete(@RequestParam ids: List<Long>): ApiResponse<String> {
        dishService.deleteDishes(ids)
        return ResponseUtil.success(MessageConstant.SUCCESS)
    }

    /**
     * 根据菜品ID获取菜品信息
     * @param id Long 菜品ID
     * @return ApiResponse<DishVO> 菜品视图对象
     */
    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ApiResponse<DishVO> {
        return ResponseUtil.success(dishService.findDishById(id))
    }

    /**
     * 修改菜品
     * @param dishDTO 菜品DTO
     * @return ApiResponse<DishVO> 修改后的菜品视图对象
     */
    @PutMapping
    fun update(@RequestBody dishDTO: DishDTO): ApiResponse<DishVO> {
        return ResponseUtil.success(dishService.updateDish(dishDTO))
    }

    /**
     * 菜品起售/停售
     * @param id Long 菜品ID
     * @param status Int 菜品状态 1起售 0停售
     * @return ApiResponse<DishVO> 更新后的菜品视图对象
     */
    @PostMapping("/status/{status}")
    fun updateStatus(id: Long, @PathVariable status: Int): ApiResponse<DishVO> {
        return ResponseUtil.success(dishService.updateDishStatus(id, status))
    }

    /**
     * 根据分类ID获取菜品列表
     * @param categoryId Long 分类ID
     * @return ApiResponse<List<DishVO>> 菜品列表
     */
    @GetMapping("/list")
    fun getByCategoryId(@RequestParam categoryId: Long): ApiResponse<List<DishVO>> {
        return ResponseUtil.success(dishService.findDishesByCategoryId(categoryId))
    }
}
