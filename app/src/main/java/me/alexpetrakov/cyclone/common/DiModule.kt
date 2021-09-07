package me.alexpetrakov.cyclone.common

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import com.squareup.moshi.Moshi
import me.alexpetrakov.cyclone.BuildConfig
import me.alexpetrakov.cyclone.common.data.ApiKeyInjector
import me.alexpetrakov.cyclone.common.data.adapters.*
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val commonModule = module {
    single { Cicerone.create() }
    single { get<Cicerone<Router>>().router }
    single { get<Cicerone<Router>>().getNavigatorHolder() }

    val okHttp = OkHttpClient.Builder()
        .addInterceptor(ApiKeyInjector())
        .build()

    val moshi = Moshi.Builder()
        .add(InstantAdapter())
        .add(IconAdapter())
        .add(TemperatureAdapter())
        .add(DistanceAdapter())
        .add(PressureAdapter())
        .add(SpeedAdapter())
        .build()

    single {
        Retrofit.Builder()
            .client(okHttp)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(BuildConfig.API_BASE_URL)
            .build()
    }
}