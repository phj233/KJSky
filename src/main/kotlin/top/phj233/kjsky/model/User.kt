package top.phj233.kjsky.model

import org.babyfish.jimmer.sql.*

/**
 * 用户信息
 */
@Entity
interface User : CreateTimeBase {

    /**
     * 主键
     */
    @Id
    @GeneratedValue(
        strategy = GenerationType.IDENTITY
    )
    val id: Long

    /**
     * 微信用户唯一标识
     */
    @Key
    val openid: String?

    /**
     * 姓名
     */
    val name: String?

    /**
     * 手机号
     */
    val phone: String?

    /**
     * 性别
     */
    val sex: String?

    /**
     * 身份证号
     */
    val idNumber: String?

    /**
     * 头像
     */
    val avatar: String?
}

