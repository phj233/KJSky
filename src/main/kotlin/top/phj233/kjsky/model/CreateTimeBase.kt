package top.phj233.kjsky.model

import org.babyfish.jimmer.sql.MappedSuperclass
import java.time.LocalDateTime

/**
 * @author phj233
 * @since 2025/6/30 20:21
 * @version
 */
@MappedSuperclass
interface CreateTimeBase {
    val createTime: LocalDateTime?
}
