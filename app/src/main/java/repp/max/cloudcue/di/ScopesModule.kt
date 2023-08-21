package repp.max.cloudcue.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class ScopesModule {

    @Provides
    @Singleton
    fun provideSingletoneScope() : CoroutineScope = GlobalScope
}