package top.phj233.kjsky.service

import cn.dev33.satoken.stp.StpUtil
import org.babyfish.jimmer.sql.ast.mutation.SaveMode
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import top.phj233.kjsky.model.ShoppingCart
import top.phj233.kjsky.model.copy
import top.phj233.kjsky.model.dto.ShoppingCartDTO
import top.phj233.kjsky.repository.DishRepository
import top.phj233.kjsky.repository.SetmealRepository
import top.phj233.kjsky.repository.ShoppingCartRepository

/**
 * 购物车服务
 * @author phj233
 * @since 2025/7/5 12:39
 * @version
 */
@Service
class ShoppingCartService(
    val shoppingCartRepository: ShoppingCartRepository,
    val dishRepository: DishRepository,
    val setmealRepository: SetmealRepository) {

    val logger: Logger = LoggerFactory.getLogger(ShoppingCartService::class.java)
    /**
     *  添加购物车
     *  @param shoppingCartDTO
     */
    fun addShoppingCart(shoppingCartDTO: ShoppingCartDTO): Boolean {
        logger.info("${StpUtil.getLoginIdAsLong()} 添加购物车: $shoppingCartDTO")
        shoppingCartRepository.findShoppingCartByUserIdOrDishIdOrSetmealIdOrDishFlavor(
            StpUtil.getLoginIdAsLong(),
            shoppingCartDTO.dishId,
            shoppingCartDTO.setmealId,
            shoppingCartDTO.dishFlavor
        ).let { existingCart ->
            logger.info("查询到的购物车: $existingCart")
            if (existingCart != null) { // 如果购物车已存在，则增加数量
                shoppingCartRepository.save(
                    existingCart[0].copy {
                        number += 1
                    }, SaveMode.UPDATE_ONLY)
            } else { // 如果购物车不存在，则创建新的购物车项
                shoppingCartRepository.save(
                    ShoppingCart {
                        if (shoppingCartDTO.dishId != null) {
                            val dish = dishRepository.findDishById(shoppingCartDTO.dishId)
                            this.image = dish.image
                            this.name = dish.name
                            this.amount = dish.price
                            this.dishId = shoppingCartDTO.dishId
                            this.dishFlavor = shoppingCartDTO.dishFlavor
                        } else if (shoppingCartDTO.setmealId != null) {
                            val setmeal = setmealRepository.findSetmealById(shoppingCartDTO.setmealId)
                            this.image = setmeal.image
                            this.name = setmeal.name
                            this.amount = setmeal.price
                            this.setmealId = shoppingCartDTO.setmealId
                        }
                        this.number = 1
                    })
            }
        }
        return true
    }


    /**
     * 查看购物车
     * @return List<ShoppingCart> 购物车列表
     */
    fun findShoppingCart(): List<ShoppingCart> {
        logger.info("${StpUtil.getLoginIdAsLong()} 查看购物车")
        return shoppingCartRepository.findByUserId(StpUtil.getLoginIdAsLong())
    }

    /**
     * 清空购物车
     */
    fun clearShoppingCart() {
        logger.info("${StpUtil.getLoginIdAsLong()} 清空购物车")
        shoppingCartRepository.deleteByUserId(StpUtil.getLoginIdAsLong())
    }

    /**
     * 删除购物车中一个商品
     * @param shoppingCartDTO 购物车DTO
     */
    fun deleteShoppingCartItem(shoppingCartDTO: ShoppingCartDTO): Boolean {
        logger.info("${StpUtil.getLoginIdAsLong()} 删除购物车项: $shoppingCartDTO")
        shoppingCartRepository.findShoppingCartByUserIdOrDishIdOrSetmealIdOrDishFlavor(
            StpUtil.getLoginIdAsLong(),
            shoppingCartDTO.dishId,
            shoppingCartDTO.setmealId,
            shoppingCartDTO.dishFlavor
        ).let { existingCart ->
            if (existingCart != null && existingCart.isNotEmpty()) {
                if (existingCart[0].number > 1) { // 如果数量大于1，则减少数量
                    shoppingCartRepository.save(
                        existingCart[0].copy {
                            number -= 1
                        }, SaveMode.UPDATE_ONLY
                    )
                } else { // 如果数量为1，则删除该购物车项
                    shoppingCartRepository.deleteById(existingCart[0].id)
                }
            }
        }
        return true
    }
}
