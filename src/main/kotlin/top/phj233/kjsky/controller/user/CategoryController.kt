package top.phj233.kjsky.controller.user

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import top.phj233.kjsky.common.ApiResponse
import top.phj233.kjsky.common.ResponseUtil
import top.phj233.kjsky.model.Category
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
     * @param type 分类类型
     * @return 分类列表
     */
    @GetMapping("/list")
    fun list(type: Int): ApiResponse<List<Category>> {
        return ResponseUtil.success(
            categoryService.findCategoryByType(type)
        )
    }



}
