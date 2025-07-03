package top.phj233.kjsky.model

import org.babyfish.jimmer.sql.*
import java.math.BigDecimal

/**
 * 菜品
 */
@Entity
interface Dish : ModelBase{

    /**
     * 主键
     */
    @Id
    @GeneratedValue(
        strategy = GenerationType.IDENTITY
    )
    val id: Long

    /**
     * 菜品名称
     */
    @Key
    val name: String

    /**
     * 菜品分类id
     */
    @IdView
    val categoryId: Long

    /**
     * 菜品价格
     */
    val price: BigDecimal?

    /**
     * 图片
     */
    val image: String?

    /**
     * 描述信息
     */
    val description: String?

    /**
     * 0 停售 1 起售
     */
    val status: Int?

    /**
     * 菜品口味
     */
    @OneToMany(mappedBy = "dish")
    val flavors: List<DishFlavor>

    /**
     * 分类
     */
    @ManyToOne
    @OnDissociate(DissociateAction.SET_NULL)
    val category: Category

}

