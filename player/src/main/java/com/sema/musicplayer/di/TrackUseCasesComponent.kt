package com.sema.musicplayer.di

import com.sema.musicplayer.domain.DownloadDrawableFromUrlUseCase
import com.sema.musicplayer.domain.SporififiDownloadDrawableFromUrlUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class TrackUseCasesComponent {
    @Binds
    abstract fun bindDownloadDrawableFromUrlUseCase(
        impl: SporififiDownloadDrawableFromUrlUseCase
    ): DownloadDrawableFromUrlUseCase

}