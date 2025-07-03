package top.phj233.kjsky.service

import org.babyfish.jimmer.sql.ast.mutation.AssociatedSaveMode
import org.babyfish.jimmer.sql.ast.mutation.SaveMode
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import top.phj233.kjsky.common.constant.MessageConstant
import top.phj233.kjsky.common.constant.StatusConstant
import top.phj233.kjsky.common.exception.BaseException
import top.phj233.kjsky.common.exception.DeletionNotAllowedException
import top.phj233.kjsky.model.copy
import top.phj233.kjsky.module.dto.DishDTO
import top.phj233.kjsky.module.dto.DishPageQueryDTO
import top.phj233.kjsky.module.dto.DishQueryDTO
import top.phj233.kjsky.module.dto.DishVO
import top.phj233.kjsky.repository.DishRepository
import top.phj233.kjsky.repository.SetmealDishRepository

/**
 * 菜品服务类
 * @author phj233
 * @since 2025/6/30 16:16
 * @version
 */
@Service
class DishService(
    val dishRepository: DishRepository,
    val setmealDishRepository: SetmealDishRepository,
    val stringRedisTemplate: StringRedisTemplate
) {
    val log: Logger = LoggerFactory.getLogger(DishService::class.java)

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO 菜品分页查询DTO
     * @return Page<DishVO> 分页查询结果
     */
    fun pageQuery(dishPageQueryDTO: DishPageQueryDTO): Page<DishVO> {
        return dishRepository.findByNameLikeAndCategoryIdAndStatus(
            dishPageQueryDTO.name,
            dishPageQueryDTO.categoryId,
            dishPageQueryDTO.status,
            PageRequest.of(dishPageQueryDTO.page, dishPageQueryDTO.pageSize),
            DishVO::class
        )
    }

    /**
     * 新增菜品
     * @param dishDTO 菜品DTO
     * @return DishVO 新增的菜品视图对象
     */
    fun saveDish(dishDTO: DishDTO): DishVO {
        dishRepository.save(dishDTO, SaveMode.UPSERT, AssociatedSaveMode.APPEND).let {
            log.info("新增菜品: $it")
            cleanCache("dish_${it.categoryId}")
            return dishRepository.viewer(DishVO::class).findNullable(it.id)!!
        }
    }

    /**
     * 批量删除菜品
     * @param ids 菜品ID列表
     * @return Boolean 删除结果
     */
    fun deleteDishes(ids: List<Long>): Boolean {
        setmealDishRepository.findAllById(ids).takeIf { it.isNotEmpty() }?.let {
            log.warn("无法删除菜品，菜品ID: $ids 已被套餐使用")
            throw DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL)
        }
        dishRepository.deleteDishesByIdsAndStatusIsFalse(ids).takeIf { it > 0 }.let {
            log.info("批量删除菜品: $ids")
            cleanCache("dish_*")
            return true
        }
    }

    /**
     * 根据ID查询菜品
     * @param id 菜品ID
     */
    fun findDishById(id: Long): DishVO {
        return dishRepository.viewer(DishVO::class).findNullable(id)
            ?: throw BaseException(MessageConstant.DISH_NOT_FOUND)
    }


    /**
     * 清理菜品缓存
     * @param key 缓存的键
     */
    private fun cleanCache(key: String) {
        stringRedisTemplate.keys(key).takeIf { it.isNotEmpty() }?.let {
            stringRedisTemplate.delete(it)
        }
    }

    /**
     * 更新菜品信息
     * @param dishDTO 菜品DTO
     * @return DishVO 更新后的菜品视图对象
     */
    fun updateDish(dishDTO: DishDTO): DishVO {
        dishRepository.save(dishDTO, SaveMode.UPDATE_ONLY).let {
            log.info("更新菜品: $it")
            cleanCache("dish_${it.categoryId}")
            return dishRepository.viewer(DishVO::class).findNullable(it.id)!!
        }
    }

    /**
     * 更新菜品状态
     * @param id 菜品ID
     * @param status 新状态
     * @return DishVO 更新后的菜品视图对象
     */
    fun updateDishStatus(id: Long, status: Int): DishVO {
        dishRepository.findDishById(id).copy {
            this.status = status
        }.let {
            log.info("更新菜品状态: $it")
            if (status == StatusConstant.DISABLE) {
                setmealDishRepository.updateSetmealDishStatusByDishId(id, StatusConstant.DISABLE).let { setmeal ->
                    if (setmeal > 0) {
                        log.warn("菜品ID: $id 已被套餐使用，现已禁用")
                    }
                }
            }
            cleanCache("dish_${it.categoryId}")
            return dishRepository.viewer(DishVO::class).findNullable(
                dishRepository.save(it, SaveMode.UPDATE_ONLY).id
            )!!
        }
    }

    /**
     * 根据分类ID查询菜品列表
     * @param categoryId 分类ID
     * @return List<DishVO> 菜品视图对象列表
     */
    fun findDishesByCategoryId(categoryId: Long): List<DishVO> {
        return dishRepository.findByCategoryIdAndStatus(categoryId, StatusConstant.ENABLE, DishVO::class)
    }

    /**
     * 根据分类ID和状态 动态查询菜品列表
     * @param dishQueryDTO 菜品查询DTO
     * @return List<DishVO> 菜品视图对象列表
     */
    fun findDishesByCategoryIdAndStatus(dishQueryDTO : DishQueryDTO): List<DishVO> {
        return dishRepository.findByNameLikeAndCategoryIdAndStatus(
            dishQueryDTO.name,
            dishQueryDTO.categoryId,
            dishQueryDTO.status,
            DishVO::class
        )
    }
}

