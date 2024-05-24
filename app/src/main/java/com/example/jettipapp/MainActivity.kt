package com.example.jettipapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jettipapp.ui.theme.JetTipAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApp {

            }
        }
    }
}

@Composable
fun MyApp(content: @Composable () -> Unit){
    JetTipAppTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding -> content()
        }
    }
}
@Preview(showBackground = true)
@Composable
fun TopHeaderCard(totalPerPerson: Double=0.0){
    Box(modifier = Modifier.padding(10.dp), contentAlignment = Alignment.Center) {
        Card(
            modifier =
            Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clip(shape = RoundedCornerShape(CornerSize(12.dp))),
            colors = CardDefaults.cardColors(Color(0xFFE9D7F7)),
            elevation = CardDefaults.cardElevation(4.dp)

        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(50.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                val total = "%.2f".format(totalPerPerson)
                Text(text = "Total per Person",
                    style= MaterialTheme.typography.titleMedium)
                Text(text = "$$total", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold)
            }

        }
    }
}
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApp{
        Text("Hello Again")
    }
}