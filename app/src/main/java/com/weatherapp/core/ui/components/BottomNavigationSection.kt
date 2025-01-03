package com.weatherapp.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.weatherapp.R
import com.weatherapp.core.ui.theme.AppFont
import com.weatherapp.presentation.navgraph.Routes

@Composable
fun BottomNavigationSection(
    navController: NavController,
    modifier: Modifier = Modifier,
    onItemSelected: (item: BottomNavItem) -> Unit
) {
    val backStackState = navController.currentBackStackEntryAsState()
    val bottomNavItems = listOf(
        BottomNavItem(
            route = Routes.WEATHER,
            title = "Weather",
            icon = Icons.Outlined.Home,
        ),
        BottomNavItem(
            route = Routes.FORECAST,
            title = "Forecast",
            icon = ImageVector.vectorResource(R.drawable.ic_forecast),
        ),
    )
    BottomNavigation(
        backgroundColor = White,
        elevation = 5.dp,
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .background(Color.Transparent)
                .fillMaxWidth(),
        ) {

            Row(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .background(Color.Transparent)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                bottomNavItems.forEach { bottomNavItem ->
                    val selected = bottomNavItem.route == backStackState.value?.destination?.route
                    BottomNavigationItem(
                        selected = selected,
                        onClick = {
                            if (!selected) onItemSelected(bottomNavItem)
                        },
                        selectedContentColor = Black,
                        unselectedContentColor = Gray,
                        icon = {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    modifier = Modifier.size(32.dp),
                                    imageVector = bottomNavItem.icon,
                                    contentDescription = bottomNavItem.title,
                                    tint = if (selected) Black else Gray
                                )
                                Text(
                                    text = bottomNavItem.title,
                                    fontSize = 12.sp,
                                    fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                                    textAlign = TextAlign.Center,
                                    fontFamily = AppFont.MontserratFont,
                                    color = if (selected) Black else Gray
                                )
                            }
                        },
                    )
                }

            }
        }
    }

}

data class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector,
)