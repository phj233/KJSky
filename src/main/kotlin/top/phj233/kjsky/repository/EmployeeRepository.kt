package top.phj233.kjsky.repository

import org.babyfish.jimmer.View
import org.babyfish.jimmer.spring.repository.DynamicParam
import org.babyfish.jimmer.spring.repository.KRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import top.phj233.kjsky.model.Employee
import kotlin.reflect.KClass

/**
 * 员工数据仓库接口
 * @author phj233
 * @since 2025/6/30 16:41
 * @version
 */
interface EmployeeRepository: KRepository<Employee, Long> {
    fun findEmployeeByUsername(username: String): Employee?
    fun findEmployeeById(id: Long): Employee
    fun <V: View<Employee>> findByNameLike(
        @DynamicParam name: String?, of: PageRequest, view: KClass<V>): Page<V>
}
