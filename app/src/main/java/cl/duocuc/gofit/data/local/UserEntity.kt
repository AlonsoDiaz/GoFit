package cl.duocuc.gofit.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "users",
    indices = [Index(value = ["correo"], unique = true)]
)
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nombre: String,
    val correo: String,
    val edad: Int,
    val terminosAceptados: Boolean,
    val lastLoginAt: Long = System.currentTimeMillis()
)
