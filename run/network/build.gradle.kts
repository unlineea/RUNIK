plugins {
    alias(libs.plugins.runik.android.library)
    alias(libs.plugins.runik.jvm.ktor)
}

android {
    namespace = "com.example.run.network"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.core.data)
}