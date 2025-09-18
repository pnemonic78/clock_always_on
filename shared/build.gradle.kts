import org.apache.tools.ant.taskdefs.condition.Os
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.jetbrainsCompose)
}

kotlin {
    androidTarget {
        compilations.all {
            compileTaskProvider.configure {
                compilerOptions {
                    jvmTarget.set(JvmTarget.JVM_11)
                }
            }
        }
    }

    if (Os.isFamily(Os.FAMILY_MAC)) {
        listOf(
            iosArm64(),
            iosSimulatorArm64(),
            iosX64()
        ).forEach {
            it.binaries.framework {
                baseName = "shared"
                isStatic = true
            }
        }
    }

    sourceSets {
        androidMain {
            dependencies {
                api(libs.compose.ui.tooling.preview)
            }
        }
        commonMain {
            dependencies {
                implementation(compose.components.resources)

                api(libs.compose.foundation)
                api(libs.compose.material3)
                api(libs.datastore)
                api(libs.datastore.preferences)
                api(libs.kotlin.date)
                api(libs.viewmodel)
            }
        }
        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }
    }
}

android {
    namespace = "pnemonic.clock_always_on"
    compileSdk = 36
    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}

compose {
    resources {
        generateResClass = always
        publicResClass = true
    }
}
