package top.phj233.kjsky.repository

import org.babyfish.jimmer.View
import org.babyfish.jimmer.spring.repository.DynamicParam
import org.babyfish.jimmer.spring.repository.KRepository
import org.babyfish.jimmer.sql.kt.ast.expression.eq
import org.babyfish.jimmer.sql.kt.ast.expression.valueIn
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import top.phj233.kjsky.model.Dish
import top.phj233.kjsky.model.id
import top.phj233.kjsky.model.status
import kotlin.reflect.KClass

/**
 * 菜品数据仓库接口
 * @author phj233
 * @since 2025/6/30 16:38
 * @version
 */
interface DishRepository: KRepository<Dish, Long> {
    fun <V: View<Dish>> findByNameLikeAndCategoryIdAndStatus(
        @DynamicParam name: String?,
        @DynamicParam categoryId: Long?,
        @DynamicParam status: Int?, of: PageRequest, view: KClass<V>): Page<V>
    fun <V: View<Dish>> findByNameLikeAndCategoryIdAndStatus(
        @DynamicParam name: String?,
        @DynamicParam categoryId: Long?,
        @DynamicParam status: Int?, view: KClass<V>): List<V>

    fun deleteDishesByIdsAndStatusIsFalse(@DynamicParam ids: List<Long>): Int = sql.createDelete(Dish::class){
        where(table.status eq 0)
        where(table.id valueIn ids)
        deleteByIds(ids)
    }.execute()
    fun findDishById(id: Long): Dish
    fun <V: View<Dish>> findByCategoryIdAndStatus(
        categoryId: Long,
        @DynamicParam status: Int, view: KClass<V>): List<V>

    fun countByStatus(status: Int): Long
}
