plugins {
    kotlin("multiplatform")
    id("com.android.library")
    // plugin for sqldelight
    id("com.squareup.sqldelight")
}

kotlin {
    android()
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
        }
    }

    sourceSets {

        val commonMain by getting {
            // Dependencies for commonMain dir, shared code for both platforms
            // all libraries in shared need to be in Kotlin
            dependencies {
                // sqldelight - db in kotlin supports KMM
                implementation("com.squareup.sqldelight:runtime:1.5.3")
                // datetime kotlin lib worse than java version
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            // Dependencies for androidMain dir
            dependencies {
                // sqldelight - android specific version in order to use it
                implementation("com.squareup.sqldelight:android-driver:1.5.3")
            }
        }
        val androidTest by getting
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            // Dependencies for iosMain dir
            dependencies {
                // sqldelight - for iOS
                implementation("com.squareup.sqldelight:native-driver:1.5.3")
            }

            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
    }
}

// configuring sqldelight
// to use sqldelight we need a plugin
sqldelight {
    // specify a name
    database("NoteDatabase") {
        packageName = "com.plcoding.noteappkmm.database"
        // folder where the db will be which
        sourceFolders = listOf("sqldelight")
        // this directory needs to be created manually

        // THEN add tables and CRUD ops.
        // then rebuild to generate classes
    }
}

android {
    namespace = "com.plcoding.noteappkmm"
    compileSdk = 33
    defaultConfig {
        minSdk = 21
        targetSdk = 33
    }
}