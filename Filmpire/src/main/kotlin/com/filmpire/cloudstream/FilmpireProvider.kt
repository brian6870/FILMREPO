package com.filmpire.cloudstream

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.JsonNode

class FilmpireProvider : MainAPI() {
    override var name = "Filmpire"
    override var mainUrl = "https://api.themoviedb.org/3"
    override var lang = "en"
    override var hasMainPage = true
    override var hasQuickSearch = true
    override var supportedTypes = setOf(TvType.Movie, TvType.TvSeries)

    private val apiKey = "90b2cae8d7161e8ba0f3836240d7d352"
    private val imageBase = "https://image.tmdb.org/t/p/w500"
    private val siteUrl = "https://filmpire.sc"
    private val mapper = ObjectMapper()

    private fun apiUrl(path: String): String {
        val sep = if (path.contains("?")) "&" else "?"
        return "$mainUrl$path${sep}api_key=$apiKey"
    }

    override val mainPage = mainPageOf(
        "$siteUrl/" to "Home",
        "$siteUrl/movies" to "Popular Movies",
        "$siteUrl/tv-shows" to "Popular TV",
        "$siteUrl/trending" to "Trending"
    )

    private fun JsonNode.toSearchResponse(): SearchResponse? {
        val tmdbId = get("id").asInt()
        val title = get("title")?.asText() ?: get("name")?.asText() ?: return null
        val posterPath = get("poster_path")?.asText()
        val mediaType = get("media_type")?.asText()

        val type = when {
            mediaType == "tv" -> TvType.TvSeries
            mediaType == "movie" -> TvType.Movie
            has("first_air_date") -> TvType.TvSeries
            else -> TvType.Movie
        }

        val url = if (type == TvType.TvSeries) {
            "$siteUrl/tv/$tmdbId"
        } else {
            "$siteUrl/movie/$tmdbId"
        }

        val poster = if (!posterPath.isNullOrBlank()) "$imageBase$posterPath" else null

        return newMovieSearchResponse(title, url, type) {
            this.posterUrl = poster
        }
    }

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        val path = when (request.name) {
            "Home" -> "/trending/all/week"
            "Popular Movies" -> "/movie/popular"
            "Popular TV" -> "/tv/popular"
            "Trending" -> "/trending/all/week"
            else -> "/trending/all/week"
        }
        val json = app.get(apiUrl("$path?page=$page"))
        val root = mapper.readTree(json.text)
        val items = root.get("results").mapNotNull { it.toSearchResponse() }
        return newHomePageResponse(request.name, items)
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val json = app.get(apiUrl("/search/multi?query=$query"))
        val root = mapper.readTree(json.text)
        return root.get("results").mapNotNull { it.toSearchResponse() }
    }

    override suspend fun load(url: String): LoadResponse? {
        val tmdbId = url.substringAfterLast("/").substringBefore("?").toIntOrNull() ?: return null
        val isTv = url.contains("/tv/")
        val detailPath = if (isTv) "/tv/$tmdbId" else "/movie/$tmdbId"
        val json = app.get(apiUrl(detailPath))
        val obj = mapper.readTree(json.text)

        val title = obj.get("title")?.asText() ?: obj.get("name")?.asText() ?: return null
        val overview = obj.get("overview")?.asText() ?: ""
        val posterPath = obj.get("poster_path")?.asText()
        val backdropPath = obj.get("backdrop_path")?.asText()
        val year = (obj.get("release_date")?.asText() ?: obj.get("first_air_date")?.asText())?.take(4)?.toIntOrNull()
        val voteAverage = obj.get("vote_average")?.asDouble()?.toFloat() ?: 0f
        val seasons = obj.get("seasons")
        val runtime = obj.get("runtime")?.asInt() ?: obj.get("episode_run_time")?.get(0)?.asInt()
        val genres = obj.get("genres")?.mapNotNull { it.get("name")?.asText() }

        val posterUrl = if (!posterPath.isNullOrBlank()) "$imageBase$posterPath" else null
        val backdropUrl = if (!backdropPath.isNullOrBlank()) "$imageBase$backdropPath" else null

        if (isTv && seasons != null && seasons.size() > 0) {
            val episodes = mutableListOf<Episode>()
            for (season in seasons) {
                val seasonNum = season.get("season_number").asInt()
                if (seasonNum == 0) continue
                val episodeCount = season.get("episode_count").asInt()
                for (ep in 1..episodeCount) {
                    episodes.add(
                        newEpisode("$siteUrl/tv/$tmdbId?season=$seasonNum&episode=$ep") {
                            this.name = "S${seasonNum.toString().padStart(2, '0')}E${ep.toString().padStart(2, '0')}"
                            this.season = seasonNum
                            this.episode = ep
                        }
                    )
                }
            }
            return newTvSeriesLoadResponse(title, url, TvType.TvSeries, episodes) {
                this.posterUrl = posterUrl
                this.year = year
                this.plot = overview
                this.score = Score.Companion.from10(voteAverage)
                this.tags = genres
                this.duration = runtime
                this.backgroundPosterUrl = backdropUrl
            }
        } else {
            return newMovieLoadResponse(title, url, TvType.Movie, url) {
                this.posterUrl = posterUrl
                this.year = year
                this.plot = overview
                this.score = Score.Companion.from10(voteAverage)
                this.tags = genres
                this.duration = runtime
                this.backgroundPosterUrl = backdropUrl
            }
        }
    }

    override suspend fun loadLinks(
        url: String,
        isEpisode: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        val tmdbId = url.substringAfterLast("/").substringBefore("?").toIntOrNull() ?: return false
        val isTv = url.contains("/tv/")

        var season = 1
        var episode = 1
        if (isTv && url.contains("?season=")) {
            val params = url.substringAfter("?")
            season = params.substringAfter("season=").substringBefore("&").toIntOrNull() ?: 1
            episode = params.substringAfter("episode=").toIntOrNull() ?: 1
        }

        val embedUrls = if (isTv) {
            listOf(
                "https://vidsrc.xyz/embed/tv/$tmdbId/$season-$episode",
                "https://screenfetch4.cyou/embed/tv?tmdb=$tmdbId&season=$season&episode=$episode",
                "https://vidsrc.me/embed/tv/$tmdbId/$season-$episode",
                "https://vsrc.su/embed/tv?tmdb=$tmdbId&season=$season&episode=$episode"
            )
        } else {
            listOf(
                "https://vidsrc.xyz/embed/movie/$tmdbId",
                "https://screenfetch4.cyou/embed/movie?tmdb=$tmdbId",
                "https://vidsrc.me/embed/movie/$tmdbId",
                "https://vsrc.su/embed/movie?tmdb=$tmdbId"
            )
        }

        for (embedUrl in embedUrls) {
            try {
                val response = app.get(embedUrl, referer = "$siteUrl/")
                val html = response.text
                val videoRegex = Regex("""(https?://[^"'\s]+\.(?:m3u8|mp4|mkv|ts)[^"'\s]*)""")
                val matches = videoRegex.findAll(html)
                for (match in matches) {
                    val videoUrl = match.value
                    val quality = when {
                        videoUrl.contains("1080") || videoUrl.contains("1920") -> Qualities.P1080.value
                        videoUrl.contains("720") -> Qualities.P720.value
                        videoUrl.contains("480") -> Qualities.P480.value
                        videoUrl.contains("360") -> Qualities.P360.value
                        else -> Qualities.P720.value
                    }
                    val linkType = if (videoUrl.contains(".m3u8")) {
                        ExtractorLinkType.M3U8
                    } else {
                        ExtractorLinkType.VIDEO
                    }
                    val link = newExtractorLink(
                        name,
                        "$name - VidSrc",
                        videoUrl,
                        linkType
                    ) {
                        this.referer = embedUrl
                        this.quality = quality
                    }
                    callback(link)
                }
            } catch (_: Exception) {
                continue
            }
        }

        return true
    }
}
