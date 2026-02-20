package com.personalcoach.presentation.navigation

sealed class NavRoutes(val route: String) {
    object Onboarding : NavRoutes("onboarding")
    object Home : NavRoutes("home")
    object Workout : NavRoutes("workout")
    object WorkoutHistory : NavRoutes("workout_history")
    object GutCheckIn : NavRoutes("gut_check_in")
    object GutHistory : NavRoutes("gut_history")
    object MealLog : NavRoutes("meal_log")
    object NutritionSummary : NavRoutes("nutrition_summary")
    object Water : NavRoutes("water")
    object Weight : NavRoutes("weight")
    object Analytics : NavRoutes("analytics")
    object Settings : NavRoutes("settings")
}
