package com.notasapp.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.notasapp.view.DetailScreen
import com.notasapp.view.FormScreen
import com.notasapp.view.HomeScreen
import com.notasapp.viewmodel.NotaViewModel

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Form : Screen("form?notaId={notaId}") {
        fun createRoute(notaId: Int? = null): String =
            if (notaId != null) "form?notaId=$notaId" else "form"
    }
    object Detail : Screen("detail/{notaId}") {
        fun createRoute(notaId: Int): String = "detail/$notaId"
    }
}

@Composable
fun AppNavigation(
    navController: NavHostController,
    viewModel: NotaViewModel = viewModel()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(navController = navController, viewModel = viewModel)
        }

        composable(
            route = Screen.Form.route,
            arguments = listOf(
                navArgument("notaId") {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) { backStackEntry ->
            val notaId = backStackEntry.arguments?.getInt("notaId") ?: -1
            FormScreen(
                navController = navController,
                viewModel = viewModel,
                notaId = if (notaId != -1) notaId else null
            )
        }

        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument("notaId") { type = NavType.IntType })
        ) { backStackEntry ->
            val notaId = backStackEntry.arguments?.getInt("notaId") ?: 0
            DetailScreen(navController = navController, viewModel = viewModel, notaId = notaId)
        }
    }
}
