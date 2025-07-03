package top.phj233.kjsky.common.exception

/**
 * 账号被锁定异常
 */
class AccountLockedException : BaseException {
    constructor(msg: String?) : super(msg)
}
