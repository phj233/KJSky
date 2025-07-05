package top.phj233.kjsky.repository

import org.babyfish.jimmer.View
import org.babyfish.jimmer.spring.repository.DynamicParam
import org.babyfish.jimmer.spring.repository.KRepository
import org.babyfish.jimmer.spring.repository.fetchSpringPage
import org.babyfish.jimmer.sql.ast.LikeMode
import org.babyfish.jimmer.sql.kt.ast.expression.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import top.phj233.kjsky.common.constant.StatusConstant
import top.phj233.kjsky.model.*
import top.phj233.kjsky.model.dto.GoodsSalesDTO
import java.time.LocalDateTime
import kotlin.reflect.KClass

/**
 * 订单数据仓库接口
 * @author phj233
 * @since 2025/6/30 16:44
 * @version
 */
interface OrderRepository: KRepository<Order, Long> {
    fun findByNumber(number: String): Order
    fun updateOrderStatus(id: Long, status: Int) = sql.createUpdate(Order::class) {
        where(table.id eq id)
        set(table.status, status)
    }.execute()

    fun <V: View<Order>> findByUserIdAndStatus(
        @DynamicParam userId: Long?,
        @DynamicParam status: Int?, pageable: Pageable, view: KClass<V>): Page<V>
    fun <V: View<Order>> findByUserIdLikeOrStatusIsOrNumberLikeOrPhoneLikeOrOrderTimeBetween(
        @DynamicParam userId: Long?,
        @DynamicParam status: Int?,
        @DynamicParam number: String?,
        @DynamicParam phone: String?,
        @DynamicParam orderTimeAfter: LocalDateTime?,
        @DynamicParam orderTimeBefore: LocalDateTime?,
        pageable: Pageable, view: KClass<V>
    ): Page<V> = sql.createQuery(Order::class){
        where(userId?.let { table.userId eq it })
        where(status?.let { table.status eq it })
        where(number?.let { table.number.ilike(it, LikeMode.ANYWHERE) })
        where(phone?.let { table.phone.ilike(it, LikeMode.START) })
        where(orderTimeAfter?.let { table.orderTime ge it })
        where(orderTimeBefore?.let { table.orderTime le it })
        select(table.fetch(view))
    }.fetchSpringPage(pageable)

    // number和userId查询订单
    fun findOrderByNumberAndUserId(
        @DynamicParam number: String?,
        @DynamicParam userId: Long?
    ): Order = sql.createQuery(Order::class) {
        where(table.number eq number)
        where(table.userId eq userId)
        select(table)
    }.execute().first()
    fun countByStatusEquals(status: Int): Long
    fun countByOrderTimeBetween(beginTime: LocalDateTime, endTime: LocalDateTime): Long
    fun countByOrderTimeBetweenAndStatusIs(
        beginTime: LocalDateTime,
        endTime: LocalDateTime,
        status: Int
    ): Long = sql.createQuery(Order::class) {
        where(table.orderTime ge beginTime)
        where(table.orderTime le endTime)
        where(table.status eq status)
        select(count(table))
    }.fetchUnlimitedCount()
    fun sumAmountByOrderTimeBetween(
        beginTime: LocalDateTime,
        endTime: LocalDateTime
    ) : Double = sql.createQuery(Order::class) {
        where(table.orderTime ge beginTime)
        where(table.orderTime le endTime)
        select(sum(table.amount))
    }.execute().first()?.toDouble() ?: 0.0

    fun sumAmountByOrderTimeBetweenAndStatusIs(
        beginTime: LocalDateTime,
        endTime: LocalDateTime,
        status: Int
    ) : Double = sql.createQuery(Order::class) {
        where(table.orderTime ge beginTime)
        where(table.orderTime le endTime)
        where(table.status eq status)
        select(sum(table.amount))
    }.execute().first()?.toDouble() ?: 0.0
    fun countByStatus(status: Int) : Long
    fun getSalesTop10(
        @DynamicParam beginTime: LocalDateTime?,
        @DynamicParam endTime: LocalDateTime?
    ): List<GoodsSalesDTO> = sql.createQuery(Order::class){
        val name = subQuery(OrderDetail::class) {
            where(table.orderId eq parentTable.id)
            select(table.name)
        }
        val amount = subQuery(OrderDetail::class) {
            where(table.orderId eq parentTable.id)
            select(sum(table.amount))
        }
        where(table.status eq StatusConstant.COMPLETED)
        where(table.status eq StatusConstant.COMPLETED)
        if (beginTime != null) {
            where(table.orderTime ge beginTime)
        }
        if (endTime != null) {
            where(table.orderTime le endTime)
        }
        orderBy(amount.desc())
        select(name,amount)
    }.execute().map {
        GoodsSalesDTO(
            name = it[0] as String,
            number = (it[1] as Number).toInt()
        )
    }

}
