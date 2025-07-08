package top.phj233.kjsky.service

import cn.dev33.satoken.stp.StpUtil
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import top.phj233.kjsky.common.constant.StatusConstant
import top.phj233.kjsky.config.KJSkyProperties
import java.util.concurrent.TimeUnit

/**
 * 商店服务
 * @author phj233
 * @since 2025/7/5 11:49
 * @version
 */
@Service
class ShopService(
    val stringRedisTemplate: StringRedisTemplate,
    val kjskyProperties: KJSkyProperties) {
    val logger:Logger = LoggerFactory.getLogger(this::class.java)
    /**
     * 设置商店状态
     * @param status 商店状态
     */
    fun setStatus(status: Int): Boolean {
        logger.info("${StpUtil.getLoginIdAsLong()} 设置商店状态为: $status")
        stringRedisTemplate.opsForValue()
            .set(StatusConstant.SHOP_STATUS_KEY,
                status.toString(),999, TimeUnit.DAYS)
        logger.info("商店状态已设置为: $status")
        return true
    }

    fun getStatus(): Int {
        logger.info("${StpUtil.getLoginIdAsLong()} 获取商店状态")
        return stringRedisTemplate.opsForValue()
            .get(StatusConstant.SHOP_STATUS_KEY).let {
                it?.toInt()
            } ?: StatusConstant.DISABLE

    }

    fun getMerchantInfo(): KJSkyProperties.ShopProperties {
        kjskyProperties.shop.let {
            logger.info("${StpUtil.getLoginIdAsLong()} 获取商户信息: $it")
            return it
        }
    }

}
