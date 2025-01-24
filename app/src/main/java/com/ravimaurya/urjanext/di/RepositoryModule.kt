package com.ravimaurya.urjanext.di

import android.content.Context
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.ravimaurya.urjanext.data.repository_impl.AuthRepositoryImpl
import com.ravimaurya.urjanext.domain.repository.AuthenticationRepository
import com.ravimaurya.urjanext.presentation.home.urjalocation.ILocationService
import com.ravimaurya.urjanext.presentation.home.urjalocation.LocationService
import com.ravimaurya.urjanext.util.UserDataPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideDataStoreContext(context: Context): UserDataPreferences{
        return UserDataPreferences(context)
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth{
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideAuthenticationRepository(firebaseAuth: FirebaseAuth): AuthenticationRepository{
        return AuthRepositoryImpl(firebaseAuth)
    }



        @Singleton
        @Provides
        fun provideLocationClient(
            @ApplicationContext context: Context
        ): ILocationService = LocationService(
            context,
            LocationServices.getFusedLocationProviderClient(context)
        )


}