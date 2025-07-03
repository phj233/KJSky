package top.phj233.kjsky.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import top.phj233.kjsky.common.constant.MessageConstant
import top.phj233.kjsky.common.exception.BaseException
import top.phj233.kjsky.common.exception.DeletionNotAllowedException
import top.phj233.kjsky.model.Category
import top.phj233.kjsky.model.dto.CategoryDTO
import top.phj233.kjsky.model.dto.CategoryPageQueryDTO
import top.phj233.kjsky.repository.CategoryRepository
import top.phj233.kjsky.repository.DishRepository
import top.phj233.kjsky.repository.SetmealRepository

/**
 * 分类服务
 * @author phj233
 * @since 2025/7/1 19:46
 * @version
 */
@Service
class CategoryService(
    val categoryRepository: CategoryRepository, repository: CategoryRepository,
    val dishRepository: DishRepository,
    val setmealRepository: SetmealRepository) {
    /**
     * 新增分类
     * @param categoryDTO 分类DTO
     */
    fun saveCategory(categoryDTO: CategoryDTO): Category {
        categoryDTO.copy(status = 0).let {
            categoryRepository.save(it).let {category ->
                return category
            }
        }
    }

    /**
     * 分类分页查询
     * @param categoryPageQueryDTO 分类分页查询DTO
     * @return Page<Category> 分页查询结果
     */
    fun pageQuery(categoryPageQueryDTO: CategoryPageQueryDTO): Page<Category> {
        return categoryRepository.findByNameLikeAndType(
            categoryPageQueryDTO.name,
            categoryPageQueryDTO.type,
            PageRequest.of(categoryPageQueryDTO.page, categoryPageQueryDTO.pageSize)
        )
    }

    /**
     * 删除分类
     * @param id 分类ID
     */
    fun deleteCategory(id: Long) {
        // 检查是否有菜品或套餐关联
        dishRepository.existsById(id).takeIf { it }?.let {
            throw DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_DISH)}
        setmealRepository.existsById(id).takeIf { it }?.let {
            throw DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_SETMEAL)}
        categoryRepository.deleteById(id)
    }

    /**
     * 更新分类
     * @param categoryDTO 分类DTO
     */
    fun updateCategory(categoryDTO: CategoryDTO): Category {
        return categoryRepository.save(
            categoryDTO
        )
    }

    /**
     * 启用/禁用分类
     * @param id 分类ID
     * @param status 分类状态 1启用 0禁用
     */
    fun updateCategoryStatus(id: Long, status: Int): Category {
        return categoryRepository.updateCategoryStatusById(id, status).let {
            if (it == 0) {
                throw BaseException(MessageConstant.CATEGORY_NOT_FOUND)
            }
            categoryRepository.findCategoryById(id)
        }
    }

    /**
     * 根据类型查询分类
     * @param type 分类类型
     * @return List<Category> 分类列表
     */
    fun findCategoryByType(type: Int): List<Category> {
        return categoryRepository.findCategoriesByType(type)
    }
}
