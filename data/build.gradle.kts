plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(libs.koin.core)

    implementation(libs.commonmark)

    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.core)
}