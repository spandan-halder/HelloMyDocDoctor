package com.hellomydoc.chat.presentation.screens


import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.hellomydoc.chat.presentation.viewModels.ChatViewModel
import com.hellomydoc.chat.navigation.NavRoute
import com.hellomydoc.chat.navigation.Routes
import com.hellomydoc.chat.presentation.userInterface.UserInterface


object ChatRoute : NavRoute<ChatViewModel> {

    override val route = Routes.Chat

    @Composable
    override fun viewModel(): ChatViewModel = hiltViewModel()

    @Composable
    override fun Content(viewModel: ChatViewModel, data: Any?) = ChatPage(viewModel.apply {
        setData(data)
    })

    override fun getArguments(): List<NamedNavArgument> {
        return listOf(
            navArgument("userId") { type = NavType.StringType },
            navArgument("peerId") { type = NavType.StringType }
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ChatPage(
    viewModel: ChatViewModel
) {
    LifecycleEvent(viewModel)
    UserInterface(viewModel)
    ActivityControl(viewModel)
}

@Composable
fun LifecycleEvent(viewModel: ChatViewModel) {
    DisposableEffect(key1 = viewModel) {
        viewModel.onStart()
        onDispose { viewModel.onStop() }
    }
}
@Composable
fun ActivityControl(viewModel: ChatViewModel) {
    val context = LocalContext.current
    if(viewModel.finish.value){
        SideEffect {
            (context as Activity).finish()
        }
    }
    BackHandler(enabled = true) {
        if(!viewModel.onBackClick()){
            (context as Activity).finish()
        }
    }
}
//////////////////////////////////////



