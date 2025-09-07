plugins {
    id("githubevents.android.feature-api")
}

android {
    namespace = "parinexus.sample.githubevents.feature.searchapi"
}

dependencies {
    implementation(libs.androidx.paging.compose)
}
