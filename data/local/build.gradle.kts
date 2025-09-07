plugins {
    id("githubevents.android.library")
    id("githubevents.android.hilt")
    id("githubevents.android.hilt.test")
    id("githubevents.test")
    id("com.google.devtools.ksp")
}

android {
    namespace = "parinexus.sample.githubevents.data.local"
}

dependencies {
    implementation(project(":data:repository"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.room.ktx)
    implementation(libs.room.paging)
    implementation(libs.room.runtime)
    ksp(libs.room.compiler)
    api(libs.paging.common.android)
}
