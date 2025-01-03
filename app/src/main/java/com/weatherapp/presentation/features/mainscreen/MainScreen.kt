package com.weatherapp.presentation.features.mainscreen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.weatherapp.core.ui.components.BottomNavigationSection
import com.weatherapp.presentation.MainActivity
import com.weatherapp.presentation.navgraph.HomeNavGraph

@Composable
fun MainScreen() {
    val context = LocalContext.current as MainActivity
    val navController = rememberNavController()
    Box(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding()
            .fillMaxSize()
    ) {

        MainContent(
            modifier = Modifier.background(White),
            navController,
        )
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainContent(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {

    var tabBarVisibilityState by remember { mutableStateOf(true) }
    Scaffold(
        modifier = modifier,
        bottomBar = {
            if (tabBarVisibilityState) {
                BottomNavigationSection(
                    navController = navController,
                    onItemSelected = {
                        navController.navigate(it.route)
                    }
                )
            }
        }
    ) {
        HomeNavGraph(
            navController = navController,
            onChangeTabBarStatus = { tabBarVisibilityState = it }
        )
    }
}


