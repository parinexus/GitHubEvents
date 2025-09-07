plugins {
    `kotlin-dsl`
}

group = "parinexus.sample.githubevents.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApplicationCompose") {
            id = "githubevents.android.application.compose"
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }
        register("androidApplication") {
            id = "githubevents.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidApplicationJacoco") {
            id = "githubevents.android.application.jacoco"
            implementationClass = "AndroidApplicationJacocoConventionPlugin"
        }
        register("androidLibraryCompose") {
            id = "githubevents.android.library.compose"
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }
        register("androidLibrary") {
            id = "githubevents.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidFeature") {
            id = "githubevents.android.feature"
            implementationClass = "AndroidFeatureConventionPlugin"
        }
        register("androidFeatureApi") {
            id = "githubevents.android.feature-api"
            implementationClass = "AndroidFeatureApiConventionPlugin"
        }
        register("test") {
            id = "githubevents.test"
            implementationClass = "TestConventionPlugin"
        }
        register("androidTest") {
            id = "githubevents.android.test"
            implementationClass = "AndroidTestConventionPlugin"
        }
        register("androidHilt") {
            id = "githubevents.android.hilt"
            implementationClass = "AndroidHiltConventionPlugin"
        }
        register("androidHiltTest") {
            id = "githubevents.android.hilt.test"
            implementationClass = "AndroidHiltTestConventionPlugin"
        }
        register("kotlinLibrary") {
            id = "githubevents.kotlin.library"
            implementationClass = "KotlinLibraryConventionPlugin"
        }
    }
}
