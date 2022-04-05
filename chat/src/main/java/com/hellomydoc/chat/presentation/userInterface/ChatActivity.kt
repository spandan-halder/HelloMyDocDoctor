package com.hellomydoc.chat.presentation.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.*
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.hellomydoc.chat.AppDelegate
import com.hellomydoc.chat.navigation.NavigationComponent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatActivity : ComponentActivity() {
    data class UserIds(
        val userId: String,
        val peerId: String
    )
    val appDelegate: AppDelegate
        get(){
            return this.application as AppDelegate
        }
    private lateinit var navController: NavHostController
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val peerId = intent?.getStringExtra(appDelegate.peerIdKey)?:""
        val userId = intent?.getStringExtra(appDelegate.myIdKey)?:""
        setContent {
            navController = rememberAnimatedNavController()
            Scaffold {
                NavigationComponent(navController, it, UserIds(userId, peerId))
            }
        }
    }
}