package com.example.calculatorapp

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Stack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorApp(navigationToSecondScreen: () -> Unit){
    //var ans by remember { mutableStateOf(0) }
    var inputvalue by remember { mutableStateOf("") }
    var hiddenInputValue by remember { mutableStateOf("") }
    var outputvalue by remember { mutableStateOf("") }
    fun infixToPostfix(expression: String): List<String> {
        val output = mutableListOf<String>()
        val operators = Stack<String>()
        val tokens = expression.split("\\s+".toRegex())

        val precedence = mapOf(
            "+" to 1,
            "-" to 1,
            "X" to 2,
            "/" to 2
        )

        for (token in tokens) {
            when {
                token.toDoubleOrNull() != null -> output.add(token) // Operand
                token in precedence.keys -> { // Operator
                    while (operators.isNotEmpty() && operators.peek() != "(" &&
                        (precedence[operators.peek()] ?: 0) >= (precedence[token] ?: 0)
                    ) {
                        output.add(operators.pop())
                    }
                    operators.push(token)
                }
                token == "(" -> operators.push(token) // Opening parenthesis
                token == ")" -> { // Closing parenthesis
                    while (operators.isNotEmpty() && operators.peek() != "(") {
                        output.add(operators.pop())
                    }
                    if (operators.isNotEmpty() && operators.peek() == "(") {
                        operators.pop() // Remove the opening parenthesis from the stack
                    }
                }
                else -> throw IllegalArgumentException("Invalid token: $token")
            }
        }

        while (operators.isNotEmpty()) {
            output.add(operators.pop())
        }

        return output
    }


    fun evaluatePostfix(postfix: List<String>): Double {
        val stack = Stack<Double>()

        for (token in postfix) {
            when {
                token.toDoubleOrNull() != null -> stack.push(token.toDouble())
                token in setOf("+", "-", "X", "/") -> {
                    val operand2 = stack.pop()
                    val operand1 = stack.pop()
                    val result = when (token) {
                        "+" -> operand1 + operand2
                        "-" -> operand1 - operand2
                        "X" -> operand1 * operand2
                        "/" -> operand1 / operand2
                        else -> throw IllegalArgumentException("Invalid operator")
                    }
                    stack.push(result)
                }
            }
        }

        return stack.pop()
    }

    fun evaluateExpression(expression: String): String {
        return try {
            // Convert infix to postfix
            val postfix = infixToPostfix(expression)
            // Evaluate the postfix expression
            val result = evaluatePostfix(postfix)
            result.toString()
        } catch (e: Exception) {
            ""
        }
    }
    val columnBackgroundColor = Color(0xFFF3F4F6)
    val buttoncolour1 = Color(0xFFD3D3D3)


    Box (
        modifier= Modifier
            .fillMaxSize()
            .background(columnBackgroundColor)
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            var fontSize1 by remember { mutableStateOf(16.sp) }
            var fontSize2 by remember { mutableStateOf(16.sp) }
            val scrollState1 = rememberScrollState()
            val scrollState2 = rememberScrollState()
            // Launched effect to automatically scroll to the end of the text when it changes
            LaunchedEffect(inputvalue) {
                scrollState1.scrollTo(scrollState1.maxValue)
            }
            LaunchedEffect(inputvalue) {
                scrollState2.scrollTo(scrollState2.maxValue)
            }
           Button(onClick = { navigationToSecondScreen() }) {
               Icon(Icons.Filled.List, contentDescription ="converters")
           }
            //Spacer(modifier = Modifier.height(180.dp))
            OutlinedTextField(
                value = inputvalue,
                onValueChange = { inputvalue = it },
                modifier= Modifier
                    .fillMaxWidth()
                    .horizontalScroll(scrollState1),
                enabled = false,
                readOnly = true,
                textStyle = TextStyle(fontSize = fontSize1, textAlign = TextAlign.End, fontWeight = FontWeight.Bold, color = Color.Black),
                colors = TextFieldDefaults.outlinedTextFieldColors(unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent,
                    disabledBorderColor = Color.Transparent
                ),
                singleLine = true
            )
            //  OutlinedTextField(value =outputvalue , onValueChange ={outputvalue=it} )
            //   Spacer(modifier = Modifier.height(10.dp))
            /*   Text(
                   outputvalue,
                   modifier = Modifier.padding(8.dp),
                   style = TextStyle(fontSize = fontSize2)
               )*/

            OutlinedTextField(
                value = outputvalue,
                onValueChange = { outputvalue = it },
                modifier= Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .horizontalScroll(scrollState2),
                enabled = false,
                readOnly = true,
                textStyle = TextStyle(fontSize = fontSize2, textAlign = TextAlign.End, fontWeight = FontWeight.Bold,color = Color.Black),
                colors = TextFieldDefaults.outlinedTextFieldColors(unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent,
                    disabledBorderColor = Color.Transparent
                ),
                singleLine = true
            )

            //  Spacer(modifier = Modifier.height(40.dp))
            Row (  horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()){
                Button(
                    onClick = {
                        outputvalue = ""
                        inputvalue = ""
                        fontSize1 = 16.sp
                        fontSize2=16.sp
                    },
                    shape = CircleShape,
                    modifier = Modifier.size(70.dp),
                    colors = ButtonDefaults.buttonColors(Color.LightGray, Color.Black)
                ) {
                    Text("AC",fontSize =18.sp)
                }
                Button(
                    onClick = { inputvalue += " % " },
                    shape = CircleShape,
                    modifier = Modifier.size(70.dp),
                    colors = ButtonDefaults.buttonColors(buttoncolour1, Color.Black)
                ) {
                    Text("%",fontSize =24.sp)
                }
                Button(
                    onClick = { if(inputvalue.isNotEmpty()){
                        inputvalue=inputvalue.dropLast(1)
                        outputvalue=evaluateExpression(inputvalue)
                    }
                    },
                    shape = CircleShape,
                    modifier = Modifier.size(70.dp),
                    colors = ButtonDefaults.buttonColors(buttoncolour1, Color.Black)
                ) {
                    Icon(Icons.Filled.Clear, contentDescription="Clear" )
                }
                Button(
                    onClick = { inputvalue += " / " },
                    shape = CircleShape,
                    modifier = Modifier.size(70.dp),
                    colors = ButtonDefaults.buttonColors(Color.LightGray, Color.Black)
                ) {
                    Text("/", fontSize =24.sp)
                }





            }
            Row (
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()

            ){
                Button(
                    onClick = {
                        inputvalue += "1"

                        outputvalue = evaluateExpression(inputvalue)

                    },
                    shape = CircleShape,
                    modifier = Modifier.size(70.dp),
                    colors = ButtonDefaults.buttonColors(Color.White, Color.Black)
                ) {
                    Text("1",fontSize =24.sp)
                }
                Button(
                    onClick = {
                        inputvalue += "2"
                        outputvalue = evaluateExpression(inputvalue)
                    },
                    shape = CircleShape,
                    modifier = Modifier.size(70.dp),
                    colors = ButtonDefaults.buttonColors(Color.White, Color.Black)
                ) {
                    Text("2",fontSize =24.sp)
                }
                Button(
                    onClick = {
                        inputvalue += "3"
                        outputvalue = evaluateExpression(inputvalue)
                    },
                    shape = CircleShape,
                    modifier = Modifier.size(70.dp),
                    colors = ButtonDefaults.buttonColors(Color.White, Color.Black)
                ) {
                    Text("3",fontSize =24.sp)
                }
                Button(
                    onClick = { inputvalue += " X " },
                    shape = CircleShape,
                    modifier = Modifier.size(70.dp),
                    colors = ButtonDefaults.buttonColors(buttoncolour1, Color.Black)
                ) {
                    Text("X",fontSize =24.sp)
                }

            }
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()

            ) {
                Button(
                    onClick = {
                        inputvalue += "4"
                        outputvalue = evaluateExpression(inputvalue)
                    },
                    shape = CircleShape,
                    modifier = Modifier.size(70.dp),
                    colors = ButtonDefaults.buttonColors(Color.White, Color.Black)
                ) {
                    Text("4",fontSize =24.sp)
                }
                Button(
                    onClick = {
                        inputvalue += "5"
                        outputvalue = evaluateExpression(inputvalue)
                    },
                    shape = CircleShape,
                    modifier = Modifier.size(70.dp),
                    colors = ButtonDefaults.buttonColors(Color.White, Color.Black)
                ) {
                    Text("5",fontSize =24.sp)
                }
                Button(
                    onClick = {
                        inputvalue += "6"
                        outputvalue = evaluateExpression(inputvalue)

                    },
                    shape = CircleShape,
                    modifier = Modifier.size(70.dp),
                    colors = ButtonDefaults.buttonColors(Color.White, Color.Black)
                ) {
                    Text("6",fontSize =24.sp)
                }
                Button(
                    onClick = {
                        inputvalue += " + "
                        outputvalue = evaluateExpression(inputvalue)
                    },
                    shape = CircleShape,
                    modifier = Modifier.size(70.dp),
                    colors = ButtonDefaults.buttonColors(Color.LightGray, Color.Black)
                ) {
                    Text("+",fontSize =24.sp)
                }

            }
            Row (
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()

            ){
                Button(
                    onClick = {
                        inputvalue += "7"
                        outputvalue = evaluateExpression(inputvalue)
                    },
                    shape = CircleShape,
                    modifier = Modifier.size(70.dp),
                    colors = ButtonDefaults.buttonColors(Color.White, Color.Black)
                ) {
                    Text("7",fontSize =24.sp)
                }
                Button(
                    onClick = {
                        inputvalue += "8"
                        outputvalue = evaluateExpression(inputvalue)
                    },
                    shape = CircleShape,
                    modifier = Modifier.size(70.dp),
                    colors = ButtonDefaults.buttonColors(Color.White, Color.Black)
                ) {
                    Text("8",fontSize =24.sp)
                }
                Button(
                    onClick = {
                        inputvalue += "9"
                        outputvalue = evaluateExpression(inputvalue)
                    },
                    shape = CircleShape,
                    modifier = Modifier.size(70.dp),
                    colors = ButtonDefaults.buttonColors(Color.White, Color.Black)
                ) {
                    Text("9",fontSize =24.sp)
                }
                Button(
                    onClick = {
                        inputvalue += " - "
                        outputvalue = evaluateExpression(inputvalue)
                    },
                    shape = CircleShape,
                    modifier = Modifier.size(70.dp),
                    colors = ButtonDefaults.buttonColors(Color.LightGray, Color.Black)
                ) {
                    Text("-",fontSize =24.sp)
                }
            }
            Row (
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()

            ){
                Button(
                    onClick = {
                        inputvalue += "00"
                        outputvalue = evaluateExpression(inputvalue)
                    },
                    shape = CircleShape,
                    modifier = Modifier.size(70.dp).padding(0.dp),
                    colors = ButtonDefaults.buttonColors(Color.White, Color.Black)
                ) {
                    //Text("00",fontSize =14.sp)
                    Box (){
                        Text(text = "00", fontSize = 24.sp, maxLines = 1, overflow = TextOverflow.Clip, softWrap = false,textAlign=TextAlign.Left)

                    }
                }
                Button(
                    onClick = {
                        inputvalue += "0"
                        outputvalue = evaluateExpression(inputvalue)
                    },
                    shape = CircleShape,
                    modifier = Modifier.size(70.dp),
                    colors = ButtonDefaults.buttonColors(Color.White, Color.Black)
                ) {
                    Text("0",fontSize =24.sp)
                }
                Button(
                    onClick = {
                        inputvalue += "."
                        outputvalue = evaluateExpression(inputvalue)
                    },
                    shape = CircleShape,
                    modifier = Modifier.size(70.dp),
                    colors = ButtonDefaults.buttonColors(Color.White, Color.Black)
                ) {
                    Text(".",fontSize =24.sp)
                }

                Button(
                    onClick = {
                        outputvalue = evaluateExpression(inputvalue)
                        fontSize1 = 12.sp
                        fontSize2 = 40.sp

                    },
                    shape = CircleShape,
                    modifier = Modifier.size(70.dp),
                    colors = ButtonDefaults.buttonColors(Color.Gray, Color.Black)
                ) {
                    Text("=",fontSize =24.sp)

                }


            }

        }
    }

}
