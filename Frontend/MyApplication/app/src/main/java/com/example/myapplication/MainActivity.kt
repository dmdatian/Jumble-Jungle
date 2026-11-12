package com.example.myapplication

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.delay
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.material3.Button
import androidx.compose.ui.text.font.FontWeight
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import android.util.Log
import org.json.JSONObject

import kotlinx.coroutines.launch
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "loading") {
                    composable("loading") { LoadingScreen(navController) }
                    composable("game_mode_selection") { GameModeSelectionScreen(navController) }
                    composable("difficulty_selection/{playWithBot}") { backStackEntry ->
                        val playWithBot = backStackEntry.arguments?.getString("playWithBot")
                            ?.toBooleanStrictOrNull() ?: false
                        MainScreen(navController, playWithBot)
                    }
                    composable("landing/{pairCount}/{playWithBot}") { backStackEntry ->
                        val pairCount =
                            backStackEntry.arguments?.getString("pairCount")?.toIntOrNull() ?: 4
                        val playWithBot = backStackEntry.arguments?.getString("playWithBot")
                            ?.toBooleanStrictOrNull() ?: false
                        LandingScreen(navController, pairCount, playWithBot)
                    }
                    composable("game/{pairCount}/{playWithBot}") { backStackEntry ->
                        val pairCount =
                            backStackEntry.arguments?.getString("pairCount")?.toIntOrNull() ?: 4
                        val playWithBot = backStackEntry.arguments?.getString("playWithBot")
                            ?.toBooleanStrictOrNull() ?: false
                        GameScreen(navController, pairCount, playWithBot)
                    }
                    composable("win/{pairCount}/{playWithBot}") { backStackEntry ->
                        val pairCount =
                            backStackEntry.arguments?.getString("pairCount")?.toIntOrNull() ?: 4
                        val playWithBot = backStackEntry.arguments?.getString("playWithBot")
                            ?.toBooleanStrictOrNull() ?: false
                        WinScreen(navController, pairCount, playWithBot)
                    }
                    composable("stats") {
                        StatsScreen(navController)
                    }
                    composable("about") { AboutScreen(navController) }
                    composable("about2") { AboutScreen2(navController) }
                    composable("about3") { AboutScreen3(navController) }
                }
            }
        }
    }

    @Composable
    fun GameModeSelectionScreen(navController: NavController) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.modescreen),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 65.dp)
                    .offset(y = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(200.dp, Alignment.CenterHorizontally)

            ) {
                Image(
                    painter = painterResource(id = R.drawable.statsbutton),
                    contentDescription = "Stats Button",
                    modifier = Modifier
                        .requiredWidth(60.dp)
                        .requiredHeight(60.dp)
                        .clickable {
                            navController.navigate("stats")
                        }
                )
                Image(
                    painter = painterResource(id = R.drawable.aboutbutton),
                    contentDescription = "About Button",
                    modifier = Modifier
                        .requiredWidth(60.dp)
                        .requiredHeight(60.dp)
                        .clickable {
                            navController.navigate("about")
                        }
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Spacer(modifier = Modifier.height(400.dp))
                Image(
                    painter = painterResource(id = R.drawable.solobutton),
                    contentDescription = "Solo Button",
                    modifier = Modifier
                        .requiredWidth(300.dp)
                        .requiredHeight(100.dp)
                        .clickable {
                            navController.navigate("difficulty_selection/false")
                        }
                )
                Spacer(modifier = Modifier.height(25.dp))
                Image(
                    painter = painterResource(id = R.drawable.botbutton),
                    contentDescription = "Bot Button",
                    modifier = Modifier
                        .requiredWidth(300.dp)
                        .requiredHeight(100.dp)
                        .clickable {
                            navController.navigate("difficulty_selection/true")
                        }
                )
            }
        }
    }

    @Composable
    fun AboutScreen(navController: NavController) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.slide1),
                contentDescription = "About Screen Background",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 125.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                Image(
                    painter = painterResource(id = R.drawable.nextabout),
                    contentDescription = "Next Button",
                    modifier = Modifier
                        .requiredWidth(200.dp)
                        .requiredHeight(64.dp)
                        .offset(y = -68.dp)
                        .clickable {
                            navController.navigate("about2")
                        }
                )
                Image(
                    painter = painterResource(id = R.drawable.closeabouts),
                    contentDescription = "Close Button",
                    modifier = Modifier
                        .requiredWidth(200.dp)
                        .requiredHeight(57.dp)
                        .offset(y = -13.dp)
                        .clickable {
                            navController.navigate("game_mode_selection")
                        }
                )
            }
        }
    }
    @Composable
    fun AboutScreen2(navController: NavController) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.slide2),
                contentDescription = "Slide 2 Background",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 125.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                Image(
                    painter = painterResource(id = R.drawable.nextabout),
                    contentDescription = "Next Button",
                    modifier = Modifier
                        .requiredWidth(200.dp)
                        .requiredHeight(64.dp)
                        .offset(y = -68.dp)
                        .clickable {
                            navController.navigate("about3")
                        }
                )
                Image(
                    painter = painterResource(id = R.drawable.closeabouts),
                    contentDescription = "Close Button",
                    modifier = Modifier
                        .requiredWidth(200.dp)
                        .requiredHeight(57.dp)
                        .offset(y = -13.dp)
                        .clickable {
                            navController.navigate("game_mode_selection")
                        }
                )
            }
        }
    }
    @Composable
    fun AboutScreen3(navController: NavController) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.slide3),
                contentDescription = "Slide 2 Background",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 120.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                Image(
                    painter = painterResource(id = R.drawable.closeaboutl),
                    contentDescription = "Close Button",
                    modifier = Modifier
                        .requiredWidth(200.dp)
                        .requiredHeight(64.dp)
                        .offset(y = -70.dp)
                        .clickable {
                            navController.navigate("game_mode_selection")
                        }
                )
            }

        }
    }

    @Composable
    fun LoadingScreen(navController: NavController) {
        LaunchedEffect(Unit) {
            delay(2000)
            navController.navigate("game_mode_selection") {
                popUpTo("loading") { inclusive = true }
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.loading),
                contentDescription = "Loading Background",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }

    @Composable
    fun MainScreen(navController: NavController, playWithBot: Boolean) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.mainscreen),
                contentDescription = "Main Screen",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 180.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.easybutton),
                    contentDescription = "Easy Button",
                    modifier = Modifier
                        .requiredWidth(300.dp)
                        .requiredHeight(100.dp)
                        .clickable {
                            navController.navigate("landing/4/$playWithBot")
                        }
                )
                Image(
                    painter = painterResource(id = R.drawable.mediumbutton),
                    contentDescription = "Medium Button",
                    modifier = Modifier
                        .requiredWidth(300.dp)
                        .requiredHeight(100.dp)
                        .clickable {
                            navController.navigate("landing/6/$playWithBot")
                        }
                )
                Image(
                    painter = painterResource(id = R.drawable.hardbutton),
                    contentDescription = "Hard Button",
                    modifier = Modifier
                        .requiredWidth(300.dp)
                        .requiredHeight(100.dp)
                        .clickable {
                            navController.navigate("landing/10/$playWithBot")
                        }
                )
            }
        }
    }

    data class Card(
        val id: Int,
        val imageName: String,
        var isFlipped: Boolean = false,
        var isMatched: Boolean = false
    )

    class GameManager(private val pairCount: Int) {
        fun generateCards(): List<Card> {
            val allAnimals = listOf(
                "bear", "bunny", "deer", "elephant", "fox", "giraffe",
                "horse", "lion", "monkey", "owl", "panda", "penguin",
                "pig", "tiger", "zebra"
            )
            val selectedImages = allAnimals.shuffled().take(pairCount)
            val pairedImages = (selectedImages + selectedImages).shuffled()
            return pairedImages.mapIndexed { index, name -> Card(id = index, imageName = name) }
        }

        fun getImageRes(context: android.content.Context, name: String): Int {
            return context.resources.getIdentifier(name, "drawable", context.packageName)
        }
    }

    @Composable
    fun GameScreen(navController: NavController, pairCount: Int, playWithBot: Boolean) {
        val context = LocalContext.current
        val manager = remember { GameManager(pairCount) }
        var cards by remember { mutableStateOf(manager.generateCards()) }
        var flippedCards by remember { mutableStateOf(listOf<Card>()) }
        var matchedPairs by remember { mutableStateOf(0) }
        var playerScore by remember { mutableStateOf(0) }
        var botScore by remember { mutableStateOf(0) }
        var playerTurn by remember { mutableStateOf(true) }
        var isBotThinking by remember { mutableStateOf(false) }

        val initialTime = when (pairCount) {
            4 -> 60
            6 -> 75
            10 -> 90
            else -> 60
        }
        var timeLeft by rememberSaveable { mutableStateOf(initialTime) }
        var timerRunning by remember { mutableStateOf(true) }

        val isGameOver = matchedPairs == pairCount || timeLeft == 0
        val isWon = matchedPairs == pairCount
        var scoreSaved by remember { mutableStateOf(false) }

        val coroutineScope = rememberCoroutineScope()

        LaunchedEffect(timerRunning, isGameOver) {
            if (timerRunning && !isGameOver) {
                while (timeLeft > 0) {
                    delay(1000)
                    timeLeft--
                }
                timerRunning = false
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = manager.getImageRes(context, "gridbg")),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (isGameOver) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            when {
                                playWithBot && playerScore > botScore -> "🎉 You Win! 🎉"
                                playWithBot && playerScore < botScore -> "You Lose!"
                                playWithBot && playerScore == botScore -> "It's a Tie!"
                                isWon -> "🎉 Congratulations 🎉"
                                else -> "Time's Up!"
                            },
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = when {
                                playWithBot && playerScore == botScore -> Color(0xFFFFA500)
                                isWon -> Color.Green
                                else -> Color.Red
                            }
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            if (playWithBot) "You: $playerScore | Bot: $botScore"
                            else "Score: ${
                                playerScore * when (pairCount) {
                                    4 -> 5
                                    6 -> 3
                                    10 -> 1
                                    else -> 1
                                }
                            }",
                            fontSize = 22.sp
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        if ((playWithBot && (playerScore >= botScore)) || (!playWithBot && isWon)) {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(24.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.continuebutton),
                                    contentDescription = "Continue Button",
                                    modifier = Modifier
                                        .requiredWidth(300.dp)
                                        .requiredHeight(100.dp)
                                        .clickable {
                                            cards = manager.generateCards()
                                            flippedCards = emptyList()
                                            matchedPairs = 0
                                            playerTurn = true
                                            isBotThinking = false
                                            timeLeft = when (pairCount) {
                                                4 -> 60
                                                6 -> 76
                                                10 -> 90
                                                else -> 60
                                            }
                                            timerRunning = true
                                        }
                                )

                                Image(
                                    painter = painterResource(id = R.drawable.backbutton),
                                    contentDescription = "Back Button",
                                    modifier = Modifier
                                        .requiredWidth(300.dp)
                                        .requiredHeight(100.dp)
                                        .clickable {
                                            if (!scoreSaved) {
                                                coroutineScope.launch {
                                                    saveScoreAndGetStats(
                                                        difficulty = when (pairCount) {
                                                            4 -> "easy"
                                                            6 -> "medium"
                                                            10 -> "hard"
                                                            else -> "unknown"
                                                        },
                                                        time = initialTime - timeLeft,
                                                        score = playerScore * when (pairCount) {
                                                            4 -> 5
                                                            6 -> 3
                                                            10 -> 1
                                                            else -> 1
                                                        },
                                                        gameMode = if (playWithBot) "bot" else "solo",
                                                        result = if (playWithBot) {
                                                            when {
                                                                playerScore > botScore -> "win"
                                                                playerScore < botScore -> "lose"
                                                                else -> "tie"
                                                            }
                                                        } else {
                                                            if (matchedPairs == pairCount) "win" else "lose"
                                                        }
                                                    )
                                                }
                                                scoreSaved = true
                                            }
                                            navController.navigate("game_mode_selection") {
                                                popUpTo("game_mode_selection") { inclusive = true }
                                            }
                                        }
                                )
                            }
                        } else {
                            Image(
                                painter = painterResource(id = R.drawable.backbutton),
                                contentDescription = "Back Button",
                                modifier = Modifier
                                    .requiredWidth(300.dp)
                                    .requiredHeight(100.dp)
                                    .clickable {
                                        if (!scoreSaved) {
                                            coroutineScope.launch {
                                                saveScoreAndGetStats(
                                                    difficulty = when (pairCount) {
                                                        4 -> "easy"
                                                        6 -> "medium"
                                                        10 -> "hard"
                                                        else -> "unknown"
                                                    },
                                                    time = initialTime - timeLeft,
                                                    score = playerScore * when (pairCount) {
                                                        4 -> 5
                                                        6 -> 3
                                                        10 -> 1
                                                        else -> 1
                                                    },
                                                    gameMode = if (playWithBot) "bot" else "solo",
                                                    result = "lose"
                                                )
                                            }
                                            scoreSaved = true
                                        }
                                        navController.navigate("game_mode_selection") {
                                            popUpTo("game_mode_selection") { inclusive = true }
                                        }
                                    }
                            )
                        }
                    }
                } else {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            "Time Left: $timeLeft sec",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(0.8f),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Player: $playerScore", fontSize = 20.sp)
                            if (playWithBot) {
                                Text("Bot: $botScore", fontSize = 20.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        LazyVerticalGrid(
                            columns = GridCells.Fixed(if (pairCount == 8) 5 else 4),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalArrangement = Arrangement.spacedBy(20.dp),
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .padding(horizontal = 16.dp)
                        ) {
                            items(cards, key = { it.id }) { card ->
                                CardItem(card = card, onClick = {
                                    if (!card.isFlipped && !card.isMatched &&
                                        flippedCards.size < 2 && !isBotThinking &&
                                        (!playWithBot || playerTurn)
                                    ) {
                                        cards = cards.map {
                                            if (it.id == card.id) it.copy(isFlipped = true) else it
                                        }
                                        flippedCards = flippedCards + card
                                    }
                                }, manager = manager)
                            }
                        }
                    }
                }
            }

            IconButton(
                onClick = { navController.navigate("game_mode_selection") },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }
        }

        LaunchedEffect(flippedCards) {
            if (flippedCards.size == 2) {
                delay(500)
                val (first, second) = flippedCards
                if (first.imageName == second.imageName) {
                    cards = cards.map {
                        if (it.id == first.id || it.id == second.id) it.copy(isMatched = true) else it
                    }
                    matchedPairs++
                    if (playWithBot) {
                        if (playerTurn) playerScore++ else botScore++
                    } else {
                        playerScore++
                    }
                } else {
                    cards = cards.map {
                        if (it.id == first.id || it.id == second.id) it.copy(isFlipped = false) else it
                    }
                    if (playWithBot) {
                        playerTurn = !playerTurn
                    }
                }
                flippedCards = listOf()
            }
        }

        LaunchedEffect(flippedCards, playerTurn) {
            if (playWithBot && !playerTurn && !isGameOver && flippedCards.size < 2) {
                isBotThinking = true
                delay(500)
                val availableCards = cards.filter { !it.isFlipped && !it.isMatched }
                val botCard = availableCards.randomOrNull()
                botCard?.let {
                    cards = cards.map { if (it.id == botCard.id) it.copy(isFlipped = true) else it }
                    flippedCards = flippedCards + botCard
                }
                isBotThinking = false
            }
        }
    }

    @Composable
    fun CardItem(card: Card, onClick: () -> Unit, manager: GameManager) {
        val context = LocalContext.current
        Box(
            modifier = Modifier
                .aspectRatio(1f)
                .clip(RoundedCornerShape(12.dp))
                .clickable(enabled = !card.isMatched, onClick = onClick)
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            if (card.isFlipped || card.isMatched) {
                Image(
                    painter = painterResource(id = manager.getImageRes(context, card.imageName)),
                    contentDescription = card.imageName,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Image(
                    painter = painterResource(id = manager.getImageRes(context, "backcard")),
                    contentDescription = "Back of card",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }

    @Composable
    fun LandingScreen(navController: NavController, pairCount: Int, playWithBot: Boolean) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.gridbg),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Memory Game", fontSize = 32.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(24.dp))
                Image(
                    painter = painterResource(id = R.drawable.startbutton),
                    contentDescription = "Solo Button",
                    modifier = Modifier
                        .requiredWidth(300.dp)
                        .requiredHeight(100.dp)
                        .clickable {
                            navController.navigate("game/$pairCount/$playWithBot")
                        }
                )
            }
        }
    }

    @Composable
    fun StatsScreen(navController: NavController) {
        var stats by remember { mutableStateOf<Map<String, Pair<Int, Int>>?>(null) }
        var wins by remember { mutableStateOf(0) }
        var losses by remember { mutableStateOf(0) }

        LaunchedEffect(Unit) {
            val client = OkHttpClient()
            val request = Request.Builder()
                .url("http://192.168.1.4/jumblejungle/get_best_stats.php")
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.e("STATS", "Failed: ${e.message}")
                }

                override fun onResponse(call: Call, response: Response) {
                    val json = response.body?.string()
                    try {
                        val obj = JSONObject(json)

                        stats = mapOf(
                            "easy" to Pair(obj.getJSONObject("easy").optInt("best_score", 0), obj.getJSONObject("easy").optInt("best_time", 0)),
                            "medium" to Pair(obj.getJSONObject("medium").optInt("best_score", 0), obj.getJSONObject("medium").optInt("best_time", 0)),
                            "hard" to Pair(obj.getJSONObject("hard").optInt("best_score", 0), obj.getJSONObject("hard").optInt("best_time", 0))
                        )
                        wins = obj.optInt("wins", 0)
                        losses = obj.optInt("losses", 0)
                    } catch (e: Exception) {
                        Log.e("STATS", "Parse error: ${e.message}")
                    }
                }
            })
            val botStatsRequest = Request.Builder()
                .url("http://192.168.1.4/jumblejungle/get_bot_stats.php")
                .build()

            client.newCall(botStatsRequest).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.e("BOT_STATS", "Failed: ${e.message}")
                }

                override fun onResponse(call: Call, response: Response) {
                    val json = response.body?.string()
                    try {
                        val obj = JSONObject(json)
                        val winCount = obj.optInt("wins", 0)
                        val lossCount = obj.optInt("losses", 0)

                        Handler(Looper.getMainLooper()).post {
                            wins = winCount
                            losses = lossCount
                        }
                    } catch (e: Exception) {
                        Log.e("BOT_STATS", "Parse error: ${e.message}")
                    }
                }

            })

        }

        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.gamestats),
                contentDescription = "Stats Background",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 90.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(60.dp))
                ScoreRow("easy", stats, modifier = Modifier)

                ScoreRow("medium", stats, modifier = Modifier.offset(y = (-80).dp))

                ScoreRow("hard", stats, modifier = Modifier.offset(y = (-75).dp))

                BotStatsRow(wins = wins, losses = losses)

                Image(
                    painter = painterResource(id = R.drawable.backstats),
                    contentDescription = "b button",
                    modifier = Modifier
                        .requiredWidth(220.dp)
                        .requiredHeight(100.dp)
                        .offset(y = 20.dp)
                        .clickable {
                            navController.popBackStack()
                        }

                )
            }
        }
    }

    @Composable
    fun BotStatsRow(wins: Int, losses: Int) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 50.dp, end = 65.dp),
            contentAlignment = Alignment.TopEnd
        ) {
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "$wins",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "$losses",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }

    @Composable
    fun ScoreRow(level: String, stats: Map<String, Pair<Int, Int>>?, modifier: Modifier) {
        val (score, time) = stats?.get(level) ?: (0 to 0)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 50.dp, end = 65.dp),
            contentAlignment = Alignment.TopEnd
        ) {
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    "$score",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    "${time}s",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }

    @Composable
    fun WinScreen(navController: NavController, pairCount: Int, playWithBot: Boolean) {
        var playerScore by remember { mutableStateOf(0) }
        val coroutineScope = rememberCoroutineScope()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFE0F7FA))
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "🎉 You Win! 🎉",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF00796B)
            )
            Spacer(modifier = Modifier.height(32.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(onClick = {
                    navController.navigate("game/$pairCount/$playWithBot") {
                        popUpTo("game/$pairCount/$playWithBot") { inclusive = true }
                    }
                }) {
                    Text("Continue")
                }
                Button(onClick = {
                    coroutineScope.launch {
                        saveScoreAndGetStats(
                            difficulty = when (pairCount) {
                                4 -> "easy"
                                6 -> "medium"
                                10 -> "hard"
                                else -> "unknown"
                            },
                            time = 0,
                            score = playerScore * when (pairCount) {
                                4 -> 5
                                6 -> 3
                                10 -> 1
                                else -> 1
                            },
                            gameMode = if (playWithBot) "bot" else "solo",
                            result = "win"
                        )
                    }
                    navController.navigate("game_mode_selection") {
                        popUpTo("game_mode_selection") { inclusive = true }
                    }
                }) {
                    Text("Back")
                }
            }
        }
    }

    suspend fun saveScoreAndGetStats(
        difficulty: String,
        time: Int,
        score: Int,
        gameMode: String,
        result: String
    ) {
        val saveScoreUrl = "http://192.168.1.4/jumblejungle/save_score.php"
        val getStatsUrl = "http://192.168.1.4/jumblejungle/get_bot_stats.php"

        val client = HttpClient(CIO)

        try {
            val saveResponse: HttpResponse = client.submitForm(
                url = saveScoreUrl,
                formParameters = Parameters.build {
                    append("difficulty", difficulty)
                    append("time", time.toString())
                    append("score", score.toString())
                    append("game_mode", gameMode)
                    append("result", result)
                }
            )
            val saveResult = saveResponse.bodyAsText()
            println("SAVE response: $saveResult")

            val statsResponse: HttpResponse = client.get(getStatsUrl)
            val statsJson = statsResponse.bodyAsText()
            println("STATS raw response: $statsJson")

            if (statsJson.isNotBlank()) {
                try {
                    val stats = JSONObject(statsJson)
                    val wins = stats.optInt("wins", 0)
                    val losses = stats.optInt("losses", 0)
                    println("Wins: $wins, Losses: $losses")
                } catch (e: Exception) {
                    println("STATS JSON parse error: ${e.message}")
                }
            } else {
                println("STATS: Empty or null response received.")
            }
        } catch (e: Exception) {
            println("Request error: ${e.message}")
        } finally {
            client.close()
        }
    }
}
