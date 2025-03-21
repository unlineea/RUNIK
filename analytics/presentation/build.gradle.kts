plugins {
    alias(libs.plugins.runik.android.feature.ui)
}

android {
    namespace = "com.example.analytics.presentation"
}

dependencies {
    implementation(projects.analytics.domain)
}