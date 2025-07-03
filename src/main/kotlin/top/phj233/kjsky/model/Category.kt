package top.phj233.kjsky.model

import org.babyfish.jimmer.sql.*

/**
 * 菜品及套餐分类
 */
@Entity
interface Category : ModelBase{

    /**
     * 主键
     */
    @Id
    @GeneratedValue(
        strategy = GenerationType.IDENTITY
    )
    val id: Long

    /**
     * 类型   1 菜品分类 2 套餐分类
     */
    val type: Int?

    /**
     * 分类名称
     */
    @Key
    val name: String

    /**
     * 顺序
     */
    val sort: Int

    /**
     * 分类状态 0:禁用，1:启用
     */
    val status: Int?
}

