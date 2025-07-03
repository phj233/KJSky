package top.phj233.kjsky.model

import org.babyfish.jimmer.sql.*

/**
 * 员工信息
 */
@Entity
interface Employee : ModelBase {

    /**
     * 主键
     */
    @Id
    @GeneratedValue(
        strategy = GenerationType.IDENTITY
    )
    val id: Long

    /**
     * 姓名
     */
    @Key
    val name: String

    /**
     * 用户名
     */
    @Key
    val username: String

    /**
     * 密码
     */
    val password: String

    /**
     * 手机号
     */
    val phone: String

    /**
     * 性别
     */
    val sex: String

    /**
     * 身份证号
     */
    val idNumber: String

    /**
     * 状态 0:禁用，1:启用
     */
    val status: Int
}

