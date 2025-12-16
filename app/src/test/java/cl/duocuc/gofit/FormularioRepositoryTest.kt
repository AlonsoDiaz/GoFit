package cl.duocuc.gofit

import cl.duocuc.gofit.data.local.UserDao
import cl.duocuc.gofit.data.local.UserEntity
import cl.duocuc.gofit.repository.FormularioRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class FormularioRepositoryTest {

    private val fakeUserDao = FakeUserDao()
    private val repository = FormularioRepository(fakeUserDao)

    @Test
    fun `upsertUser persiste y recupera por correo`() = runBlocking {
        val correo = "test@example.com"
        val user = UserEntity(
            id = 0,
            nombre = "Usuario Test",
            correo = correo,
            edad = 30,
            terminosAceptados = true,
            lastLoginAt = 1000L
        )

        assertNull(repository.findUserByEmail(correo))

        repository.upsertUser(user)
        val recuperado = repository.findUserByEmail(correo)

        assertNotNull(recuperado)
        assertEquals("Usuario Test", recuperado?.nombre)
    }

    @Test
    fun `getLastUser entrega el usuario con login mas reciente`() = runBlocking {
        val primero = UserEntity(
            id = 0,
            nombre = "Primer Usuario",
            correo = "primero@example.com",
            edad = 25,
            terminosAceptados = true,
            lastLoginAt = 1_000L
        )
        val segundo = primero.copy(
            nombre = "Segundo Usuario",
            correo = "segundo@example.com",
            lastLoginAt = 5_000L
        )

        repository.upsertUser(primero)
        repository.upsertUser(segundo)

        val lastUser = repository.getLastUser()

        assertNotNull(lastUser)
        assertEquals("segundo@example.com", lastUser?.correo)
    }
}

private class FakeUserDao : UserDao {
    private val users = mutableListOf<UserEntity>()
    private var idCounter = 1L

    override suspend fun getUserByCorreo(correo: String): UserEntity? {
        return users.find { it.correo == correo }
    }

    override suspend fun upsert(user: UserEntity) {
        val index = users.indexOfFirst { it.correo == user.correo }
        val assignedId = if (index >= 0) users[index].id else idCounter++
        val entity = user.copy(id = assignedId)
        if (index >= 0) {
            users[index] = entity
        } else {
            users.add(entity)
        }
    }

    override suspend fun getLastSignedUser(): UserEntity? {
        return users.maxByOrNull { it.lastLoginAt }
    }
}
