version = 1

dependencies {
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("androidx.core:core-ktx:1.12.0")
}

android {
    buildFeatures {
        buildConfig = true
        viewBinding = true
    }
    defaultConfig {
        buildConfigField("String", "TMDB_API", "\"90b2cae8d7161e8ba0f3836240d7d352\"")
        buildConfigField("String", "ZSHOW_API", "\"\"")
        buildConfigField("String", "ANICHI_API", "\"\"")
        buildConfigField("String", "ANICHI_APP", "\"\"")
        buildConfigField("String", "KissKh", "\"\"")
        buildConfigField("String", "KisskhSub", "\"\"")
        buildConfigField("String", "SUPERSTREAM_THIRD_API", "\"\"")
        buildConfigField("String", "SUPERSTREAM_FOURTH_API", "\"\"")
        buildConfigField("String", "SUPERSTREAM_FIRST_API", "\"\"")
        buildConfigField("String", "PROXYAPI", "\"\"")
        buildConfigField("String", "KAISVA", "\"\"")
        buildConfigField("String", "KAIMEG", "\"\"")
        buildConfigField("String", "KAIDEC", "\"\"")
        buildConfigField("String", "KAIENC", "\"\"")
        buildConfigField("String", "MOVIEBOX_SECRET_KEY_ALT", "\"\"")
        buildConfigField("String", "MOVIEBOX_SECRET_KEY_DEFAULT", "\"\"")
        buildConfigField("String", "NuvFeb", "\"\"")
        buildConfigField("String", "YFXENC", "\"\"")
        buildConfigField("String", "YFXDEC", "\"\"")
    }
}

cloudstream {
    description = "Watch free movies and TV shows"
    authors = listOf("brian6870")
    status = 1
    tvTypes = listOf("Movie", "TvSeries", "Anime", "Cartoon")
    language = "en"
}
