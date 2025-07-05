package top.phj233.kjsky.repository

import org.babyfish.jimmer.spring.repository.DynamicParam
import org.babyfish.jimmer.spring.repository.KRepository
import org.babyfish.jimmer.sql.kt.ast.expression.eq
import org.springframework.stereotype.Repository
import top.phj233.kjsky.model.AddressBook
import top.phj233.kjsky.model.default
import top.phj233.kjsky.model.userId

/**
 * 地址簿数据仓库接口
 * @author phj233
 * @since 2025/6/30 16:37
 * @version
 */
@Repository
interface AddressBookRepository: KRepository<AddressBook, Long> {
    fun findAddressBooksByUserIdOrPhoneOrDefault(
        @DynamicParam userId: Long? = null,
        @DynamicParam phone: String? = null,
        @DynamicParam isDefault: Boolean? = null
    ): List<AddressBook>

    fun findAddressBookById(id: Long): AddressBook?
    fun updateAllToNotDefault(loginIdAsLong: Long) : Boolean = sql.createUpdate(AddressBook::class) {
        where(table.userId eq loginIdAsLong)
        set(table.default, false)
    }.execute() > 0


}
