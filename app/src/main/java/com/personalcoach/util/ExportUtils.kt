package com.personalcoach.util

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.personalcoach.domain.model.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object ExportUtils {
    private val gson: Gson = GsonBuilder().setPrettyPrinting().create()

    data class ExportData(
        val exportedAt: String,
        val userProfile: UserProfile?,
        val workoutSessions: List<WorkoutSession>,
        val gutCheckIns: List<GutCheckIn>,
        val meals: List<MealLog>,
        val waterIntakes: List<WaterIntake>,
        val weightRecords: List<WeightRecord>
    )

    fun exportToJson(
        context: Context,
        userProfile: UserProfile?,
        workoutSessions: List<WorkoutSession>,
        gutCheckIns: List<GutCheckIn>,
        meals: List<MealLog>,
        waterIntakes: List<WaterIntake>,
        weightRecords: List<WeightRecord>
    ): File {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
            .format(Date())
        val exportData = ExportData(
            exportedAt = timestamp,
            userProfile = userProfile,
            workoutSessions = workoutSessions,
            gutCheckIns = gutCheckIns,
            meals = meals,
            waterIntakes = waterIntakes,
            weightRecords = weightRecords
        )
        val file = File(context.getExternalFilesDir(null), "health_export_$timestamp.json")
        file.writeText(gson.toJson(exportData))
        return file
    }

    fun exportToCsv(context: Context, meals: List<MealLog>): File {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
            .format(Date())
        val file = File(context.getExternalFilesDir(null), "meals_export_$timestamp.csv")
        val sb = StringBuilder()
        sb.appendLine("Date,MealType,Description,Calories,ProteinG,CarbsG,FatsG,FiberG")
        for (meal in meals) {
            sb.appendLine(
                "${DateUtils.formatDateTime(meal.dateTime)}," +
                "${meal.mealType}," +
                "${meal.description ?: ""}," +
                "${meal.calories}," +
                "${meal.proteinG}," +
                "${meal.carbsG}," +
                "${meal.fatsG}," +
                "${meal.fiberG ?: ""}"
            )
        }
        file.writeText(sb.toString())
        return file
    }
}
