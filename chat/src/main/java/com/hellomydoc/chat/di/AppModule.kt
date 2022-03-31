package com.hellomydoc.chat.di

import android.content.Context
import com.hellomydoc.chat.AppDelegate
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDelegate(@ApplicationContext app: Context): AppDelegate {
        return app as AppDelegate
    }
}