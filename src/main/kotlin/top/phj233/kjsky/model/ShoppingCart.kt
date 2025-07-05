package top.phj233.kjsky.model

import org.babyfish.jimmer.sql.*
import java.math.BigDecimal

/**
 * 购物车
 */
@Entity
interface ShoppingCart : CreateTimeBase{

    /**
     * 主键
     */
    @Id
    @GeneratedValue(
        strategy = GenerationType.IDENTITY
    )
    val id: Long

    /**
     * 商品名称
     */
    val name: String?

    /**
     * 图片
     */
    val image: String?

    /**
     * 用户id
     */
    @IdView
    val userId: Long

    /**
     * 菜品id
     */
    @IdView
    val dishId: Long?

    /**
     * 套餐id
     */
    @IdView
    val setmealId: Long?

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
     * 用户
     */
    @ManyToOne
    @OnDissociate(DissociateAction.DELETE)
    val user: User

    /**
     * 菜品
     */
    @ManyToOne
    @OnDissociate(DissociateAction.SET_NULL)
    val dish: Dish?

    /**
     * 套餐
     */
    @ManyToOne
    @OnDissociate(DissociateAction.SET_NULL)
    val setmeal: Setmeal?

}

