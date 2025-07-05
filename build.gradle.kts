plugins {
    kotlin("jvm") version "2.1.0"
    kotlin("plugin.spring") version "2.1.0"
    id("org.springframework.boot") version "3.5.3"
    id("io.spring.dependency-management") version "1.1.7"
    // ksp
    id("com.google.devtools.ksp") version "2.1.0-1.0.29"
}

group = "top.phj233"
version = "0.0.1-SNAPSHOT"

val jimmerVersion = "0.9.96"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    // maven tencent
    maven { url = uri("https://mirrors.cloud.tencent.com/nexus/repository/maven-public/") }
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    // jimmer
    implementation("org.babyfish.jimmer:jimmer-spring-boot-starter:${jimmerVersion}")
    implementation("org.springframework.boot:spring-boot-starter-websocket")
    ksp("org.babyfish.jimmer:jimmer-ksp:${jimmerVersion}")
    // druid
    implementation("com.alibaba:druid-spring-boot-starter:1.2.25")
    // sa-token
    implementation("cn.dev33:sa-token-spring-boot3-starter:1.44.0")
    // sa-token集成redis
    implementation("cn.dev33:sa-token-redis-template:1.44.0")
    implementation("org.apache.commons:commons-pool2")
    implementation("cn.dev33:sa-token-jwt:1.44.0")
    implementation ("cn.hutool:hutool-all:5.8.26")
    // WeChatPay
    implementation("com.github.wechatpay-apiv3:wechatpay-java:0.2.17")

    // Minio
    implementation("io.minio:minio:8.5.17")
    // pio
    implementation("org.apache.poi:poi:5.4.1")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    runtimeOnly("com.mysql:mysql-connector-j")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
