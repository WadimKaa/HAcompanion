import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}


val localProperties = Properties().apply {
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        load(FileInputStream(localPropertiesFile))
    }
}

android {

    val haBaseUrl: String = localProperties.getProperty("HA_BASE_URL") as String? ?: ""
    val haToken: String = localProperties.getProperty("HA_TOKEN") as String? ?: ""


    namespace = "com.powakaz.core_network"
    compileSdk = 36

    defaultConfig {
        minSdk = 24


        buildConfigField("String", "HA_TOKEN", "\"${haToken}\"")
        buildConfigField("String", "HA_BASE_URL", "\"${haBaseUrl}\"")

    }
    buildFeatures {
        buildConfig = true

    }
}

dependencies {
    implementation(libs.retrofit)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.retrofit.serialization)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)
    implementation(libs.coroutines.core)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
}