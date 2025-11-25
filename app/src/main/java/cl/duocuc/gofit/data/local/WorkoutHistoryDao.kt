package cl.duocuc.gofit.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutHistoryDao {
    @Transaction
    @Query("SELECT * FROM workout_history ORDER BY fechaMillis DESC")
    fun observeWorkoutHistory(): Flow<List<WorkoutHistoryWithExercises>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(history: WorkoutHistoryEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercises(exercises: List<WorkoutExerciseEntity>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSeries(series: List<WorkoutSerieEntity>)

    @Query("DELETE FROM workout_history")
    suspend fun clearAll()
}
