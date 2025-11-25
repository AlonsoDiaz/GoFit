package cl.duocuc.gofit.data.remote

import com.google.gson.annotations.SerializedName

data class RoutineDto(
    val id: Long,
    @SerializedName("created_at") val createdAt: Long?,
    @SerializedName("user_id") val userId: Long?,
    val name: String,
    val description: String?,
    val difficulty: String?,
    @SerializedName("training_type") val trainingType: String?,
    @SerializedName("publication_date") val publicationDate: String?
)
