package top.phj233.kjsky.controller.user

import cn.dev33.satoken.stp.StpUtil
import org.springframework.data.domain.Page
import org.springframework.web.bind.annotation.*
import top.phj233.kjsky.common.ApiResponse
import top.phj233.kjsky.common.ResponseUtil
import top.phj233.kjsky.common.constant.MessageConstant
import top.phj233.kjsky.model.dto.*
import top.phj233.kjsky.service.OrderService

/**
 * 用户端/订单控制器
 * @author phj233
 * @since 2025/7/5 10:05
 * @version
 */
@RestController("userOrderController")
@RequestMapping("/user/order")
class OrderController(val orderService: OrderService) {
    /**
     * 用户下单
     * @param orderSubmitDTO
     * @return ApiResponse<OrdersSubmitVO>
     */
    @PostMapping("/submit")
    fun submit(@RequestBody orderSubmitDTO: OrdersSubmitDTO): ApiResponse<OrderSubmitVO> {
        return ResponseUtil.success(orderService.submitOrder(orderSubmitDTO))
    }

    /**
     * 订单支付
     * @param ordersPaymentDTO 订单支付DTO
     * @return ApiResponse<OrderPaymentVO>
     */
    @PutMapping("/payment")
    fun payment(@RequestBody ordersPaymentDTO: OrdersPaymentDTO): ApiResponse<OrderPaymentVO> {
        return ResponseUtil.success(orderService.paymentOrder(ordersPaymentDTO))
    }

    /**
     * 历史订单查询
     * @param page 页码
     * @param pageSize 页大小
     * @param status 订单状态
     * @return ApiResponse<Any>
     */
    @GetMapping("/historyOrders")
    fun historyOrders(
        @RequestParam page: Int,
        @RequestParam pageSize: Int,
        @RequestParam status: Int? = null
    ): ApiResponse<Page<OrderVO>> {
        return ResponseUtil.success(orderService.pageQuery(OrdersPageQueryDTO(
            userId = StpUtil.getLoginIdAsLong(),
            page = page,
            pageSize = pageSize,
        )))
    }


    /**
     * 查询订单详情
     * @param id 订单ID
     * @return ApiResponse<OrderVO>
     */
    @GetMapping("/orderDetail/{id}")
    fun orderDetail(@PathVariable id: Long): ApiResponse<OrderVO> {
        return ResponseUtil.success(orderService.findOrderDetailById(id))
    }


    /**
     * 用户取消订单
     * @return ApiResponse<String>
     */
    @PutMapping("/cancel/{id}")
    fun cancelOrder(@PathVariable id: Long): ApiResponse<String> {
        orderService.cancelOrder(id)
        return ResponseUtil.success(MessageConstant.SUCCESS)
    }

    /**
     * 再来一单
     * @param id 订单ID
     * @return ApiResponse<String>
     */
    @PostMapping("/repetition/{id}")
    fun repetitionOrder(@PathVariable id: Long): ApiResponse<String> {
        orderService.repeatOrder(id)
        return ResponseUtil.success(MessageConstant.SUCCESS)
    }

    /**
     * 客户催单
     * @param id 订单ID
     * @return ApiResponse<String>
     */
    @GetMapping("/reminder/{id}")
    fun reminderOrder(@PathVariable id: Long): ApiResponse<String> {
        orderService.urgeOrder(id)
        return ResponseUtil.success(MessageConstant.SUCCESS)
    }
}
