
plugins {
    id("java")
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    /* module 2.3 - reactive programming - RxJava */
    //guava
    implementation("com.google.guava:guava:30.1.1-jre")
    implementation("io.reactivex.rxjava3:rxjava:3.1.2")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}