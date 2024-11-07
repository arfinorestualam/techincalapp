package com.fin.technicalapp.core.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.fin.technicalapp.core.utils.HeaderInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {
    single {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .addInterceptor(getChuckerInterceptor(get()))
            .addInterceptor(HeaderInterceptor())
            .build()
    }
    single {
        Retrofit.Builder()
            .baseUrl("https://fakestoreapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
    }

    single { get<Retrofit>().create(AuthenticationApi::class.java) }

    single { get<Retrofit>().create(ProductApi::class.java) }

    fun getChuckerInterceptor(context: Context): ChuckerInterceptor {
        return ChuckerInterceptor.Builder(context).build()
    }

    val repositoryModule = module {

    }

    val stashModule = module {

    }
}