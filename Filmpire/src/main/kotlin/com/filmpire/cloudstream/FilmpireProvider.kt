package com.filmpire.cloudstream

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.metaproviders.TmdbProvider
import com.lagradost.cloudstream3.utils.*
import org.json.JSONArray
import org.json.JSONObject

class FilmpireProvider : TmdbProvider() {
    override var name = "Filmpire"
    override val hasMainPage = true
    override val hasQuickSearch = true
    override val supportedTypes = setOf(TvType.Movie, TvType.TvSeries)

    private val apiKey = "90b2cae8d7161e8ba0f3836240d7d352"

    override val mainPage = mainPageOf(
        "https://api.themoviedb.org/3/trending/all/week?api_key=$apiKey" to "Trending",
        "https://api.themoviedb.org/3/movie/popular?api_key=$apiKey" to "Popular Movies",
        "https://api.themoviedb.org/3/tv/popular?api_key=$apiKey" to "Popular TV",
    )

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        val res = try { JSONObject(data) } catch (_: Exception) { return false }
        val imdbId = res.optString("imdbId", "")
        val season = if (res.has("season")) res.optInt("season") else null
        val episode = if (res.has("episode")) res.optInt("episode") else null

        if (imdbId.isNotBlank()) {
            allMovieLandExtractor(imdbId, season, episode, callback)
        }
        return true
    }

    private suspend fun allMovieLandExtractor(
        imdbId: String,
        season: Int?,
        episode: Int?,
        callback: (ExtractorLink) -> Unit
    ) {
        val allmovielandAPI = "https://allmovieland.io"
        try {
            val playerJs = app.get("https://allmovieland.link/player.js?v=60%20128").text
            val host = Regex("""const AwsIndStreamDomain.*'(.*)';""")
                .find(playerJs)?.groupValues?.getOrNull(1) ?: return

            val resPage = app.get("$host/play/$imdbId", referer = "$allmovielandAPI/").text
            val resData = resPage
                .substringAfter("{").substringBefore(";").substringBefore(")")

            val json = JSONObject("{$resData}")
            val csrf = json.optString("key", "")
            val headers = mapOf("X-CSRF-TOKEN" to csrf)
            val fileUrl = json.optString("file", "").let {
                if (it.startsWith("http")) it else host + it
            }

            val serverResp = app.get(fileUrl, headers = headers, referer = "$allmovielandAPI/").text
            val cleaned = serverResp.replace(Regex(""",\s*/"""), "").replace(Regex(""",\s*\[\s*]"""), "")
            val servers = JSONArray(cleaned)

            for (i in 0 until servers.length()) {
                val server = servers.optJSONObject(i) ?: continue

                val filesToProcess = mutableListOf<Pair<String, String>>() // file to title

                if (season == null) {
                    val file = server.optString("file", "")
                    val title = server.optString("title", "")
                    if (file.isNotBlank()) filesToProcess.add(file to title)
                } else {
                    if (server.optString("id") == season.toString()) {
                        val folders = server.optJSONArray("folder") ?: continue
                        for (j in 0 until folders.length()) {
                            val epObj = folders.optJSONObject(j) ?: continue
                            if (epObj.optString("episode") == episode.toString()) {
                                val epFolders = epObj.optJSONArray("folder") ?: continue
                                for (k in 0 until epFolders.length()) {
                                    val f = epFolders.optJSONObject(k) ?: continue
                                    val fUrl = f.optString("file", "")
                                    val fTitle = f.optString("title", "")
                                    if (fUrl.isNotBlank()) filesToProcess.add(fUrl to fTitle)
                                }
                            }
                        }
                    }
                }

                for ((file, title) in filesToProcess) {
                    try {
                        val plResp = app.post(
                            "$host/playlist/$file.txt",
                            headers = headers,
                            referer = "$allmovielandAPI/"
                        )
                        if (plResp.code != 200) continue
                        val plUrl = plResp.text.trim()
                        if (plUrl.isBlank()) continue

                        M3u8Helper.generateM3u8(
                            "AllMovieLand-$title",
                            plUrl,
                            allmovielandAPI,
                            headers = mapOf(
                                "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36",
                                "Referer" to allmovielandAPI,
                                "Origin" to allmovielandAPI
                            )
                        ).forEach(callback)
                    } catch (_: Exception) {}
                }
            }
        } catch (_: Exception) {}
    }
}
