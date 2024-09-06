package com.example.calculatorapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun Converters(navigateToFirstScreen :() ->Unit){
    var inputvalue by remember { mutableStateOf("") }
    var outputvalue by remember{ mutableStateOf("") }
    var inputunit by remember{ mutableStateOf("meter") }
    var outputunit by remember{ mutableStateOf("meter") }
    var iexpanded by remember{ mutableStateOf(false) }
    var oexpanded by remember{ mutableStateOf(false) }
    val iconversionfactor = remember{ mutableStateOf(1.0) }
    val oconversionfactor = remember{ mutableStateOf(1.0) }

    fun convertunits(){
        val inputvaluedouble=inputvalue.toDoubleOrNull() ?: 0.0
        val result=(inputvaluedouble*iconversionfactor.value*100/oconversionfactor.value).roundToInt()/100.0
        outputvalue=result.toString()
    }
    Column(
        modifier= Modifier.fillMaxSize(),
        verticalArrangement= Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Text("Unit Converter",style= MaterialTheme.typography.headlineMedium)
        Spacer(modifier= Modifier.height(16.dp))
        OutlinedTextField(value = inputvalue, onValueChange ={
            inputvalue=it
            convertunits()
        }, label = {Text("enter value")})
        Row {
            Box {
                Button(onClick = { iexpanded=true }) {
                    Text(inputunit)
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "")

                }
                DropdownMenu(expanded = iexpanded, onDismissRequest = { iexpanded=false }) {
                    DropdownMenuItem(
                        text = { Text("centimeters") },
                        onClick = {
                            iexpanded=false
                            inputunit="centimeters"
                            iconversionfactor.value=0.01
                            convertunits()

                        }
                    )
                    DropdownMenuItem(
                        text = { Text("meters") },
                        onClick = {
                            iexpanded=false
                            inputunit="meters"
                            iconversionfactor.value=1.0
                            convertunits()
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("feet") },
                        onClick = {
                            iexpanded=false
                            inputunit="feet"
                            iconversionfactor.value=0.3048
                            convertunits() }
                    )
                    DropdownMenuItem(
                        text = { Text("millimeters") },
                        onClick = {
                            iexpanded=false
                            inputunit="millimeters"
                            iconversionfactor.value=0.001
                            convertunits() }
                    )
                }

            }
            Spacer(modifier = Modifier.width(16.dp))

            Box {
                Button(onClick = { oexpanded=true }) {
                    Text(outputunit)
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "")

                }
                DropdownMenu(expanded = oexpanded, onDismissRequest = { oexpanded=false }) {
                    DropdownMenuItem(
                        text = { Text("centimeters") },
                        onClick = { oexpanded=false
                            outputunit="centimeters"
                            oconversionfactor.value=0.01
                            convertunits()
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("meters") },
                        onClick = { oexpanded=false
                            outputunit="meters"
                            oconversionfactor.value=1.00
                            convertunits() }
                    )
                    DropdownMenuItem(
                        text = { Text("feet") },
                        onClick = { oexpanded=false
                            outputunit="feet"
                            oconversionfactor.value=0.3048
                            convertunits() }
                    )
                    DropdownMenuItem(
                        text = { Text("millimeters") },
                        onClick = { oexpanded=false
                            outputunit="millimeters"
                            oconversionfactor.value=0.001
                            convertunits() }
                    )
                }

            }
        }
        Spacer(modifier= Modifier.height(16.dp))
        Text("Result: $outputvalue $outputunit",style= MaterialTheme.typography.headlineMedium)
    }

}