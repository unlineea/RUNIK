plugins {
    alias(libs.plugins.runik.android.library)
    alias(libs.plugins.runik.jvm.ktor)
}

android {
    namespace = "com.example.core.data"
}

dependencies {
    implementation(libs.bundles.koin)

    implementation(libs.timber)
    implementation(projects.core.domain)
    implementation(projects.core.database)
}