package top.phj233.kjsky.repository

import org.babyfish.jimmer.spring.repository.DynamicParam
import org.babyfish.jimmer.spring.repository.KRepository
import org.babyfish.jimmer.sql.kt.ast.expression.eq
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Repository
import top.phj233.kjsky.model.Category
import top.phj233.kjsky.model.id
import top.phj233.kjsky.model.status

/**
 * 分类数据仓库接口
 * @author phj233
 * @since 2025/6/30 16:37
 * @version
 */
@Repository
interface CategoryRepository: KRepository<Category, Long> {
    fun findByNameLikeAndType(
        @DynamicParam name: String?,
        @DynamicParam type: Int?, of: PageRequest): Page<Category>
    fun findCategoryById(id: Long): Category
    fun findCategoriesByType(type: Int?): List<Category>
    fun updateCategoryStatusById(id: Long, status: Int): Int = sql.createUpdate(Category::class){
        where(table.id eq id)
        set(table.status, status)
    }.execute()
}
