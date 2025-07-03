package top.phj233.kjsky.repository

import org.babyfish.jimmer.spring.repository.KRepository
import org.springframework.stereotype.Repository
import top.phj233.kjsky.model.AddressBook

/**
 * 地址簿数据仓库接口
 * @author phj233
 * @since 2025/6/30 16:37
 * @version
 */
@Repository
interface AddressBookRepository: KRepository<AddressBook, Long> {
}
