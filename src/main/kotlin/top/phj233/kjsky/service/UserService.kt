package top.phj233.kjsky.service

import cn.dev33.satoken.stp.StpUtil
import cn.dev33.satoken.stp.parameter.SaLoginParameter
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import top.phj233.kjsky.common.constant.JwtClaimsConstant
import top.phj233.kjsky.common.constant.MessageConstant
import top.phj233.kjsky.common.exception.BaseException
import top.phj233.kjsky.common.exception.LoginFailedException
import top.phj233.kjsky.config.KJSkyProperties
import top.phj233.kjsky.model.User
import top.phj233.kjsky.model.dto.UserLoginDTO
import top.phj233.kjsky.model.dto.UserLoginVO
import top.phj233.kjsky.repository.UserRepository

/**
 * 用户服务类
 * @author phj233
 * @since 2025/7/5 09:03
 * @version
 */
@Service
class UserService(
    private val kJSkyProperties: KJSkyProperties,
    val userRepository: UserRepository,
    val objectMapper: ObjectMapper
) {

    val logger: Logger = LoggerFactory.getLogger(this::class.java)
    /**
     * 微信登录
     * @param userLoginDTO 用户登录DTO，包含微信登录所需的code
     * @return UserLoginVO 用户登录视图对象，包含用户信息和token
     */
    fun wechatLogin(userLoginDTO: UserLoginDTO): UserLoginVO {
        logger.info("微信登录，参数：$userLoginDTO")
        val openid: String = getOpenId(userLoginDTO.code) ?: throw LoginFailedException(MessageConstant.LOGIN_FAILED)
         userRepository.findByOpenid(openid).let {
             if (it != null) {
                 //如果用户存在，则返回用户信息
                 logger.info("$openid 用户已存在，返回用户信息")
                 StpUtil.login(it.id, SaLoginParameter()
                     .setExtra(JwtClaimsConstant.USER_ID, it.id)
                     .setExtra(JwtClaimsConstant.ROLE, JwtClaimsConstant.USER))
                    return userRepository.viewer(UserLoginVO::class).findNullable(it.id)!!.copy(
                        token = StpUtil.getTokenValue()
                    )
             }
             //如果用户不存在，则创建新用户
             logger.info("$openid 对应的用户不存在，创建新用户")
             userRepository.save(User { this.openid = openid }).let { user ->
                 StpUtil.login(user.id, SaLoginParameter()
                        .setExtra(JwtClaimsConstant.USER_ID, user.id)
                        .setExtra(JwtClaimsConstant.ROLE, JwtClaimsConstant.USER))
                 return UserLoginVO(user, token = StpUtil.getTokenValue()).also {  userLoginVO ->
                     logger.info("新用户登录成功，用户信息：$userLoginVO")
                 }
             }
        }
    }

   fun getOpenId(code: String): String? {
       //调用微信接口服务，获得当前微信用户的openid
       val map = mutableMapOf<String, String>()
       map.put("appid", kJSkyProperties.weChat.appid.toString())
       map.put("secret", kJSkyProperties.weChat.secret.toString())
       map.put("js_code", code)
       map.put("grant_type", "authorization_code")
         logger.info("获取微信用户openid，参数：$map")
         val restTemplate = RestTemplate()
         val response = restTemplate.getForObject(
              "https://api.weixin.qq.com/sns/jscode2session?appid={appid}&secret={secret}&js_code={js_code}&grant_type={grant_type}",
              String::class.java, map
            )
       logger.info("获取微信用户openid，响应：$response")
       response.let {
           //解析响应，获取openid
           val jsonObject: JsonNode = objectMapper.readTree(it)
           val openid: String? = jsonObject.get("openid")?.asText()
           if (openid.isNullOrEmpty()) {
               throw BaseException("获取openid失败，请检查code是否正确")
           }
           logger.info("获取到的openid：$openid")
           return openid
       }
   }
}
