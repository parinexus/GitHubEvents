plugins {
    id("githubevents.android.feature")
}

android {
    namespace = "parinexus.sample.githubevents.feature.search"
}

dependencies {
    implementation(project(":features:search-api"))
    implementation(libs.androidx.paging.compose)
    implementation(libs.androidx.lifecycle.process)
    testImplementation(libs.androidx.lifecycle.runtime.testing)
}
