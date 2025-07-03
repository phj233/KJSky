package top.phj233.kjsky.model

import org.babyfish.jimmer.sql.*
import java.math.BigDecimal

/**
 * 订单明细表
 */
@Entity
interface OrderDetail {

    /**
     * 主键
     */
    @Id
    @GeneratedValue(
        strategy = GenerationType.IDENTITY
    )
    val id: Long

    /**
     * 名字
     */
    val name: String?

    /**
     * 图片
     */
    val image: String?

    /**
     * 订单id
     */
    @IdView("orders")
    val orderId: Long

    /**
     * 菜品id
     */
    @IdView
    val dishId: Long

    /**
     * 套餐id
     */
    @IdView
    val setmealId: Long

    /**
     * 口味
     */
    val dishFlavor: String?

    /**
     * 数量
     */
    val number: Int

    /**
     * 金额
     */
    val amount: BigDecimal?

    /**
     * 订单
     */
    @ManyToOne
    val orders: Order

    /**
     * 菜品
     */
    @ManyToOne
    val dish: Dish

    /**
     * 套餐
     */
    @ManyToOne
    val setmeal: Setmeal

}

