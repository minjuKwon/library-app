package com.example.library.fake

import android.util.Patterns
import com.example.library.domain.ExternalUser
import com.example.library.data.entity.User
import com.example.library.data.repository.FirebaseException
import com.example.library.domain.UserRepository

class FakeUserRepository: UserRepository {

    private val authList= mutableListOf<AuthUser>()
    private val storeList= mutableListOf<User>()
    private val pattern= Patterns.EMAIL_ADDRESS

    private var currentUser= CurrentUser(User())
    private var verificationCnt=0

    companion object{
        const val NEW_PASSWORD="000000"
    }

    override suspend fun createUser(email: String, password: String): Result<ExternalUser?> {
        return try {
            if(!pattern.matcher(email).matches())
                return Result.failure(FirebaseException("ERROR_INVALID_EMAIL"))
            if(authList.any { it.email==email })
                return Result.failure(FirebaseException("ERROR_EMAIL_ALREADY_IN_USE"))
            if(password.length<6)
                return Result.failure(FirebaseException("ERROR_WEAK_PASSWORD"))

            val user=AuthUser(email+password,email, password)
            authList.add(user)

            Result.success(FakeExternalUser(uid=user.uid,authList=authList))
        }catch(e:Exception){
            Result.failure(e)
        }
    }

    override suspend fun deleteUserAccount(user: ExternalUser?): Result<Unit> {
        return try{
            user?.delete()

            verificationCnt=0
            currentUser= CurrentUser(User())

            Result.success(Unit)
        }catch (e:Exception){
            Result.failure(e)
        }
    }

    override suspend fun getUser(uid: String): Result<User> {
        return try{
            val userData = authList.find { it.uid == uid }
            val user= storeList.find{it.email== userData?.email}

            if(user==null){
                Result.failure((IllegalStateException("No user data")))
            }else{
                Result.success(user)
            }
        }catch (e:Exception){
            Result.failure(e)
        }
    }

    override suspend fun updateUser(data: Map<String, Any>): Result<Unit> {
        return try{
            if(currentUser.user.email.isEmpty())
                return Result.failure(IllegalStateException("No Account"))

            val index = storeList.indexOfFirst { it.email == currentUser.user.email }
            if(index==-1) return Result.failure(IllegalStateException("No Account"))

            storeList[index]= storeList[index].copy(name = data["name"].toString())
            currentUser= currentUser.copy(user = currentUser.user.copy(name=data["name"].toString()))

            Result.success(Unit)
        }catch (e:Exception){
            Result.failure(e)
        }
    }

    override suspend fun deleteUserData(uid: String): Result<User?> {
        return try{
            val userData = authList.find { it.uid == uid }
            val user= storeList.find{it.email== userData?.email}

            storeList.remove(user)
            currentUser=CurrentUser(User())

            Result.success(user)
        }catch (e:Exception){
            Result.failure(e)
        }
    }

    override suspend fun saveUser(user: User): Result<Unit> {
        return try{
            storeList.add(user)
            currentUser= currentUser.copy(user=user)

            Result.success(Unit)
        }catch (e:Exception){
            Result.failure(e)
        }
    }

    override suspend fun signInUser(email: String, password: String): Result<ExternalUser?> {
        return try{
            if(!pattern.matcher(email).matches())
                return Result.failure(FirebaseException("ERROR_INVALID_EMAIL"))

            val user= authList.find{it.email== email}

            if(user?.email!= email || user.password!= password)
                return Result.failure(FirebaseException("ERROR_INVALID_CREDENTIAL"))

            val userData= storeList.find { it.email==email }
            if (userData != null) {
                currentUser= currentUser.copy(user= userData)
            }

            Result.success(FakeExternalUser(user.uid, currentUser.isEmailVerified, authList))
        }catch (e:Exception){
            Result.failure(e)
        }
    }

    override suspend fun signOutUser(): Result<Unit> {
        return try {
            currentUser= currentUser.copy(user= User())

            Result.success(Unit)
        }catch (e:Exception){
            Result.failure(e)
        }
    }

    override suspend fun reAuthenticateUser(password: String): Result<ExternalUser> {
        return try{
            if(currentUser.user.email.isEmpty())
                return Result.failure(IllegalStateException("No Account"))

            val user= authList.find { it.email== currentUser.user.email && it.password== password }

            if(user==null){
                return Result.failure(FirebaseException("ERROR_INVALID_CREDENTIAL"))
            }else{
                Result.success(FakeExternalUser(uid= user.uid,authList=authList))
            }
        }catch (e:Exception){
            Result.failure(e)
        }
    }

    override suspend fun sendVerificationEmail(user: ExternalUser?): Result<Unit> {
        return try{
            verificationCnt++
            if(verificationCnt>1) currentUser.isEmailVerified=true

            Result.success(Unit)
        }catch (e:Exception){
            Result.failure(e)
        }
    }

    override suspend fun isEmailVerified(): Boolean {
        return currentUser.isEmailVerified
    }

    override suspend fun updatePassword(password: String): Result<Unit> {
        return try{
            if(currentUser.user.email.isEmpty())
                return Result.failure(IllegalStateException("No Account"))

            if(password.length<6)
                return Result.failure(FirebaseException("ERROR_WEAK_PASSWORD"))

            val index= authList.indexOfFirst { it.email== currentUser.user.email }
            if(index==-1) return Result.failure(IllegalStateException("No Account"))

            authList[index]= authList[index].copy(password=password)

            Result.success(Unit)
        }catch (e:Exception){
            Result.failure(e)
        }
    }

    override suspend fun resetPassword(email: String): Result<Unit> {
        return try{
            val index= authList.indexOfFirst { it.email== email }
            if(index==-1) return Result.failure(IllegalStateException("No Account"))

            authList[index]= authList[index].copy(password=NEW_PASSWORD)

            Result.success(Unit)
        }catch (e:Exception){
            Result.failure(e)
        }
    }
}

data class AuthUser(
    val uid:String,
    val email: String,
    val password: String
)

data class CurrentUser(
    val user: User,
    var isEmailVerified: Boolean= false
)