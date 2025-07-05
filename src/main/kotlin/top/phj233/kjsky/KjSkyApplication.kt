package top.phj233.kjsky

import org.babyfish.jimmer.spring.repository.EnableJimmerRepositories
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.scheduling.annotation.EnableScheduling

@EnableCaching //开发缓存注解功能
@EnableScheduling //开启任务调度
@SpringBootApplication
@EnableJimmerRepositories(basePackages = ["top.phj233.kjsky.repository"])
class KjSkyApplication

fun main(args: Array<String>) {
    runApplication<KjSkyApplication>(*args)
}
