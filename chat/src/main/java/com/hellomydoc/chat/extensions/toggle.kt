package com.hellomydoc.chat

import androidx.compose.runtime.MutableState

fun MutableState<Boolean>.toggle(){
        this.value = !this.value
    }