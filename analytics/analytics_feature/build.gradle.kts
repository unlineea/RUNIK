plugins {
    alias(libs.plugins.runik.android.dynamic.feature)
}
android {
    namespace = "com.example.analytics.analytics_feature"
}

dependencies {
    implementation(project(":app"))

    implementation(libs.androidx.navigation.compose)

    api(projects.analytics.presentation)
    implementation(projects.analytics.domain)
    implementation(projects.analytics.data)
    implementation(projects.core.database)
}