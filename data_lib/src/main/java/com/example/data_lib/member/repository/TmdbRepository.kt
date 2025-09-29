package com.example.data_lib.member.repository

import com.example.base_lib.executors.AppExecutors
import com.example.base_lib.net.NetConstant
import com.example.base_lib.net.NetEngine
import com.example.data_lib.tmdb.TmdbApiWebService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TmdbRepository(

) {
    private val netEngine: NetEngine = NetEngine.getInstance()

    suspend fun loadData(apiKey: String) = withContext(Dispatchers.IO) {
        netEngine.setBaseUrl(NetConstant.URL_TMDB_BASE)
        netEngine.createService(TmdbApiWebService::class.java)
            .getPopularMovies(apiKey)
    }
}