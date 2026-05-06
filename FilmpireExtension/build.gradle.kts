import com.lagradost.cloudstream3.gradle.CloudstreamExtension
import com.android.build.gradle.BaseExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

version = 1

cloudstream {
    description = "Watch free movies and TV shows from Filmpire"
    authors = listOf("AI-Agent")
    status = 1
    tvTypes = listOf("Movie", "TvSeries")
    language = "en"
}

tasks.withType<KotlinJvmCompile> {
    compilerOptions {
        allWarningsAsErrors = false
    }
}
