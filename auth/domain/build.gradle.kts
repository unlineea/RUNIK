plugins {
    alias(libs.plugins.runik.jvm.library)
}

dependencies {

    // module relations
    implementation(projects.core.domain)
}