package com.prueba.hugo.di.inject_repository

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder

/* Created by
 Victor Hernandez
 on 13/01/2020.
 contact : victoralfonso92@yahoo.com
 */
class InjectRepository {

//    fun provideOkhttpClient(): OkHttpClient {
//        try {
//            var loggingInterceptor = HttpLoggingInterceptor()
//            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
//            var builder: OkHttpClient.Builder = OkHttpClient.Builder()
//            builder.addInterceptor(NetworkInterceptor())
//            builder.addInterceptor(loggingInterceptor)
//            builder.connectTimeout(300, TimeUnit.SECONDS)
//            builder.readTimeout(80, TimeUnit.SECONDS)
//            builder.writeTimeout(90, TimeUnit.SECONDS)
//            builder.retryOnConnectionFailure(true)
//            builder.addNetworkInterceptor(Interceptor {
//                val request: Request = it.request().newBuilder().addHeader("Connection", "close").build()
//                return@Interceptor it.proceed(request)
//            })
//            if (BuildConfig.DEBUG) {
//                builder.addInterceptor(OkHttpProfilerInterceptor())
//            }
//            return builder.build()
//        } catch (e: Exception) {
//            throw RuntimeException(e)
//        }
//    }

//    inline fun <reified T>getRetrofit(url: String): T {
//        val retrofit = Retrofit.Builder()
//            .addConverterFactory(GsonConverterFactory.create())
//            .baseUrl(url)
//            .client(provideOkhttpClient())
//            .build()
//        return retrofit.create(T::class.java)
//    }

    internal fun provideGson(): Gson {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        return gsonBuilder.create()
    }
}