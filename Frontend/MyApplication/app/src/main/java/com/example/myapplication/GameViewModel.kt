package com.example.myapplication

import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {
    var highScore = mutableIntStateOf(0)
}
