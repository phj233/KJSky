package top.phj233.kjsky.model

import org.babyfish.jimmer.sql.*
import java.math.BigDecimal
import java.time.LocalDateTime

/**
 * 订单表
 */
@Entity
@Table(name = "orders")
interface Order {

    /**
     * 主键
     */
    @Id
    @GeneratedValue(
        strategy = GenerationType.IDENTITY
    )
    val id: Long

    /**
     * 订单号
     */
    @Key
    val number: String?

    /**
     * 订单状态 1待付款 2待接单 3已接单 4派送中 5已完成 6已取消 7退款
     */
    val status: Int

    /**
     * 下单用户
     */
    @IdView
    val userId: Long

    /**
     * 地址id
     */
    @IdView
    val addressBookId: Long

    /**
     * 下单时间
     */
    val orderTime: LocalDateTime

    /**
     * 结账时间
     */
    val checkoutTime: LocalDateTime?

    /**
     * 支付方式 1微信,2支付宝
     */
    val payMethod: Int

    /**
     * 支付状态 0未支付 1已支付 2退款
     */
    val payStatus: Int

    /**
     * 实收金额
     */
    val amount: BigDecimal?

    /**
     * 备注
     */
    val remark: String?

    /**
     * 手机号
     */
    val phone: String?

    /**
     * 地址
     */
    val address: String?

    /**
     * 用户名称
     */
    val userName: String?

    /**
     * 收货人
     */
    val consignee: String?

    /**
     * 订单取消原因
     */
    val cancelReason: String?

    /**
     * 订单拒绝原因
     */
    val rejectionReason: String?

    /**
     * 订单取消时间
     */
    val cancelTime: LocalDateTime?

    /**
     * 预计送达时间
     */
    val estimatedDeliveryTime: LocalDateTime?

    /**
     * 配送状态  1立即送出  0选择具体时间
     */
    val deliveryStatus: Short

    /**
     * 送达时间
     */
    val deliveryTime: LocalDateTime?

    /**
     * 打包费
     */
    val packAmount: Int?

    /**
     * 餐具数量
     */
    val tablewareNumber: Int?

    /**
     * 餐具数量状态  1按餐量提供  0选择具体数量
     */
    val tablewareStatus: Short

    /**
     * 用户
     */
    @ManyToOne
    @OnDissociate(DissociateAction.SET_NULL)
    val user: User

    /**
     * 地址簿
     */
    @ManyToOne
    @OnDissociate(DissociateAction.DELETE)
    val addressBook: AddressBook

    @OneToMany(mappedBy = "order")
    val orderDetail: List<OrderDetail>
}

