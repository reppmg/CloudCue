package repp.max.cloudcue.api

import okhttp3.Interceptor
import okhttp3.Response
import repp.max.cloudcue.Constants

class ApiKeyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val originalUrl = request.url

        val newUrl = originalUrl.newBuilder()
            .addQueryParameter("appid", Constants.apiKey)
            .build()
        return chain.proceed(request.newBuilder()
            .url(newUrl)
            .build())
    }
}