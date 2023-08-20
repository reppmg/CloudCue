package repp.max.cloudcue.api

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import repp.max.cloudcue.Config
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {
    @Provides
    @Singleton
    fun provideWeatherApiService(): WeatherApi {
        val retrofit = createRetrofit("appid", Config.weatherApiKey, Config.weatherBaseUrl)
        return retrofit.create(WeatherApi::class.java)
    }

    @Provides
    @Singleton
    fun provideTimeApiService(): CityTimeApi {
        val retrofit = createRetrofit("api_key", Config.gmtApiKey, Config.gmtBaseUrl)
        return retrofit.create(CityTimeApi::class.java)
    }

    private fun createRetrofit(apiKeyQueryName: String, apiKey: String, baseUrl: String): Retrofit {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(ApiKeyInterceptor(apiKeyQueryName, apiKey))
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            })
            .build()
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}