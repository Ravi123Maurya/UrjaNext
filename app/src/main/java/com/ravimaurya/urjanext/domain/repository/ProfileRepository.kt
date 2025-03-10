package com.ravimaurya.urjanext.domain.repository

import com.ravimaurya.urjanext.domain.model.UserModel
import com.ravimaurya.urjanext.util.Resource
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {

    suspend fun createUser(userModel: UserModel): Flow<Resource<UserModel>>

    suspend fun getUser(userId: Int): Flow<Resource<UserModel>>

    suspend fun updateUser(userModel: UserModel): Flow<Resource<UserModel>>

    suspend fun deleteUser(userId: UserModel): Flow<Resource<String>>

}