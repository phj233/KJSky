package top.phj233.kjsky.controller.admin

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.CacheEvict
import org.springframework.data.domain.Page
import org.springframework.web.bind.annotation.*
import top.phj233.kjsky.common.ApiResponse
import top.phj233.kjsky.common.ResponseUtil
import top.phj233.kjsky.common.constant.MessageConstant
import top.phj233.kjsky.model.Setmeal
import top.phj233.kjsky.module.dto.SetmealDTO
import top.phj233.kjsky.module.dto.SetmealPageQueryDTO
import top.phj233.kjsky.module.dto.SetmealVO
import top.phj233.kjsky.service.SetmealService

/**
 * 套餐控制器
 * @author phj233
 * @since 2025/7/2 10:25
 * @version
 */
@RestController
@RequestMapping("/admin/setmeal")
class SetmealController(val setmealService: SetmealService) {
    val logger: Logger = LoggerFactory.getLogger(this::class.java)

    /**
     * 新增套餐
     * @param setmealDTO 套餐DTO
     * @return ApiResponse<Setmeal> 新增的套餐视图对象
     */
    @PostMapping
    @CacheEvict(cacheNames = ["setmealCache"], key = "#setmealDTO.categoryId") //key: setmealCache::100
    fun save( setmealDTO: SetmealDTO): ApiResponse<Setmeal> {
        logger.info("新增套餐: $setmealDTO")
        return ResponseUtil.success(setmealService.saveSetmeal(setmealDTO))
    }

    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO 套餐分页查询DTO
     * @return ApiResponse<Page<Setmeal>> 分页查询结果
     */
    @GetMapping("/page")
    fun page(setmealPageQueryDTO: SetmealPageQueryDTO): ApiResponse<Page<Setmeal>> {
        logger.info("套餐分页查询: $setmealPageQueryDTO")
        return ResponseUtil.success(setmealService.pageQuery(setmealPageQueryDTO))
    }

    /**
     * 批量删除套餐
     * @param ids 套餐ID列表
     * @return ApiResponse<String> 删除结果
     */
    @DeleteMapping
    @CacheEvict(cacheNames = ["setmealCache"], allEntries = true)
    fun delete(ids: List<Long>): ApiResponse<String> {
        logger.info("批量删除套餐，IDs: $ids")
        setmealService.deleteSetmeals(ids)
        return ResponseUtil.success(MessageConstant.SUCCESS)
    }

    /**
     * 根据ID查询套餐及其菜品
     * @param id 套餐ID
     * @return ApiResponse<SetmealVO> 套餐视图对象
     */
    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ApiResponse<SetmealVO> {
        logger.info("根据ID查询套餐，ID: $id")
        return ResponseUtil.success(setmealService.findSetmealById(id))
    }

    /**
     * 更新套餐
     * @param setmealDTO 套餐DTO
     * @return ApiResponse<Setmeal> 更新后的套餐视图对象
     */
    @PutMapping
    @CacheEvict(cacheNames = ["setmealCache"], allEntries = true)
    fun update(@RequestBody setmealDTO: SetmealDTO): ApiResponse<Setmeal> {
        logger.info("更新套餐: $setmealDTO")
        return ResponseUtil.success(setmealService.updateSetmeal(setmealDTO))
    }

    /**
     * 套餐启售/停售
     * @param id 套餐ID
     * @param status 套餐状态（0：停售，1：启售）
     * @return ApiResponse<SetmealVO> 更新后的套餐视图对象
     */
    @PostMapping("/status/{status}")
    fun updateStatus(@PathVariable status: Int, id: Long): ApiResponse<SetmealVO> {
        logger.info("更新套餐状态，ID: $id, 状态: $status")
        val setmeal = setmealService.updateSetmealStatus(id, status)
        return ResponseUtil.success(setmeal)
    }
}
