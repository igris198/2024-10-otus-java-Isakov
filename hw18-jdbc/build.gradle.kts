dependencies {
    implementation("ch.qos.logback:logback-classic")
    implementation("org.postgresql:postgresql")
    implementation("org.flywaydb:flyway-core")
    implementation("com.zaxxer:HikariCP")

    runtimeOnly("org.flywaydb:flyway-database-postgresql")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

