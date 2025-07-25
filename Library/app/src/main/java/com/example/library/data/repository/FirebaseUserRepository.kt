package com.example.library.data.repository

import com.example.library.FirebaseExternalUser
import com.example.library.data.ExternalUser
import com.example.library.data.User
import com.example.library.domain.UserRepository
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FirebaseUserRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val fireStore:FirebaseFirestore
):UserRepository {

    override suspend fun createUser(email: String, password: String): Result<ExternalUser?> =
        withContext(Dispatchers.IO){
            return@withContext try{
                val result=firebaseAuth.createUserWithEmailAndPassword(email, password).await()
                Result.success(result.user?.let { FirebaseExternalUser(it) })
            }catch(e:Exception){
                Result.failure(e)
            }
    }

    override suspend fun deleteUserAccount(user:ExternalUser?): Result<Unit> = withContext(Dispatchers.IO){
        return@withContext try{
            if(user==null){ firebaseAuth.currentUser?.delete()?.await() }
            else{ user.delete() }
            Result.success(Unit)
        }catch (e:Exception){
            Result.failure(e)
        }
    }

    override suspend fun getUser(uid: String): Result<User> =
        withContext(Dispatchers.IO){
            return@withContext try{
                val docRef = fireStore.collection(USER_COLLECTION).document(uid)
                val snapshot = docRef.get().await()
                if (!snapshot.exists()) {
                    return@withContext Result.failure(IllegalStateException("No user data"))
                }
                val data = snapshot.toObject(User::class.java)
                    ?: return@withContext Result.failure(IllegalStateException("Failed to parse user data"))
                Result.success(data)
            }catch (e:Exception){
                Result.failure(e)
            }
        }

    override suspend fun updateUser(data: Map<String, Any>):Result<Unit> =
        withContext(Dispatchers.IO){
            val uid= firebaseAuth.currentUser?.uid
                ?: return@withContext Result.failure(IllegalStateException("No Account"))
            return@withContext try{
                fireStore.collection(USER_COLLECTION)
                    .document(uid)
                    .update(data)
                    .await()
                Result.success(Unit)
            }catch (e:Exception){
                Result.failure(e)
            }
        }

    override suspend fun deleteUserData(uid:String): Result<User?> =
        withContext(Dispatchers.IO){
            return@withContext try{
                val data= getUser(uid).getOrNull()
                fireStore.collection(USER_COLLECTION)
                    .document(uid)
                    .delete()
                    .await()
                Result.success(data)
            }catch (e:Exception){
                Result.failure(e)
            }
        }

    override suspend fun saveUser(user: User): Result<Unit> =
        withContext(Dispatchers.IO){
            val uid= firebaseAuth.currentUser?.uid
                ?: return@withContext Result.failure(IllegalStateException("No Account"))
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

    override suspend fun signInUser(email: String, password: String): Result<ExternalUser?> =
        withContext(Dispatchers.IO){
            return@withContext try{
                val result= firebaseAuth.signInWithEmailAndPassword(email, password).await()
                Result.success(result.user?.let { FirebaseExternalUser(it) })
            }catch (e:Exception){
                Result.failure(e)
            }
        }

    override suspend fun signOutUser(): Result<Unit> {
        return try{
            firebaseAuth.signOut()
            Result.success(Unit)
        }catch(e:Exception){
            Result.failure(e)
        }
    }

    override suspend fun reAuthenticateUser(password: String): Result<ExternalUser> =
        withContext(Dispatchers.IO){
            val user= firebaseAuth.currentUser
                ?: return@withContext Result.failure(IllegalStateException("No Account"))
            val credential= EmailAuthProvider.getCredential(user.email?:"", password)
            return@withContext try{
                user.reauthenticate(credential).await()
                Result.success(FirebaseExternalUser(user))
            }catch (e:Exception){
                Result.failure(e)
            }
    }

    override suspend fun sendVerificationEmail(user:ExternalUser?):Result<Unit> =
        withContext(Dispatchers.IO){
            return@withContext try{
                if(user==null) firebaseAuth.currentUser?.sendEmailVerification()
                else user.sendEmailVerification()
                Result.success(Unit)
            }catch (e:Exception){
                Result.failure(e)
            }
        }

    override suspend fun isEmailVerified ():Boolean{
        firebaseAuth.currentUser?.let {
            it.reload().await()
            return it.isEmailVerified
        }
        return false
    }

    override suspend fun updatePassword(password: String): Result<Unit> =
        withContext(Dispatchers.IO){
            val user= firebaseAuth.currentUser
                ?:return@withContext Result.failure(IllegalStateException("No Account"))
            return@withContext try{
                user.updatePassword(password).await()
                Result.success(Unit)
            }catch (e:Exception){
                Result.failure(e)
            }
        }

    override suspend fun resetPassword(email: String): Result<Unit> =
        withContext(Dispatchers.IO){
            return@withContext try{
                firebaseAuth.sendPasswordResetEmail(email).await()
                Result.success(Unit)
            }catch (e:Exception){
                Result.failure(e)
            }
        }

    companion object {
        const val USER_COLLECTION="users"
    }

}