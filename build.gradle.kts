plugins {
    id("java")
    id("org.springframework.boot") version "3.3.1"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.springframework.boot:spring-boot-starter-websocket:3.3.1")
}

java{
    version = 19
}

tasks.test {
    useJUnitPlatform()
}