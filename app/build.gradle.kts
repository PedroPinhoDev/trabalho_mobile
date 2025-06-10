import org.gradle.testing.jacoco.plugins.JacocoPluginExtension
import org.gradle.testing.jacoco.plugins.JacocoTaskExtension
import org.gradle.testing.jacoco.tasks.JacocoReport

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.gms.google-services")
    id("jacoco")
}

android {
    namespace = "com.example.trabalho"
    // CORRETO: Mantido no SDK 35, que é exigido pelas suas dependências.
    compileSdk = 35

    defaultConfig {
        applicationId = "com.pedido.trabalho"
        minSdk = 24
        // CORRETO: Mantido no SDK 35.
        targetSdk = 35
        versionCode = 2
        versionName = "1.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }
}

// Configura a versão da ferramenta Jacoco
extensions.configure<JacocoPluginExtension> {
    toolVersion = "0.8.12"
}

// Exclui classes do JDK/SUN da instrumentação Jacoco
tasks.withType<Test>().configureEach {
    extensions.configure<JacocoTaskExtension> {
        excludes = listOf(
            "java.*",
            "jdk.internal.*",
            "sun.*",
            "com.sun.*"
        )
    }
}

dependencies {
    // ==== produção ====
    implementation(platform("com.google.firebase:firebase-bom:33.1.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.code.gson:gson:2.8.8")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.ui.test.android)
    implementation(libs.androidx.junit.ktx)

    testImplementation("com.google.auto.service:auto-service-annotations:1.0.1")


    // ==== testes JVM (unit tests) ====
    testImplementation(libs.junit)
    // CORRETO: Versão do Robolectric atualizada para ser compatível com o SDK 35.
    testImplementation("org.robolectric:robolectric:4.13")
    testImplementation("androidx.test:core:1.5.0")
    testImplementation("io.mockk:mockk:1.13.10")
    testImplementation("com.squareup.okhttp3:mockwebserver:4.9.3")

    // ==== testes instrumentados (Android) ====
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation("androidx.test:runner:1.5.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.0")
}

// Task para gerar relatório de cobertura Jacoco após os unit tests
tasks.register<JacocoReport>("jacocoTestReport") {
    group = "verification"
    description = "Gera relatório Jacoco após testDebugUnitTest"

    dependsOn("testDebugUnitTest")

    reports {
        html.required.set(true)
        xml.required.set(true)
        csv.required.set(false)
    }

    // classes compiladas pelo Kotlin/Java
    val classesDir = fileTree("$buildDir/tmp/kotlin-classes/debug") {
        exclude(
            "**/R.class", "**/R\$*.class",
            "**/BuildConfig.*", "**/Manifest*.*"
        )
    }
    classDirectories.setFrom(files(classesDir))

    // fontes do seu app
    sourceDirectories.setFrom(files("src/main/java", "src/main/kotlin"))

    // dados de execução do Jacoco
    executionData.setFrom(files("$buildDir/jacoco/testDebugUnitTest.exec"))
}