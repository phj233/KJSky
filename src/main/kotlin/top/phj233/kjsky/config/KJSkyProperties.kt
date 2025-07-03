package top.phj233.kjsky.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

/**
 * KJSky配置文件
 * @author phj233
 * @since 2025/6/30 17:47
 * @version
 */
@Component
@ConfigurationProperties(prefix = "kjsky")
data class KJSkyProperties(
    var minio: MinioProperties = MinioProperties(),
    var weChat: WeChatProperties = WeChatProperties(),
    var shop: ShopProperties = ShopProperties(),
    var baidu: BaiduProperties = BaiduProperties()
) {
    /**
     * Minio配置
     * @author phj233
     * @since 2025/6/30 16:30
     * @version
     */
    data class MinioProperties(
        /**
         * minio地址
         */
        var endpoint: String? = "http://home.phj233.top:9005",
        var accessKey: String? = "bBohf4gCVGAOtyUKdrdr",
        var secretKey: String? = "5ygVgDZgfT2ccNBWESRNpgmeLhGZpV3klftR1MwH",
        /**
         * 存储桶名称
         */
        var bucketName: String? = "kjsky"
    )

    /**
     * 微信支付配置
     * @author phj233
     * @since 2025/6/30 17:47
     * @version
     */
    data class WeChatProperties(
        /**
         * 微信公众号/小程序的AppID
         */
        var appid: String? = "wxffb3637a228223b8",
        /**
         * 微信公众号/小程序的AppSecret
         */
        var secret: String? = "84311df9199ecacdf4f12d27b6b9522d",
        /**
         * 商户号
         */
        var mchid: String? = "1561414331",
        /**
         * 商户证书序列号
         */
        var mchSerialNo: String? = "4B3B3DC35414AD50B1B755BAF8DE9CC7CF407606",
        /**
         * 商户私钥文件路径
         */
        var privateKeyFilePath: String? = "D:\\pay\\apiclient_key.pem",

        /**
         * 商户公钥文件路径
         */
        var publicKeyFilePath: String? = "D:\\pay\\apiclient_cert.pem",
        /**
         * 商户公钥ID
         */
        var publicKeyId: String? = "04B3B3DC35414AD50B1B755BAF8DE9CC7CF407606",

        /**
         * API V3密钥
         */
        var apiV3Key: String? = "CZBK51236435wxpay435434323FFDuv3",
        /**
         * 微信支付证书文件路径
         */
        var weChatPayCertFilePath: String? = "D:\\pay\\wechatpay_166D96F876F45C7D07CE98952A96EC980368ACFC.pem",
        /**
         * 支付成功回调地址
         */
        var notifyUrl: String? = "https://58869fb.r2.cpolar.top/notify/paySuccess",
        /**
         * 退款成功回调地址
         */
        var refundNotifyUrl: String? = "https://58869fb.r2.cpolar.top/notify/refundSuccess"
    )

    /**
     * 店铺属性配置
     */
    data class ShopProperties(
        /**
         * 店铺名称
         */
        var name: String? = "KJSky",
        /**
         * 店铺地址
         */
        var address: String? = "中国浙江省杭州市余杭区",
        /**
         * 店铺电话
         */
        var phone: String? = "0571-12345678",
        /**
         * 店铺营业时间
         */
        var businessHours: String? = "09:00-21:00"
    )


    /**
     * 百度地图API配置
     */
    data class BaiduProperties(
        /**
         * 百度地图API密钥
         */
        var ak: String? = "your_baidu_map_ak",
    )
}

