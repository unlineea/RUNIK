plugins {
    alias(libs.plugins.runik.android.library)
    alias(libs.plugins.runik.android.room)
}

android {
    namespace = "com.example.core.database"
}

dependencies {
    implementation(libs.org.mongodb.bson)
    implementation(projects.core.domain)
}