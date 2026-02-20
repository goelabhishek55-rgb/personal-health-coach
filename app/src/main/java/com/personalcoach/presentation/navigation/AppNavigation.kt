package com.personalcoach.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.personalcoach.presentation.analytics.AnalyticsScreen
import com.personalcoach.presentation.gut.GutCheckInScreen
import com.personalcoach.presentation.gut.GutHistoryScreen
import com.personalcoach.presentation.home.HomeScreen
import com.personalcoach.presentation.home.HomeViewModel
import com.personalcoach.presentation.nutrition.MealLogScreen
import com.personalcoach.presentation.nutrition.NutritionSummaryScreen
import com.personalcoach.presentation.onboarding.OnboardingScreen
import com.personalcoach.presentation.settings.SettingsScreen
import com.personalcoach.presentation.water.WaterScreen
import com.personalcoach.presentation.weight.WeightScreen
import com.personalcoach.presentation.workout.WorkoutHistoryScreen
import com.personalcoach.presentation.workout.WorkoutScreen

data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val route: String
)

val bottomNavItems = listOf(
    BottomNavItem("Home", Icons.Default.Home, NavRoutes.Home.route),
    BottomNavItem("Workouts", Icons.Default.FitnessCenter, NavRoutes.Workout.route),
    BottomNavItem("Nutrition", Icons.Default.Restaurant, NavRoutes.MealLog.route),
    BottomNavItem("Gut", Icons.Default.Spa, NavRoutes.GutCheckIn.route),
    BottomNavItem("Analytics", Icons.Default.BarChart, NavRoutes.Analytics.route)
)

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val homeViewModel: HomeViewModel = hiltViewModel()
    val onboardingCompleted by homeViewModel.onboardingCompleted.collectAsState()

    val startDestination = if (onboardingCompleted == true) NavRoutes.Home.route
    else NavRoutes.Onboarding.route

    val bottomNavRoutes = bottomNavItems.map { it.route }
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStack?.destination
    val showBottomBar = currentDestination?.route in bottomNavRoutes

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) },
                            selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(NavRoutes.Onboarding.route) {
                OnboardingScreen(onFinished = {
                    navController.navigate(NavRoutes.Home.route) {
                        popUpTo(NavRoutes.Onboarding.route) { inclusive = true }
                    }
                })
            }
            composable(NavRoutes.Home.route) {
                HomeScreen(
                    onNavigateToWorkout = { navController.navigate(NavRoutes.Workout.route) },
                    onNavigateToGut = { navController.navigate(NavRoutes.GutCheckIn.route) },
                    onNavigateToMeal = { navController.navigate(NavRoutes.MealLog.route) },
                    onNavigateToWeight = { navController.navigate(NavRoutes.Weight.route) },
                    onNavigateToWater = { navController.navigate(NavRoutes.Water.route) },
                    onNavigateToSettings = { navController.navigate(NavRoutes.Settings.route) }
                )
            }
            composable(NavRoutes.Workout.route) {
                WorkoutScreen(onNavigateToHistory = { navController.navigate(NavRoutes.WorkoutHistory.route) })
            }
            composable(NavRoutes.WorkoutHistory.route) {
                WorkoutHistoryScreen(onBack = { navController.popBackStack() })
            }
            composable(NavRoutes.GutCheckIn.route) {
                GutCheckInScreen(onNavigateToHistory = { navController.navigate(NavRoutes.GutHistory.route) })
            }
            composable(NavRoutes.GutHistory.route) {
                GutHistoryScreen(onBack = { navController.popBackStack() })
            }
            composable(NavRoutes.MealLog.route) {
                MealLogScreen(onNavigateToSummary = { navController.navigate(NavRoutes.NutritionSummary.route) })
            }
            composable(NavRoutes.NutritionSummary.route) {
                NutritionSummaryScreen(onBack = { navController.popBackStack() })
            }
            composable(NavRoutes.Water.route) {
                WaterScreen(onBack = { navController.popBackStack() })
            }
            composable(NavRoutes.Weight.route) {
                WeightScreen(onBack = { navController.popBackStack() })
            }
            composable(NavRoutes.Analytics.route) {
                AnalyticsScreen()
            }
            composable(NavRoutes.Settings.route) {
                SettingsScreen(onBack = { navController.popBackStack() })
            }
        }
    }
}
