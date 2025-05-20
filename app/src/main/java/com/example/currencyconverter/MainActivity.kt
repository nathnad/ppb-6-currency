package com.example.currencyconverter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.DecimalFormat
import com.example.currencyconverter.ui.theme.CurrencyConverterTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CurrencyConverterTheme {
                CurrencyConverterUI()
            }
        }
    }
}

fun convertCurrency(amount: Double, from: String, to: String): Double {
    val rates = mapOf(
        "USD" to 1.0,
        "IDR" to 15500.0,
        "EUR" to 0.92,
        "GBP" to 0.78
    )

    val amountInUSD = amount / (rates[from] ?: 1.0)
    return amountInUSD * (rates[to] ?: 1.0)
}


@Composable
fun CurrencyConverterUI() {
    var input by remember { mutableStateOf("") }
    var fromCurrency by remember { mutableStateOf("IDR") }
    var toCurrency by remember { mutableStateOf("USD") }
    var result by remember { mutableStateOf<String?>(null) }
    var convertedUnit by remember { mutableStateOf<String?>(null) }

//    val currencies = listOf("USD", "IDR", "EUR", "GBP")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(12.dp))
        Text("Currency Converter", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = input,
            onValueChange = { input = it },
            label = { Text("Amount") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))
        CurrencyDropdown(selected = fromCurrency, onSelected = { fromCurrency = it }, label = "From")
        Spacer(modifier = Modifier.height(8.dp))
        CurrencyDropdown(selected = toCurrency, onSelected = { toCurrency = it }, label = "To")

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            val amount = input.toDoubleOrNull()
            if (amount != null) {
                val converted = convertCurrency(amount, fromCurrency, toCurrency)
                val formatter = DecimalFormat("#,##0.00")
                result = formatter.format(converted)
                convertedUnit = toCurrency
            } else {
                result = "Invalid input"
                convertedUnit = null
            }
        }) {
            Text("Convert", fontSize = 18.sp)
        }

        result?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Text("Result: $it ${convertedUnit ?: ""}", style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
fun CurrencyDropdown(selected: String, onSelected: (String) -> Unit, label: String) {
    var expanded by remember { mutableStateOf(false) }
    val currencies = listOf("USD", "IDR", "EUR", "GBP")

    Column {
        Text(label)
        Box {
            Button(
                onClick = { expanded = true },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary
                )
            ) {
                Text(selected)
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                currencies.forEach { currency ->
                    DropdownMenuItem(
                        text = { Text(currency) },
                        onClick = {
                            onSelected(currency)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CurrencyConverterPreview() {
    CurrencyConverterTheme {
        CurrencyConverterUI()
    }
}

