package cl.duocuc.gofit.repository

import cl.duocuc.gofit.data.local.UserDao
import cl.duocuc.gofit.data.local.UserEntity

class FormularioRepository(
    private val userDao: UserDao
) {

    suspend fun findUserByEmail(correo: String): UserEntity? {
        return userDao.getUserByCorreo(correo)
    }

    suspend fun upsertUser(user: UserEntity) {
        userDao.upsert(user)
    }

    suspend fun getLastUser(): UserEntity? {
        return userDao.getLastSignedUser()
    }
}