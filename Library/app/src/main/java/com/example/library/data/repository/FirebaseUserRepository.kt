package com.example.library.data.repository

import com.example.library.data.User
import com.example.library.domain.UserRepository
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FirebaseUserRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val fireStore:FirebaseFirestore
):UserRepository {

    override suspend fun createUser(email: String, password: String): Result<Unit> =
        withContext(Dispatchers.IO){
            return@withContext try{
                firebaseAuth.createUserWithEmailAndPassword(email, password).await()
                Result.success(Unit)
            }catch(e:Exception){
                Result.failure(e)
            }
    }

    override suspend fun signInUser(email: String, password: String): Result<FirebaseUser?> =
        withContext(Dispatchers.IO){
            return@withContext try{
                val result= firebaseAuth.signInWithEmailAndPassword(email, password).await()
                Result.success(result.user)
            }catch (e:Exception){
                Result.failure(e)
            }
        }

    override suspend fun saveUser(user: User): Result<Unit> =
        withContext(Dispatchers.IO){
            val uid= firebaseAuth.currentUser?.uid?:
                return@withContext Result.failure(IllegalStateException("No Account"))
            return@withContext try{
                fireStore.collection(USER_COLLECTION)
                    .document(uid)
                    .set(user)
                    .await()
                Result.success(Unit)
            }catch (e:Exception){
                Result.failure(e)
            }
        }

    companion object {
        const val USER_COLLECTION="users"
    }

}