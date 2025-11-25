package cl.duocuc.gofit.data.local

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "workout_history")
data class WorkoutHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val fechaMillis: Long,
    val nombreRutina: String
)

@Entity(
    tableName = "workout_exercise",
    foreignKeys = [
        ForeignKey(
            entity = WorkoutHistoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["historyId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("historyId")]
)
data class WorkoutExerciseEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val historyId: Long,
    val nombre: String
)

@Entity(
    tableName = "workout_serie",
    foreignKeys = [
        ForeignKey(
            entity = WorkoutExerciseEntity::class,
            parentColumns = ["id"],
            childColumns = ["exerciseId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("exerciseId")]
)
data class WorkoutSerieEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val exerciseId: Long,
    val numero: Int,
    val peso: String,
    val repeticiones: String
)

data class WorkoutExerciseWithSeries(
    @Embedded val exercise: WorkoutExerciseEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "exerciseId"
    )
    val series: List<WorkoutSerieEntity>
)

data class WorkoutHistoryWithExercises(
    @Embedded val history: WorkoutHistoryEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "historyId",
        entity = WorkoutExerciseEntity::class
    )
    val exercises: List<WorkoutExerciseWithSeries>
)
