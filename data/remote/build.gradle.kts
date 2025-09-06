plugins {
    id("githubevents.android.library")
    id("githubevents.android.hilt")
    id("githubevents.android.hilt.test")
    id("githubevents.test")
}

android {
    namespace = "parinexus.sample.githubevents.data.remote"

    buildTypes {
        getByName("debug") {
            buildConfigField("String", "BASE_URL", "\"https://api.github.com/\"")
            buildConfigField("String", "GITHUB_TOKEN", "\"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\"")
            buildConfigField("String", "APPLICATION_ID", "\"parinexus.sample.githubevents\"")
        }
        getByName("release") {
            buildConfigField("String", "BASE_URL", "\"https://api.github.com/\"")
            buildConfigField("String", "GITHUB_TOKEN", "\"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\"")
            buildConfigField("String", "APPLICATION_ID", "\"parinexus.sample.githubevents\"")
        }
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(project(":data:repository"))
    implementation(libs.okhttp.logging)
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.gson.converter)
    implementation(libs.androidx.paging.common)

    testImplementation(libs.okhttp.mockWebServer)
}
