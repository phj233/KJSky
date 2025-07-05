package top.phj233.kjsky.service

import cn.dev33.satoken.stp.StpUtil
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import top.phj233.kjsky.common.constant.StatusConstant
import top.phj233.kjsky.model.dto.BusinessDataVO
import top.phj233.kjsky.model.dto.OrderOverViewVO
import top.phj233.kjsky.module.dto.DishOverViewVO
import top.phj233.kjsky.module.dto.SetmealOverViewVO
import top.phj233.kjsky.repository.DishRepository
import top.phj233.kjsky.repository.OrderRepository
import top.phj233.kjsky.repository.UserRepository
import java.time.LocalDateTime
import java.time.LocalTime

/**
 * 工作台服务
 * @author phj233
 * @since 2025/7/3 14:15
 * @version
 */
@Service
class WorkspaceService(
    val orderRepository: OrderRepository,
    val userRepository: UserRepository,
    val dishRepository: DishRepository,
    val setmealRepository: OrderRepository) {

    val logger: Logger = LoggerFactory.getLogger(this::class.java)
    /**
     * 根据时间段统计营业数据
     * @param beginTime 开始时间
     * @param endTime 结束时间
     * @return BusinessDataVO 营业数据视图对象
     */
    fun getBusinessData(beginTime: LocalDateTime, endTime: LocalDateTime): BusinessDataVO {
        logger.info("${StpUtil.getLoginIdAsLong()} 获取营业数据，开始时间：$beginTime，结束时间：$endTime")
        // 订单总数
        logger.info("查询订单总数")
        val orderCount = orderRepository.countByOrderTimeBetween(beginTime, endTime)
        // 有效订单数
        logger.info("查询有效订单数")
        val validOrderCount: Int = orderRepository.countByOrderTimeBetweenAndStatusIs(
            beginTime, endTime, StatusConstant.COMPLETED
        ).toInt()
        // 营业额
        logger.info("查询营业额")
        val turnover = orderRepository.sumAmountByOrderTimeBetween(beginTime, endTime)
        // 新增用户数
        logger.info("查询新增用户数")
        val newUserCount: Int = userRepository.countByCreateTimeBetween(beginTime, endTime)

        return BusinessDataVO(
            validOrderCount = validOrderCount,
            turnover = turnover,
            orderCompletionRate = validOrderCount.toDouble() / orderCount,
            unitPrice = turnover / validOrderCount,
            newUsers = newUserCount,
        )
    }

    /**
     * 查询订单状态数据
     * @return OrderOverViewVO 订单概览视图对象
     */
    fun getOrderStatusData(): OrderOverViewVO {
        logger.info("${StpUtil.getLoginIdAsLong()} 查询订单状态数据")
        val waitingOrders = orderRepository.countByOrderTimeBetweenAndStatusIs(
            LocalDateTime.now().with(LocalTime.MIN),
            LocalDateTime.now().with(LocalTime.MAX),
            StatusConstant.TO_BE_CONFIRMED)

        val completedOrders = orderRepository.countByStatusEquals(StatusConstant.COMPLETED)
        val deliveredOrders = orderRepository.countByStatusEquals(StatusConstant.CONFIRMED)
        val cancelledOrders = orderRepository.countByStatusEquals(StatusConstant.CANCELLED)
        val allOrders = orderRepository.count()

        return OrderOverViewVO(
            waitingOrders = waitingOrders.toInt(),
            completedOrders = completedOrders.toInt(),
            deliveredOrders = deliveredOrders.toInt(),
            cancelledOrders = cancelledOrders.toInt(),
            allOrders = allOrders.toInt()
        )
    }

    /**
     * 查询菜品状态数据
     * @return DishOverViewVO 菜品概览视图对象
     */
    fun getDishStatusData(): DishOverViewVO{
        logger.info("${StpUtil.getLoginIdAsLong()} 查询菜品状态数据")
        val sold = dishRepository.countByStatus(StatusConstant.ENABLE)
        val discontinued = dishRepository.countByStatus(StatusConstant.DISABLE)

        return DishOverViewVO(
            sold = sold.toInt(),
            discontinued = discontinued.toInt()
        )
    }

    /**
     * 查询套餐状态数据
     * @return SetmealOverViewVO 套餐概览视图对象
     */
    fun getSetmealStatusData(): SetmealOverViewVO {
        logger.info("${StpUtil.getLoginIdAsLong()} 查询套餐状态数据")
        val sold = setmealRepository.countByStatus(StatusConstant.ENABLE)
        val discontinued = setmealRepository.countByStatus(StatusConstant.DISABLE)
        return SetmealOverViewVO(
            sold = sold.toInt(),
            discontinued = discontinued.toInt()
        )
    }
}
