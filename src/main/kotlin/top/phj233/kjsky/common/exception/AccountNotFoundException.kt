package top.phj233.kjsky.common.exception

/**
 * 账号不存在异常
 */
class AccountNotFoundException : BaseException {
    constructor(msg: String?) : super(msg)
}
