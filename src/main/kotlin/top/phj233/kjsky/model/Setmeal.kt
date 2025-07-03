package top.phj233.kjsky.model

import org.babyfish.jimmer.sql.*
import java.math.BigDecimal

/**
 * 套餐
 */
@Entity
interface Setmeal : ModelBase {

    /**
     * 主键
     */
    @Id
    @GeneratedValue(
        strategy = GenerationType.IDENTITY
    )
    val id: Long

    /**
     * 菜品分类id
     */
    @IdView
    val categoryId: Long

    /**
     * 套餐名称
     */
    @Key
    val name: String

    /**
     * 套餐价格
     */
    val price: BigDecimal?

    /**
     * 售卖状态 0:停售 1:起售
     */
    val status: Int?

    /**
     * 描述信息
     */
    val description: String?

    /**
     * 图片
     */
    val image: String?

    /**
     * 套餐包含的菜品
     */
    @OneToMany(mappedBy = "setmeal")
    val setmealDishes: List<SetmealDish>

    /**
     * 套餐分类
     */
    @ManyToOne
    @OnDissociate(DissociateAction.SET_NULL)
    val category: Category
}

