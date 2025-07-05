package top.phj233.kjsky.controller.admin

import cn.dev33.satoken.annotation.SaCheckRole
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.web.bind.annotation.*
import top.phj233.kjsky.common.ApiResponse
import top.phj233.kjsky.common.ResponseUtil
import top.phj233.kjsky.model.Category
import top.phj233.kjsky.model.dto.CategoryDTO
import top.phj233.kjsky.model.dto.CategoryPageQueryDTO
import top.phj233.kjsky.service.CategoryService

/**
 * 管理端/分类控制器
 * @author phj233
 * @since 2025/7/1 19:44
 * @version
 */
@RestController
@RequestMapping("/admin/category")
@SaCheckRole("employee")
class CategoryController(val categoryService: CategoryService) {

    val logger = LoggerFactory.getLogger(this::class.java)
    /**
     * 新增分类
     * @param categoryDTO 分类DTO
     * @return ApiResponse<Category> 新增的分类对象
     */
    @PostMapping
    fun saveCategory(@RequestBody categoryDTO: CategoryDTO): ApiResponse<Category> {
        return ResponseUtil.success(categoryService.saveCategory(categoryDTO))
    }

    /**
     * 分类分页查询
     * @param categoryPageQueryDTO 分类分页查询DTO
     * @return ApiResponse<Page<Category>> 分页查询结果
     */
    @GetMapping("/page")
    fun pageQuery(categoryPageQueryDTO: CategoryPageQueryDTO): ApiResponse<Page<Category>> {
        return ResponseUtil.success(categoryService.pageQuery(categoryPageQueryDTO))
    }

    /**
     * 删除分类
     * @param id 分类ID
     * @return ApiResponse<String> 删除结果
     */
    @DeleteMapping
    fun deleteCategory(id: Long): ApiResponse<String> {
        logger.info("删除分类，ID: $id")
        categoryService.deleteCategory(id)
        return ResponseUtil.success("分类删除成功")
    }

    /**
     * 修改分类
     * @param categoryDTO 分类DTO
     * @return ApiResponse<Category> 修改后的分类对象
     */
    @PutMapping
    fun updateCategory(@RequestBody categoryDTO: CategoryDTO): ApiResponse<Category> {
        return ResponseUtil.success(categoryService.updateCategory(categoryDTO))
    }

    /**
     * 启用/禁用分类
     * @param id 分类ID
     * @param status 分类状态（0：禁用，1：启用）
     * @return ApiResponse<String> 启用/禁用结果
     */
    @PostMapping("/status/{status}")
    fun updateCategoryStatus(@PathVariable status: Int,id: Long): ApiResponse<String> {
        val category = categoryService.updateCategoryStatus(id, status)
        return ResponseUtil.success("分类状态更新成功，当前状态：${category.status}")
    }

    /**
     * 根据类型获取分类列表
     * @param type 分类类型
     * @return ApiResponse<List<Category>> 分类列表
     */
    @GetMapping("/list")
    fun getCategoryList(@RequestParam type: Int): ApiResponse<List<Category>> {
        return ResponseUtil.success(categoryService.findCategoryByType(type))
    }
}
