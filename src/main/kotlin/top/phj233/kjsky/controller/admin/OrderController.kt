package top.phj233.kjsky.controller.admin

import org.springframework.data.domain.Page
import org.springframework.web.bind.annotation.*
import top.phj233.kjsky.common.ApiResponse
import top.phj233.kjsky.common.ResponseUtil
import top.phj233.kjsky.common.constant.MessageConstant
import top.phj233.kjsky.model.dto.*
import top.phj233.kjsky.service.OrderService

/**
 * 订单控制器
 * @author phj233
 * @since 2025/7/2 10:58
 * @version
 */
@RestController
@RequestMapping("/admin/order")
class OrderController(private val orderService: OrderService, service: OrderService) {

    /**
     * 订单搜索
     * @param ordersPageQueryDTO 订单分页查询DTO
     * @return ApiResponse<Page<OrderVO>> 分页查询结果
     */
    @GetMapping("/conditionSearch")
    fun conditionSearch(ordersPageQueryDTO: OrdersPageQueryDTO): ApiResponse<Page<OrderVO>> {
        return ResponseUtil.success(orderService.pageQuery(ordersPageQueryDTO))
    }

    /**
     * 获取各状态订单数量统计
     * @return ApiResponse<OrderStatisticsVO> 订单状态统计结果
     */
    @GetMapping("/statistics")
    fun statistics(): ApiResponse<OrderStatisticsVO> {
        return ResponseUtil.success(orderService.countOrderStatus())
    }

    /**
     * 查询订单详情
     * @param id 订单ID
     */
    @GetMapping("/details/{id}")
    fun details(@PathVariable id: Long): ApiResponse<OrderVO> {
        return ResponseUtil.success(orderService.findOrderDetailById(id))
    }

    /**
     * 接单
     * @param orderConfirmDTO 订单确认DTO
     * @return ApiResponse<String> 接单结果
     */
    @PutMapping("/confirm")
    fun confirm(orderConfirmDTO: OrdersConfirmDTO): ApiResponse<String> {
        orderService.confirmOrder(orderConfirmDTO)
        return ResponseUtil.success(MessageConstant.SUCCESS)
    }

    /**
     * 拒单
     * @param orderRejectDTO 订单拒绝DTO
     * @return ApiResponse<String> 拒单结果
     */
    @PutMapping("/rejection")
    fun reject(orderRejectDTO: OrdersRejectionDTO): ApiResponse<String> {
        orderService.rejectOrder(orderRejectDTO)
        return ResponseUtil.success(MessageConstant.SUCCESS)
    }

    /**
     * 取消订单
     * @param orderCancelDTO 订单取消DTO
     * @return ApiResponse<String> 取消订单结果
     */
    @PutMapping("/cancel")
    fun cancel(orderCancelDTO: OrdersCancelDTO): ApiResponse<String> {
        orderService.cancelOrder(orderCancelDTO)
        return ResponseUtil.success(MessageConstant.SUCCESS)
    }

    /**
     * 派送订单
     * @param id 订单ID
     * @return ApiResponse<String> 派送结果
     */
    @PutMapping("/delivery/{id}")
    fun delivery(@PathVariable id: Long): ApiResponse<String> {
        orderService.deliveryOrder(id)
        return ResponseUtil.success(MessageConstant.SUCCESS)
    }

    /**
     * 完成订单
     * @param id 订单ID
     * @return ApiResponse<String> 完成订单结果
     */
    @PutMapping("/complete/{id}")
    fun complete(@PathVariable id: Long): ApiResponse<String> {
        orderService.completeOrder(id)
        return ResponseUtil.success(MessageConstant.SUCCESS)
    }

}
