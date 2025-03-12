plugins {
    alias(libs.plugins.runik.android.feature.ui)
}

android {
    namespace = "com.example.run.presentation"
}

dependencies {

    implementation(libs.timber)

    implementation(projects.core.domain)
//    implementation(projects.auth.domain)
    implementation(projects.run.domain)

    // OSMdroid main Dependency
    implementation (libs.osmdroid.android)
    implementation(libs.osm.android.compose)
}