package top.phj233.kjsky.controller.user

import cn.dev33.satoken.annotation.SaIgnore
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import top.phj233.kjsky.common.ApiResponse
import top.phj233.kjsky.common.ResponseUtil
import top.phj233.kjsky.model.dto.UserLoginDTO
import top.phj233.kjsky.model.dto.UserLoginVO
import top.phj233.kjsky.service.UserService

/**
 * 用户端/用户控制器
 * @author phj233
 * @since 2025/7/5 08:54
 * @version
 */
@RestController("userUserController")
@RequestMapping("/user/user")
class UserController(val userService: UserService) {

    /**
     * 微信登录
     * @param userLoginDTO 用户登录DTO
     * @return
     */
    @PostMapping("/login")
    @SaIgnore
    fun login(@RequestBody userLoginDTO: UserLoginDTO): ApiResponse<UserLoginVO> {
        return ResponseUtil.success(userService.wechatLogin(userLoginDTO))
    }

}
