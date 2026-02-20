package com.personalcoach.util

object Constants {
    // Notification channels
    const val CHANNEL_REMINDERS = "channel_reminders"
    const val CHANNEL_HEALTH_SYNC = "channel_health_sync"

    // Worker tags
    const val WORKER_GUT_REMINDER = "worker_gut_reminder"
    const val WORKER_WATER_REMINDER = "worker_water_reminder"
    const val WORKER_WEIGHT_REMINDER = "worker_weight_reminder"
    const val WORKER_NUTRITION_REVIEW = "worker_nutrition_review"
    const val WORKER_HEALTH_SYNC = "worker_health_sync"

    // Notification IDs
    const val NOTIFICATION_GUT_REMINDER = 1001
    const val NOTIFICATION_WATER_REMINDER = 1002
    const val NOTIFICATION_WEIGHT_REMINDER = 1003
    const val NOTIFICATION_NUTRITION_REVIEW = 1004

    // Meal types
    val MEAL_TYPES = listOf("Breakfast", "Lunch", "Dinner", "Snack")

    // Workout categories
    val WORKOUT_CATEGORIES = listOf(
        "Strength", "Cardio", "HIIT", "Yoga", "Pilates",
        "Swimming", "Cycling", "Running", "Sports", "Other"
    )

    // Default gut symptoms
    val DEFAULT_GUT_SYMPTOMS = listOf(
        "Bloating", "Gas", "Cramping", "Diarrhea", "Constipation",
        "Nausea", "Heartburn", "Reflux", "Pain", "Urgency"
    )

    // Primary goals
    val PRIMARY_GOALS = listOf(
        "Weight Loss", "Muscle Gain", "Improve Fitness",
        "Better Gut Health", "Increase Energy", "Better Sleep"
    )
}
