package top.phj233.kjsky.repository

import org.babyfish.jimmer.spring.repository.KRepository
import top.phj233.kjsky.model.User

/**
 * 用户数据仓库接口
 * @author phj233
 * @since 2025/6/30 16:47
 * @version
 */
interface UserRepository: KRepository<User, Long> {
}
