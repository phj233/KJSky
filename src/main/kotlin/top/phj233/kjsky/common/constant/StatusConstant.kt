package top.phj233.kjsky.common.constant

/**
 * 状态常量，启用或者禁用
 */
object StatusConstant {
    //启用
    const val ENABLE: Int = 1

    //禁用
    const val DISABLE: Int = 0

    //店铺状态
    const val KEY: String = "SHOP_STATUS"

    /**
     * 订单状态 1待付款 2待接单 3已接单 4派送中 5已完成 6已取消
     */
    const val PENDING_PAYMENT: Int = 1
    const val TO_BE_CONFIRMED: Int = 2
    const val CONFIRMED: Int = 3
    const val DELIVERY_IN_PROGRESS: Int = 4
    const val COMPLETED: Int = 5
    const val CANCELLED: Int = 6

    /**
     * 支付状态 0未支付 1已支付 2退款
     */
    const val UN_PAID: Int = 0
    const val PAID: Int = 1
    const val REFUND: Int = 2
}
