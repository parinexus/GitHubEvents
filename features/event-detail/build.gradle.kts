plugins {
    id("githubevents.android.feature")
}

android {
    namespace = "parinexus.sample.githubevents.feature.eventdetail"
}

dependencies {
    implementation(project(":features:event-detail-api"))
    implementation(project(":libraries:navigation"))
}
