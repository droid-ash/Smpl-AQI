package com.ash.smplaqi.di

import android.content.Context
import com.ash.smplaqi.WS_URL
import com.ash.smplaqi.data.db.CityAqiDatabase
import com.ash.smplaqi.data.model.CityAqi
import com.ash.smplaqi.repository.WebSocketImpl
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.Request
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun providesOkhttpClient(): OkHttpClient {
        return OkHttpClient().newBuilder().pingInterval(30, TimeUnit.SECONDS).build()
    }

    @Provides
    @Singleton
    fun providesOkHttpRequest(): Request {
        return Request.Builder().url(WS_URL).build()
    }

    @Singleton
    @Provides
    fun providesDatabase(@ApplicationContext appContext: Context) =
        CityAqiDatabase.createDb(appContext)

    @Singleton
    @Provides
    fun providesAppDao(db: CityAqiDatabase) = db.cityAqiDao()

    @Singleton
    @Provides
    fun providesGson() = Gson()

    @Singleton
    @Provides
    fun providesGsonTypeToken(): Type {
        return object : TypeToken<List<CityAqi?>?>() {}.type
    }

    @Singleton
    @Provides
    fun providesWebSocketImpl(
        okHttpClient: OkHttpClient, request: Request, gson: Gson, type: Type
    ): WebSocketImpl {
        return WebSocketImpl(okHttpClient, request, gson, type)
    }
}