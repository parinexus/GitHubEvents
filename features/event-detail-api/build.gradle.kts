plugins {
    id("githubevents.android.feature")
}

android {
    namespace = "parinexus.sample.githubevents.feature.eventdetailapi"
}

dependencies {
    implementation(project(":domain"))
}
