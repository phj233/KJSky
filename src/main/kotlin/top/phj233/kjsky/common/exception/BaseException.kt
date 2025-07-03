package top.phj233.kjsky.common.exception

/**
 * 业务异常
 */
open class BaseException : RuntimeException {
    constructor()

    constructor(msg: String?) : super(msg)
}
