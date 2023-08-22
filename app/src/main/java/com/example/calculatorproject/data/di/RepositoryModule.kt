package com.example.calculatorproject.data.di

import com.example.calculatorproject.data.repository.LoginRepository
import com.example.calculatorproject.data.repository.LoginRepositoryImpl
import com.example.calculatorproject.data.repository.RegisterRepository
import com.example.calculatorproject.data.repository.RegisterRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideLoginRepository(loginRepositoryImpl: LoginRepositoryImpl): LoginRepository = loginRepositoryImpl

    @Provides
    @Singleton
    fun provideRegisterRepository(registerRepositoryImpl: RegisterRepositoryImpl): RegisterRepository = registerRepositoryImpl
}