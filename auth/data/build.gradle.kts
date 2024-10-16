plugins {
    alias(libs.plugins.runik.android.library)
    alias(libs.plugins.runik.jvm.ktor)
}

android {
    namespace = "com.example.auth.data"
}

dependencies {

    // koin
    implementation(libs.bundles.koin)

    // module relations
    implementation(projects.core.data)
    implementation(projects.core.domain)
    implementation(projects.auth.domain)
}