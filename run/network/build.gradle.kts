plugins {
    alias(libs.plugins.runik.android.library)
    alias(libs.plugins.runik.jvm.ktor)
}

android {
    namespace = "com.example.run.network"
}

dependencies {
    //koin
    implementation(libs.bundles.koin)

    implementation(projects.core.domain)
    implementation(projects.core.data)
}