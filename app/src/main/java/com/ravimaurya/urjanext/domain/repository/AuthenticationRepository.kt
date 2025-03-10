package com.ravimaurya.urjanext.domain.repository

import com.ravimaurya.urjanext.domain.model.UserModel
import com.ravimaurya.urjanext.util.Resource
import kotlinx.coroutines.flow.Flow

interface AuthenticationRepository {

    suspend fun checkUserExist() : Flow<Resource<Boolean>>

    suspend fun register(userModel: UserModel) : Flow<Resource<UserModel>>

    suspend fun login(userModel: UserModel) : Flow<Resource<UserModel>>

    suspend fun deleteAccount() : Flow<Resource<String>>

    suspend fun createUser(userModel: UserModel) : Flow<Resource<Boolean>>

    suspend fun getUser(userId: String) : Flow<Resource<UserModel?>>

}