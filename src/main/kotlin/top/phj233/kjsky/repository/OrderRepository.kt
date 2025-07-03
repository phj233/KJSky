package top.phj233.kjsky.repository

import org.babyfish.jimmer.View
import org.babyfish.jimmer.spring.repository.DynamicParam
import org.babyfish.jimmer.spring.repository.KRepository
import org.babyfish.jimmer.sql.kt.ast.expression.eq
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import top.phj233.kjsky.model.Order
import top.phj233.kjsky.model.id
import top.phj233.kjsky.model.status
import java.time.LocalDateTime
import kotlin.reflect.KClass

/**
 * 订单数据仓库接口
 * @author phj233
 * @since 2025/6/30 16:44
 * @version
 */
interface OrderRepository: KRepository<Order, Long> {
    fun findOrderByNumber(number: String): Order
    fun updateOrderStatus(id: Long, status: Int) = sql.createUpdate(Order::class) {
        where(table.id eq id)
        set(table.status, status)
    }.execute()

    fun <V: View<Order>> findByUserIdAndStatus(
        @DynamicParam userId: Long?,
        @DynamicParam status: Int?, pageable: Pageable, view: KClass<V>): Page<V>
    fun <V: View<Order>> findByUserIdOrStatusOrNumberOrPhoneOrOrderTimeAfterOrOrderTimeBefore(
        @DynamicParam userId: Long?,
        @DynamicParam status: Int?,
        @DynamicParam number: String?,
        @DynamicParam phone: String?,
        @DynamicParam orderTimeAfter: LocalDateTime?,
        @DynamicParam orderTimeBefore: LocalDateTime?,
        pageable: Pageable, view: KClass<V>
    ): Page<V>
    fun findOrderByNumberAndUserId(outTradeNo: String, loginIdAsLong: Long): Order
    fun findOrderById(orderId: Long): Order?
    fun countOrderByStatus(status: Int): Long
}
