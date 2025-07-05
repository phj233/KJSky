package top.phj233.kjsky.repository

import org.babyfish.jimmer.View
import org.babyfish.jimmer.spring.repository.DynamicParam
import org.babyfish.jimmer.spring.repository.KRepository
import org.babyfish.jimmer.sql.kt.ast.expression.eq
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import top.phj233.kjsky.model.Setmeal
import top.phj233.kjsky.model.id
import top.phj233.kjsky.model.status
import kotlin.reflect.KClass

/**
 * 套餐数据仓库接口
 * @author phj233
 * @since 2025/6/30 16:45
 * @version
 */
interface SetmealRepository: KRepository<Setmeal, Long> {
    fun <V: View<Setmeal>> findByNameLikeAndCategoryIdAndStatus(
        @DynamicParam name: String?,
        @DynamicParam categoryId: Long?,
        @DynamicParam status: Int?,view: KClass<V>, of: PageRequest): Page<V>

    fun <V: View<Setmeal>> findByNameLikeAndCategoryIdAndStatus(
        @DynamicParam name: String?,
        @DynamicParam categoryId: Long?,
        @DynamicParam status: Int?, view: KClass<V>): List<V>

    fun deleteByIdsAndStatusIsFalse(ids: List<Long>): Int = sql.createDelete(Setmeal::class) {
        where(table.status eq 0)
        deleteByIds(ids)
    }.execute()

    fun updateSetmealStatusById(id: Long, status: Int): Int = sql.createUpdate(Setmeal::class) {
        where(table.id eq id)
        set(table.status, status)
    }.execute()

    fun findSetmealById(setmealId: Long) : Setmeal


}
