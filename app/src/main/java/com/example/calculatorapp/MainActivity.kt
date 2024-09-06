package com.example.calculatorapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.calculatorapp.ui.theme.CalculatorAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalculatorAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyApp()
                }
            }
        }
    }
}


@Composable
fun MyApp(){
 val navController= rememberNavController()
  NavHost(navController = navController, startDestination ="firstscreen"  ){
       composable("firstscreen"){
             CalculatorApp{
                 navController.navigate("secondscreen")
             }
       }
      composable("secondscreen"){
          Converters{
              navController.navigate("firstscreen")
          }
      }

  }
}