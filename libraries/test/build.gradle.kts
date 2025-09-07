plugins {
    id("githubevents.android.library")
    id("githubevents.android.library.compose")
    id("githubevents.android.hilt")
}

android {
    namespace = "parinexus.sample.githubevents.libraries.test"
}

dependencies {
    implementation(libs.android.material)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.okhttp.mockWebServer)
    implementation(libs.androidx.compose.ui.junit4)
    implementation(libs.androidx.compose.ui.test)
}
