package top.phj233.kjsky.model

import org.babyfish.jimmer.sql.MappedSuperclass
import java.time.LocalDateTime

/**
 * 基础实体类接口，包含通用的字段
 * @author phj233
 * @since 2025/6/30 20:08
 * @version
 */
@MappedSuperclass
interface ModelBase {

    /**
     * 创建时间
     */
    val createTime: LocalDateTime?

    /**
     * 更新时间
     */
    val updateTime: LocalDateTime?

    /**
     * 创建人
     */
    val createUser: Long?

    /**
     * 修改人
     */
    val updateUser: Long?
}
