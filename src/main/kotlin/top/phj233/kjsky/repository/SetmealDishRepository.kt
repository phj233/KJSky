package top.phj233.kjsky.repository

import org.babyfish.jimmer.View
import org.babyfish.jimmer.spring.repository.KRepository
import org.babyfish.jimmer.sql.kt.ast.expression.eq
import top.phj233.kjsky.model.SetmealDish
import top.phj233.kjsky.model.dishId
import top.phj233.kjsky.model.setmeal
import top.phj233.kjsky.model.status
import kotlin.reflect.KClass

/**
 * 套餐菜品数据库仓库接口
 * @author phj233
 * @since 2025/6/30 16:45
 * @version
 */
interface SetmealDishRepository: KRepository<SetmealDish, Long> {
    fun updateSetmealDishStatusByDishId(dishId: Long, status: Int): Int = sql.createUpdate(SetmealDish::class){
        set(table.setmeal.status, status)
        where(table.dishId eq dishId)
    }.execute()

    fun <V: View<SetmealDish>> findBySetmealId(setmealId: Long,view: KClass<V>): List<V>
    fun  findBySetmealId(setmealId: Long): List<SetmealDish>
}
