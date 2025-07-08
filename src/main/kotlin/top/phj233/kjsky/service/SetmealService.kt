package top.phj233.kjsky.service

import org.babyfish.jimmer.sql.ast.mutation.AssociatedSaveMode
import org.babyfish.jimmer.sql.ast.mutation.SaveMode
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import top.phj233.kjsky.common.constant.MessageConstant
import top.phj233.kjsky.common.constant.StatusConstant
import top.phj233.kjsky.common.exception.BaseException
import top.phj233.kjsky.common.exception.SetmealEnableFailedException
import top.phj233.kjsky.model.Setmeal
import top.phj233.kjsky.model.dto.DishItemVO
import top.phj233.kjsky.module.dto.SetmealDTO
import top.phj233.kjsky.module.dto.SetmealPageQueryDTO
import top.phj233.kjsky.module.dto.SetmealQueryDTO
import top.phj233.kjsky.module.dto.SetmealVO
import top.phj233.kjsky.repository.SetmealDishRepository
import top.phj233.kjsky.repository.SetmealRepository

/**
 * 套餐服务类
 * @author phj233
 * @since 2025/7/1 22:17
 * @version
 */
@Service
class SetmealService(
    val setmealRepository: SetmealRepository,
    val setmealDishRepository: SetmealDishRepository) {

    val logger : Logger = LoggerFactory.getLogger(this::class.java)
    /**
     * 新增套餐
     * @param setmealDTO 套餐DTO
     * @return Setmeal 新增的套餐对象
     */
    fun saveSetmeal(setmealDTO: SetmealDTO): Setmeal {
        return setmealRepository.save(setmealDTO, SaveMode.UPSERT, AssociatedSaveMode.APPEND)
    }

    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO 套餐分页查询DTO
     * @return Page<Setmeal> 分页查询结果
     */
    fun pageQuery(setmealPageQueryDTO: SetmealPageQueryDTO): Page<SetmealVO> {
        return setmealRepository.findByNameLikeAndCategoryIdAndStatus(
            setmealPageQueryDTO.name,
            setmealPageQueryDTO.categoryId,
            setmealPageQueryDTO.status,
            SetmealVO::class,
            PageRequest.of(setmealPageQueryDTO.page, setmealPageQueryDTO.pageSize,)
        )
    }

    /**
     * 批量删除套餐
     * @param ids 套餐ID列表
     * @return Boolean 是否删除成功
     */
    fun deleteSetmeals(ids: List<Long>): Boolean {
        return setmealRepository.deleteByIdsAndStatusIsFalse(ids) > 0
    }

    /**
     * 根据Id查询套餐及其菜品
     * @param id 套餐ID
     * @return SetmealVO 套餐视图对象
     */
    fun findSetmealById(id: Long): SetmealVO {
        return setmealRepository.viewer(SetmealVO::class).findNullable(id) ?: throw BaseException(MessageConstant.SETMEAL_NOT_FOUND)
    }

    /**
     * 更新套餐
     * @param setmealDTO 套餐DTO
     * @return Setmeal 更新后的套餐对象
     */
    fun updateSetmeal(setmealDTO: SetmealDTO): Setmeal {
        return setmealRepository.save(setmealDTO, SaveMode.UPSERT, AssociatedSaveMode.APPEND)
    }

    /**
     * 套餐启售/停售
     * @param id 套餐ID
     * @param status 套餐状态（0：停售，1：启售）
     * @return SetmealVO 更新后的套餐视图对象
     */
    fun updateSetmealStatus(id: Long, status: Int): SetmealVO {
        //status 1 时还要再判断 菜品 status 是否 1
        if (status == StatusConstant.ENABLE){
            setmealDishRepository.findBySetmealId(id, DishItemVO::class).forEach {
                logger.info("套餐菜品状态检查: ${it.status}")
                if (it.status != StatusConstant.ENABLE) {
                    throw SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED)
                }
            }
        }
        return setmealRepository.updateSetmealStatusById(id, status).let {
            if (it == 0) {
                throw BaseException(MessageConstant.SETMEAL_NOT_FOUND)
            }
            setmealRepository.viewer(SetmealVO::class).findNullable(id) ?: throw BaseException(MessageConstant.SETMEAL_NOT_FOUND)
        }
    }

    /**
     * 套餐条件查询
     * @param setmealQueryDTO 套餐查询DTO
     * @return List<SetmealVO> 套餐视图对象列表
     */
    fun querySetmeal(setmealQueryDTO: SetmealQueryDTO): List<SetmealVO> {
        return setmealRepository.findByCategoryIdAndStatus(
            setmealQueryDTO.categoryId,
            setmealQueryDTO.status,
            SetmealVO::class
        )
    }

    /**
     * 根据Id查询菜品选项
     * @param id 套餐ID
     * @return List<DishItemVO> 套餐菜品视图对象列表
     */
    fun findSetmealDishesById(id: Long): List<DishItemVO> {
        return setmealDishRepository.findBySetmealId(id, DishItemVO::class)
    }


}
