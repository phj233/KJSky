package top.phj233.kjsky.model

import org.babyfish.jimmer.sql.*

/**
 * 地址簿
 */
@Entity
interface AddressBook {

    /**
     * 主键
     */
    @Id
    @GeneratedValue(
        strategy = GenerationType.IDENTITY
    )
    val id: Long

    /**
     * 用户id
     */
    @IdView
    val userId: Long

    /**
     * 收货人
     */
    val consignee: String?

    /**
     * 性别
     */
    val sex: String?

    /**
     * 手机号
     */
    val phone: String

    /**
     * 省级区划编号
     */
    val provinceCode: String?

    /**
     * 省级名称
     */
    val provinceName: String?

    /**
     * 市级区划编号
     */
    val cityCode: String?

    /**
     * 市级名称
     */
    val cityName: String?

    /**
     * 区级区划编号
     */
    val districtCode: String?

    /**
     * 区级名称
     */
    val districtName: String?

    /**
     * 详细地址
     */
    val detail: String?

    /**
     * 标签
     */
    val label: String?

    /**
     * 默认 0 否 1是
     */
    val default: Boolean

    @ManyToOne
    @OnDissociate( DissociateAction.DELETE)
    val user: User
}

