import com.lagradost.cloudstream3.gradle.CloudstreamExtension
import com.android.build.gradle.BaseExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

version = 1

cloudstream {
    description = "Watch free movies and TV shows with streams from AllMovieLand"
    authors = listOf("brian6870")
    status = 1
    tvTypes = listOf("Movie", "TvSeries")
    language = "en"
    iconUrl = "https://raw.githubusercontent.com/brian6870/FILMREPO/main/icon.png"
}

tasks.withType<KotlinJvmCompile> {
    compilerOptions {
        allWarningsAsErrors = false
    }
}
