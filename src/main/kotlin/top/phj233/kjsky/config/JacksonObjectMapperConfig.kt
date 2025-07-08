package top.phj233.kjsky.config

import org.springframework.context.annotation.Configuration


/**
 * Jackson对象映射配置类 未生效。。
 * @author phj233
 * @since 2025/7/5 22:56
 * @version
 */
@Configuration
class JacksonObjectMapperConfig {
//    @Bean
//    fun redisSerializer(): RedisSerializer<Any?> {
//        val objectMapper: ObjectMapper = ObjectMapper()
//        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY)
//        // 必须设置，否则无法将JSON转化为对象，会转化成Map类型
//        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL)
//        // 自定义ObjectMapper的时间处理模块
//        val javaTimeModule = JavaTimeModule()
//        javaTimeModule.addSerializer<LocalDateTime?>(
//            LocalDateTime::class.java,
//            LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
//        )
//        javaTimeModule.addDeserializer<LocalDateTime?>(
//            LocalDateTime::class.java,
//            LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
//        )
//        javaTimeModule.addSerializer<LocalDate?>(
//            LocalDate::class.java,
//            LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
//        )
//        javaTimeModule.addDeserializer<LocalDate?>(
//            LocalDate::class.java,
//            LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
//        )
//        javaTimeModule.addSerializer<LocalTime?>(
//            LocalTime::class.java,
//            LocalTimeSerializer(DateTimeFormatter.ofPattern("HH:mm:ss"))
//        )
//        javaTimeModule.addDeserializer<LocalTime?>(
//            LocalTime::class.java,
//            LocalTimeDeserializer(DateTimeFormatter.ofPattern("HH:mm:ss"))
//        )
//        objectMapper.registerModule(javaTimeModule)
//        // 禁用将日期序列化为时间戳的行为
//        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
//        //创建JSON序列化器
//        return Jackson2JsonRedisSerializer<Any?>(objectMapper, Any::class.java)
//    }

//    @Bean
//    fun localDateTimeObjectMapper(): ObjectMapper {
//        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
//        val mapper = ObjectMapper()
//        mapper.registerModule(
//            JavaTimeModule().apply {
//                addDeserializer(LocalDateTime::class.java, LocalDateTimeDeserializer(formatter))
//                addSerializer(LocalDateTime::class.java, LocalDateTimeSerializer(formatter))
//            }
//        )
//        return mapper
//    }
}
