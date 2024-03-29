package com.example.finallabmad

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.finallabmad.ui.theme.FinalLabMADTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FinalLabMADTheme {

                EconoTrackeApp()

            }
        }
    }
}

@Composable
fun EconoTrackeApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "splash_screen") {
        composable("splash_screen") { SplashScreen(navController) }
        composable("main_screen") { MainScreen(navController) }
        composable("overview_screen") { OverviewScreen(navController) }
        composable("add_transaction_screen") { AddTransactionScreen(navController) }
    }
}


@Composable
fun SplashScreen(navController: NavController) {
    LaunchedEffect(Unit) {
        delay(3000)
        navController.navigate("main_screen")
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "App Logo",
            modifier = Modifier.size(100.dp)
        )
        Text(text = "EconoTracker", style = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp)
        )
    }
}

// MainScreen.kt
@Composable
fun MainScreen(navController: NavController) {
    val balance = remember { mutableStateOf(0.0) } // Replace with actual balance calculation
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Current Balance: $${balance.value}", style = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp))
        Button(onClick = { navController.navigate("add_transaction_screen") }) {
            Text(text = "Add Income/Expense")
        }
        Button(onClick = { navController.navigate("overview_screen") }) {
            Text(text = "Overview")
        }
    }
}
// AddTransactionScreen.kt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(navController: NavController) {
    val transactionName = remember { mutableStateOf("") }
    val transactionAmount = remember { mutableStateOf("") }
    val transactionType = remember { mutableStateOf("") }

    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text(text = "Add Transaction", modifier = Modifier.align(Alignment.CenterHorizontally)) },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
                }
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = transactionName.value,
                onValueChange = { transactionName.value = it },
                label = { Text(text = "Transaction Name") }
            )
            OutlinedTextField(
                value = transactionAmount.value,
                onValueChange = { transactionAmount.value = it },
                label = { Text(text = "Amount") }
            )
            OutlinedTextField(
                value = transactionType.value,
                onValueChange = { transactionType.value = it },
                label = { Text(text = "Type (Income/Expense)") }
            )

            Button(
                onClick = {
                    // Save transaction data to SavedStateHandle
                    savedStateHandle?.set("transaction_name", transactionName.value)
                    savedStateHandle?.set("transaction_amount", transactionAmount.value)
                    savedStateHandle?.set("transaction_type", transactionType.value)

                    navController.popBackStack()
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(text = "Add Transaction")
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun OverviewScreen(navController: NavController) {
    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle

    val transactionName = savedStateHandle?.get<String>("transaction_name")
    val transactionAmount = savedStateHandle?.get<String>("transaction_amount")
    val transactionType = savedStateHandle?.get<String>("transaction_type")

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text(text = "Overview", modifier = Modifier.align(Alignment.CenterHorizontally)) },
            navigationIcon = {
                IconButton(onClick = { /* Open menu */ }) {
                    Icon(imageVector = Icons.Filled.Menu, contentDescription = "Menu")
                }
            },
            actions = {
                IconButton(onClick = { navController.navigate("add_transaction_screen") }) {
                    Icon(imageVector = Icons.Filled.AddCircle, contentDescription = "Add Transaction")
                }
            }
        )

        // Display statistics in three horizontal bars
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TotalIncomeBlock(transactionName, transactionAmount, transactionType)
            //TotalExpenseBlock(transactionName, transactionAmount, transactionType)
            //NetSavingsBlock(transactionName, transactionAmount, transactionType)
        }

        // Display last transaction summary
        //LastTransactionSummary(transactionName, transactionAmount, transactionType)
    }
}

@Composable
fun TotalIncomeBlock(transactionName: String?, transactionAmount: String?, transactionType: String?) {
    // Calculate total income based on transaction data
    val totalIncome = 1000.00 // Replace with actual calculation

    Column(
        modifier = Modifier
            .width(100.dp)
            .height(100.dp)
            .background(Color.Green) // Set color for total income block
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Total Income", style = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp))
        Text(text = "$totalIncome", style = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp))
    }
}

@Composable
fun TotalIncomeBlock() {
    Column(
        modifier = Modifier
            .width(100.dp)
            .height(100.dp)
            .background(Color.Green) // Set color for total income block
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Total Income", style = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp))
        Text(text = "$1000.00", style = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp))
    }
}

// Implement TotalExpenseBlock, NetSavingsBlock, and LastTransactionSummary similarly



