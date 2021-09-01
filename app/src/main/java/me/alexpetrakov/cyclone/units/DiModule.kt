package me.alexpetrakov.cyclone.units

import me.alexpetrakov.cyclone.units.data.UnitsDataStore
import me.alexpetrakov.cyclone.units.domain.UnitsRepository
import org.koin.dsl.module

val unitsModule = module {
    single<UnitsRepository> { UnitsDataStore() }
}