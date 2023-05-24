package com.sandbox.serviceproject.di

import android.content.Context
import androidx.room.Room
import com.sandbox.serviceproject.data.db.HighScoreDao
import com.sandbox.serviceproject.data.db.HighScoreDatabase
import com.sandbox.serviceproject.data.repository.HighScoreRepository
import com.sandbox.serviceproject.data.repository.HighScoreRepositoryImpl
import com.sandbox.serviceproject.domain.GetScoresUseCase
import com.sandbox.serviceproject.domain.GetScoresUseCaseImpl
import com.sandbox.serviceproject.domain.UpdateScoresUseCase
import com.sandbox.serviceproject.domain.UpdateScoresUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@InstallIn(ViewModelComponent::class)
@Module
object MotionGameModule {

    @Provides
    fun providesHighScoreDataBase(@ApplicationContext context: Context): HighScoreDatabase {
        return Room.databaseBuilder(context, HighScoreDatabase::class.java, HighScoreDatabase.NAME)
            .build()
    }

    @Provides
    fun providesHighScoreDao(db: HighScoreDatabase): HighScoreDao {
        return db.highScoreDao()
    }

    @Provides
    fun providesHighScoreRepository(dao: HighScoreDao): HighScoreRepository {
        return HighScoreRepositoryImpl(dao)
    }

    @Provides
    fun providesGetScoresUseCase(repository: HighScoreRepository): GetScoresUseCase {
        return GetScoresUseCaseImpl(repository)
    }

    @Provides
    fun providesUpdateScoresUseCase(repository: HighScoreRepository): UpdateScoresUseCase {
        return UpdateScoresUseCaseImpl(repository)
    }
}
