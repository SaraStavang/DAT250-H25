plugins {
    java
    id("org.springframework.boot") version "3.5.5"
    id("io.spring.dependency-management") version "1.1.7"
}

repositories { mavenCentral() }

dependencies {
    implementation("jakarta.persistence:jakarta.persistence-api:3.2.0")
    implementation("org.hibernate.orm:hibernate-core:7.1.1.Final")
    runtimeOnly("com.h2database:h2:2.3.232")

    implementation("org.springframework.boot:spring-boot-starter-web")

    // Keep ONLY this for tests (Boot brings a consistent JUnit Jupiter + Platform)
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // REMOVE any explicit JUnit lines like:
    // testImplementation("org.junit.jupiter:junit-jupiter-api:...")
    // testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:...")
    // testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
