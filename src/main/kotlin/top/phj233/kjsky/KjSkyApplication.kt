package top.phj233.kjsky

import org.babyfish.jimmer.spring.repository.EnableJimmerRepositories
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableJimmerRepositories(basePackages = ["top.phj233.kjsky.repository"])
class KjSkyApplication

fun main(args: Array<String>) {
    runApplication<KjSkyApplication>(*args)
}
