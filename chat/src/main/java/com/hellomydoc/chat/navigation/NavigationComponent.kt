package com.hellomydoc.chat.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.hellomydoc.chat.presentation.screens.ChatRoute

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavigationComponent(
    navHostController: NavHostController,
    paddingValues: PaddingValues,
    data: Any? = null
) {
    AnimatedNavHost(
        navController = navHostController,
        startDestination = ChatRoute.route,
        modifier = Modifier.padding(paddingValues)
    ) {
        ChatRoute.composable(this, navHostController, data)
        /*ChatRoute.composable(
            this, navHostController
        )*/
    }
}