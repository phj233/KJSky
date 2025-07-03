package top.phj233.kjsky.model

import org.babyfish.jimmer.sql.*

/**
 * 菜品口味关系表
 */
@Entity
interface DishFlavor {

    /**
     * 主键
     */
    @Id
    @GeneratedValue(
        strategy = GenerationType.IDENTITY
    )
    val id: Long

    /**
     * 菜品
     */
    @IdView
    val dishId: Long

    /**
     * 口味名称
     */
    val name: String?

    /**
     * 口味数据list
     */
    val value: String?

    /**
     * 菜品
     */
    @ManyToOne
    @OnDissociate(DissociateAction.DELETE)
    val dish: Dish
}

