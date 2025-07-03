package top.phj233.kjsky.model

import org.babyfish.jimmer.sql.*
import java.math.BigDecimal

/**
 * 套餐菜品关系
 */
@Entity
interface SetmealDish {

    /**
     * 主键
     */
    @Id
    @GeneratedValue(
        strategy = GenerationType.IDENTITY
    )
    val id: Long

    /**
     * 套餐id
     */
    @IdView
    val setmealId: Long

    /**
     * 菜品id
     */
    @IdView
    val dishId: Long?

    /**
     * 菜品名称 （冗余字段）
     */
    val name: String?

    /**
     * 菜品单价（冗余字段）
     */
    val price: BigDecimal?

    /**
     * 菜品份数
     */
    val copies: Int?

    /**
     * 套餐
     */
    @ManyToOne
    @OnDissociate(DissociateAction.DELETE)
    val setmeal: Setmeal

    /**
     * 菜品
     */
    @ManyToOne
    @OnDissociate(DissociateAction.DELETE)
    val dish: Dish?

}

