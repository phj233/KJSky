package top.phj233.kjsky.controller.admin

import cn.dev33.satoken.annotation.SaCheckRole
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import top.phj233.kjsky.common.ApiResponse
import top.phj233.kjsky.common.ResponseUtil
import top.phj233.kjsky.model.dto.BusinessDataVO
import top.phj233.kjsky.model.dto.OrderOverViewVO
import top.phj233.kjsky.module.dto.DishOverViewVO
import top.phj233.kjsky.module.dto.SetmealOverViewVO
import top.phj233.kjsky.service.WorkspaceService
import java.time.LocalDateTime
import java.time.LocalTime

/**
 * 管理端/工作台控制器
 * @author phj233
 * @since 2025/7/2 10:56
 * @version
 */
@RestController
@RequestMapping("/admin/workspace")
@SaCheckRole("employee")
class WorkSpaceController(private val workspaceService: WorkspaceService) {

    /**
     * 工作台今日数据查询
     * @return ApiResponse<BusinessDataVO> 工作台业务数据视图对象
     */
    @GetMapping("/businessData")
    fun businessData(): ApiResponse<BusinessDataVO> {
        //获得当天的开始时间
        val begin = LocalDateTime.now().with(LocalTime.MIN)
        //获得当天的结束时间
        val end = LocalDateTime.now().with(LocalTime.MAX)
        return ResponseUtil.success(workspaceService.getBusinessData(begin, end))
    }

    /**
     * 查询订单管理数据
     * @return ApiResponse<OrderOverViewVO> 订单概览视图对象
     */
    @GetMapping("/overviewOrders")
    fun overviewOrders(): ApiResponse<OrderOverViewVO> {
        return ResponseUtil.success(workspaceService.getOrderStatusData())
    }

    /**
     * 查询菜品总览
     * @return ApiResponse<DishOverViewVO> 菜品概览视图对象
     */
    @GetMapping("/overviewDishes")
    fun overviewDishes(): ApiResponse<DishOverViewVO> {
        return ResponseUtil.success(workspaceService.getDishStatusData())
    }

    /**
     * 查询套餐总览
     * @return ApiResponse<SetmealOverViewVO> 套餐概览视图对象
     */
    @GetMapping("/overviewSetmeals")
    fun overviewSetmeals(): ApiResponse<SetmealOverViewVO> {
        return ResponseUtil.success(workspaceService.getSetmealStatusData())
    }
}
