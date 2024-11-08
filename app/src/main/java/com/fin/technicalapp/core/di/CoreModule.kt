package com.fin.technicalapp.core.di

import android.content.Context
import android.content.SharedPreferences
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.fin.technicalapp.core.data.authentication.api.repository.AuthenticationRepository
import com.fin.technicalapp.core.data.authentication.implemantion.remote.AuthenticationApi
import com.fin.technicalapp.core.data.authentication.implemantion.repository.AuthenticationRepositoryImpl
import com.fin.technicalapp.core.data.product.api.repository.ProductRepository
import com.fin.technicalapp.core.data.product.implemantion.database.ProductDatabase
import com.fin.technicalapp.core.data.product.implemantion.remote.ProductApi
import com.fin.technicalapp.core.data.product.implemantion.repository.ProductRepositoryImpl
import com.fin.technicalapp.core.stash.api.Stash
import com.fin.technicalapp.core.stash.implemantion.StashImpl
import com.fin.technicalapp.core.utils.AppExecutors
import com.fin.technicalapp.core.utils.HeaderInterceptor
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
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
        single { ProductDatabase.getInstance(androidContext()) }
        single { get<ProductDatabase>().cartDao() }

        factory { AppExecutors() }
        single<AuthenticationRepository> { AuthenticationRepositoryImpl(get(), get()) }
        single<ProductRepository> { ProductRepositoryImpl(get(), get()) }
    }

    val stashModule = module {
        single<SharedPreferences> {
            androidContext().getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        }
        single<CoroutineDispatcher> { Dispatchers.IO }
        single<Stash> { StashImpl(get(), get()) }
    }
}