package com.ravimaurya.urjanext.data.repository_impl

import com.google.firebase.firestore.FirebaseFirestore
import com.ravimaurya.urjanext.domain.model.UserModel
import com.ravimaurya.urjanext.domain.repository.ProfileRepository
import com.ravimaurya.urjanext.util.Resource
import com.ravimaurya.urjanext.util.UrjaConstants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class ProfileRepositoryImpl @Inject constructor(private val firestore: FirebaseFirestore): ProfileRepository {

    private val userRef = firestore.collection(UrjaConstants.URJA_COLLECTION)

    override suspend fun createUser(userModel: UserModel) = flow {
        try {
            emit(Resource.Loading)
            userRef.document()
                .set(userModel)
                .addOnSuccessListener {
                    println("User Successfully Added!!!!!")
                }
                .addOnFailureListener{
                    println("Failed to Add User!!!!!!!")
                }


        } catch (e: Exception){
            println("CreateUser Exception Error!!!!")
        }

    }

    override suspend fun getUser(userId: Int) = flow {
        try {
            emit(Resource.Loading)

        } catch(e: Exception){
            println("GetUser Exception Error!!!!")
        }
    }

    override suspend fun updateUser(userModel: UserModel): Flow<Resource<UserModel>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteUser(userId: UserModel): Flow<Resource<String>> {
        TODO("Not yet implemented")
    }

    ///// Profile FireStore Repository-Implementation-ViewModel-



}