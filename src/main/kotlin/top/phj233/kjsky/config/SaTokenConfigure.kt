package top.phj233.kjsky.config

import cn.dev33.satoken.`fun`.SaFunction
import cn.dev33.satoken.interceptor.SaInterceptor
import cn.dev33.satoken.jwt.StpLogicJwtForSimple
import cn.dev33.satoken.router.SaRouter
import cn.dev33.satoken.stp.StpInterface
import cn.dev33.satoken.stp.StpLogic
import cn.dev33.satoken.stp.StpUtil
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

/**
 * Sa-Token 配置类
 * @author phj233
 * @since 2025/7/1 09:25
 * @version
 */
@Configuration
class SaTokenConfigure : WebMvcConfigurer{
    // 注册拦截器
    override fun addInterceptors(registry: InterceptorRegistry) {
        // 注册 Sa-Token 拦截器，校验规则为 StpUtil.checkLogin() 登录校验。
        registry.addInterceptor(SaInterceptor{
            SaRouter.match("/**").notMatch(
                "/admin/employee/login",
                "/openapi.html",
                "/openapi.yml",
                "/ts.zip")
                .check(SaFunction {
                    StpUtil.checkLogin()
                })
        })
    }

    @Bean
    fun getStpLogicJwt(): StpLogic {
        return StpLogicJwtForSimple()
    }

    @Bean
    fun corsFilter() = CorsFilter(UrlBasedCorsConfigurationSource().apply {
        registerCorsConfiguration("/**", CorsConfiguration().apply {
            allowCredentials = true // 允许携带 Cookie
            addAllowedOriginPattern("*") // 允许的请求来源
            addAllowedMethod("*") // 允许的请求方法类型
            addAllowedHeader("*") // 允许的请求头类型
        })
    })
}

@Component
class StpInterFaceImpl: StpInterface{
    val logger: Logger = LoggerFactory.getLogger(this::class.java)
    override fun getPermissionList(
        loginId: Any?,
        loginType: String?
    ): List<String?>? {
        TODO("Not yet implemented")
    }

    override fun getRoleList(
        loginId: Any?,
        loginType: String?
    ): List<String?>? {
        logger.info("""
            -----已进入 StpInterFaceImpl.getRoleList 方法-----
            loginId: $loginId - loginType: $loginType
             tokenInfo： ${StpUtil.getTokenInfo()}
            """.trimIndent())
        return listOf(StpUtil.getExtra("role").toString())
    }
}
