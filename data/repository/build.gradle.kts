plugins {
    id("githubevents.android.library")
    id("githubevents.android.hilt")
    id("githubevents.test")
}

android {
    namespace = "parinexus.sample.githubevents.data.repository"
}

dependencies {
    implementation(project(":domain"))
    implementation(libs.androidx.paging.runtime)
    implementation(libs.retrofit.core)

    implementation(libs.room.ktx)
    implementation(libs.androidx.paging.common)

}
