import java.util.Locale

plugins {
    id("githubevents.android.application")
    id("githubevents.android.application.compose")
    id("githubevents.android.hilt")
}

android {
    defaultConfig {
        namespace = "parinexus.sample.githubevents"
        applicationId = "parinexus.sample.githubevents"
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        setProperty("archivesBaseName", "${parent?.name?.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
                Locale.getDefault()
            ) else it.toString()
        }}-$versionName")
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles("proguard-rules.pro")
        }
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }
    }

    lint {
        abortOnError = false
        htmlOutput = file("${rootProject.rootDir}/build/reports/${project.name}-lint.html")
        lintConfig = file("${rootProject.rootDir}/config/gradle/lint.xml")
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":libraries:design"))
    implementation(project(":libraries:navigation"))
    implementation(project(":features:search-api"))
    implementation(project(":features:search"))
    implementation(project(":features:event-detail-api"))
    implementation(project(":features:event-detail"))
    implementation(project(":data:repository"))
    implementation(project(":data:remote"))
    implementation(project(":data:local"))
    implementation(libs.hilt.android)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtimeCompose)
}
