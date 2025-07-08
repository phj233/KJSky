package top.phj233.kjsky.controller.user

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import top.phj233.kjsky.common.ApiResponse
import top.phj233.kjsky.common.ResponseUtil
import top.phj233.kjsky.model.Category
import top.phj233.kjsky.model.dto.CategoryUserListDTO
import top.phj233.kjsky.service.CategoryService

/**
 * 用户端/分类控制器
 * @author phj233
 * @since 2025/7/5 10:05
 * @version
 */
@RestController("userCategoryController")
@RequestMapping("/user/category")
class CategoryController(val categoryService: CategoryService) {

    /**
     * 查询分类
     * @param categoryUserListDTO 分类用户列表DTO
     * @return 分类列表
     */
    @GetMapping("/list")
    fun list(categoryUserListDTO: CategoryUserListDTO): ApiResponse<List<Category>> {
        LoggerFactory.getLogger(CategoryController::class.java).info("查询分类，类型：${categoryUserListDTO.type}")
        return ResponseUtil.success(
            categoryService.findCategoryByType(categoryUserListDTO.type)
        )
    }



}
