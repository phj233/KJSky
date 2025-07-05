package top.phj233.kjsky.common.constant

/**
 * 信息提示常量类
 */
object MessageConstant {
    const val ADDRESS_NOT_FOUND: String = "地址不存在"
    const val ACCOUNT_DISABLED: String = "账号已被禁用"
    const val SETMEAL_NOT_FOUND: String = "套餐不存在"
    const val CATEGORY_NOT_FOUND: String = "分类不存在"
    const val SUCCESS: String = "操作成功"
    const val DISH_NOT_FOUND: String = "菜品不存在"
    const val LOGOUT_SUCCESS: String = "退出成功"
    const val PASSWORD_ERROR: String = "密码错误"
    const val ACCOUNT_NOT_FOUND: String = "账号不存在"
    const val ACCOUNT_LOCKED: String = "账号被锁定"
    const val ALREADY_EXISTS: String = "已存在"
    const val UNKNOWN_ERROR: String = "未知错误"
    const val USER_NOT_LOGIN: String = "用户未登录"
    const val CATEGORY_BE_RELATED_BY_SETMEAL: String = "当前分类关联了套餐,不能删除"
    const val CATEGORY_BE_RELATED_BY_DISH: String = "当前分类关联了菜品,不能删除"
    const val SHOPPING_CART_IS_NULL: String = "购物车数据为空，不能下单"
    const val ADDRESS_BOOK_IS_NULL: String = "用户地址为空，不能下单"
    const val LOGIN_FAILED: String = "登录失败"
    const val UPLOAD_FAILED: String = "文件上传失败"
    const val SETMEAL_ENABLE_FAILED: String = "套餐内包含未启售菜品，无法启售"
    const val PASSWORD_EDIT_FAILED: String = "密码修改失败"
    const val DISH_ON_SALE: String = "起售中的菜品不能删除"
    const val SETMEAL_ON_SALE: String = "起售中的套餐不能删除"
    const val DISH_BE_RELATED_BY_SETMEAL: String = "当前菜品关联了套餐,不能删除"
    const val ORDER_STATUS_ERROR: String = "订单状态错误"
    const val ORDER_NOT_FOUND: String = "订单不存在"
}
