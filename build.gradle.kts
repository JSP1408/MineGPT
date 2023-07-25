plugins {
    id("java")
}

group = "com.e11eman"
version = "1.0-SNAPSHOT"


dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    compileOnly("io.papermc.paper:paper-api:1.20-R0.1-SNAPSHOT")
    implementation("com.theokanning.openai-gpt3-java:service:0.14.0")
}
tasks.test {
    useJUnitPlatform()
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

tasks.jar {
    manifest.attributes["Main-Class"] = "io.papermc.minegpt.MineGPT"
    val dependencies = configurations
            .runtimeClasspath
            .get()
            .map(::zipTree) // OR .map { zipTree(it) }
    from(dependencies)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}


