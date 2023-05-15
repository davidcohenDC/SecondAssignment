
plugins {
    id("java")
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

