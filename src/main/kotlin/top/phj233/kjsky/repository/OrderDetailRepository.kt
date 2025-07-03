package top.phj233.kjsky.repository

import org.babyfish.jimmer.spring.repository.KRepository
import top.phj233.kjsky.model.OrderDetail

/**
 * 订单详情数据仓库接口
 * @author phj233
 * @since 2025/6/30 16:43
 * @version
 */
interface OrderDetailRepository: KRepository<OrderDetail, Long> {
    fun findOrderDetailsByOrderId(orderId: Long): List<OrderDetail>?
}
