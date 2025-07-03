package top.phj233.kjsky.repository

import org.babyfish.jimmer.spring.repository.KRepository
import top.phj233.kjsky.model.ShoppingCart

/**
 * 购物车数据仓库接口
 * @author phj233
 * @since 2025/6/30 16:47
 * @version
 */
interface ShoppingCartRepository: KRepository<ShoppingCart, Long> {
    fun findByUserId(userId: Long): List<ShoppingCart>
    fun deleteAllByUserId(userId: Long)
}
