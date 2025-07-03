package top.phj233.kjsky.repository

import org.babyfish.jimmer.spring.repository.KRepository
import org.springframework.stereotype.Repository
import top.phj233.kjsky.model.DishFlavor

/**
 * 菜品口味数据仓库接口
 * @author phj233
 * @since 2025/6/30 16:38
 * @version
 */
@Repository
interface DishFlavorRepository: KRepository<DishFlavor, Long> {
}
