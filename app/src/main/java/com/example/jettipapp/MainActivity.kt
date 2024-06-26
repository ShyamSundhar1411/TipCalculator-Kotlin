package com.example.jettipapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jettipapp.components.InputField
import com.example.jettipapp.ui.theme.JetTipAppTheme
import com.example.jettipapp.utils.calculateTotalTip
import com.example.jettipapp.utils.calculatetotalPerPerson
import com.example.jettipapp.widgets.RoundIconButton
import kotlin.math.round

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApp {
                MainContent()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp(content: @Composable () -> Unit){
    JetTipAppTheme(darkTheme = false) {
        Scaffold(modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(title = { Text("JetTipApp") })
            }
        ){ innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                Column(modifier = Modifier.padding(10.dp)){
                    content()
                }
            }
        }
    }
}

@Composable
fun TopHeaderCard(totalPerPerson:Double = 0.0){
    Box(modifier = Modifier.padding(20.dp), contentAlignment = Alignment.Center) {
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(50.dp),
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
@OptIn(ExperimentalComposeUiApi::class)
@Preview(showBackground = true)
@Composable
fun MainContent(){
    BillForm(){
        billAmt->
        Log.d("Amt", billAmt)
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BillForm(
    modifier: Modifier = Modifier,
    onValChange: (String) -> Unit = {}
){
    val totalBillState = remember{
        mutableStateOf("0");
    }
    val validState = remember(key1 = totalBillState.value) {
        totalBillState.value.trim().isNotEmpty();
    }
    val keyboardController = LocalSoftwareKeyboardController.current;
    val sliderPositionState = remember {
        mutableFloatStateOf(0f)
    }
    val splitByState = remember{
        mutableIntStateOf(1)
    }
    val range = IntRange(start=1, endInclusive = 15);
    val totalTipAmount = remember {
        mutableDoubleStateOf(0.00);
    }
    val tipPercentage = remember {
        mutableIntStateOf(0);
    }
    val totalPerPersonState = remember {
        mutableDoubleStateOf(0.00);
    }
    TopHeaderCard(totalPerPerson = totalPerPersonState.doubleValue)
    Surface(modifier = Modifier
        .padding(2.dp)
        .fillMaxWidth(),
        shape = RoundedCornerShape(CornerSize(12.dp)),
        border = BorderStroke(width = 1.dp,color = Color.LightGray)
    ) {
        Column {
            InputField(
                valueState = totalBillState,
                labelId = "Enter the Bill",
                enabled = true,
                isSingleLine = true,
                onAction = KeyboardActions {
                    if(!validState) return@KeyboardActions
                    onValChange(totalBillState.value.trim())
                    keyboardController?.hide()
                }
            )
                Row(modifier = Modifier.padding(10.dp),
                    horizontalArrangement = Arrangement.Start) {
                        Text("Split",
                            modifier = Modifier.align(alignment = Alignment.CenterVertically)

                        )
                    Spacer(modifier = Modifier.width(120.dp))
                    Row(modifier = Modifier.padding(horizontal = 3.dp), horizontalArrangement = Arrangement.End){
                        RoundIconButton(imageVector = Icons.Default.Remove,
                            onClick = {
                                splitByState.intValue = if(splitByState.intValue>1) splitByState.intValue-1
                                else 1
                                totalPerPersonState.doubleValue = calculatetotalPerPerson(totalBillState.value.toDouble(),splitByState.intValue, tipPercentage.intValue)
                            })
                        Text(text = "${splitByState.intValue}",
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .padding(start = 9.dp, end = 9.dp)
                            )
                        RoundIconButton(imageVector = Icons.Default.Add,
                            onClick = {
                                if (splitByState.intValue<range.last) splitByState.intValue += 1
                                totalPerPersonState.doubleValue = calculatetotalPerPerson(totalBillState.value.toDouble(),splitByState.intValue, tipPercentage.intValue)
                            })
                    }

                }
               Row(modifier = Modifier.padding(10.dp),
                   horizontalArrangement = Arrangement.Start
                   ) {
                   Text(text = "Tip",modifier = Modifier.align(alignment = Alignment.CenterVertically))
                   Spacer(modifier = Modifier.width(200.dp))
                   Text(text = "$${totalTipAmount.doubleValue}",modifier = Modifier.align(alignment = Alignment.CenterVertically))
               }
            Column(modifier = modifier.padding(10.dp),verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
                ) {
                Text(text = "${tipPercentage.intValue}%")
                Spacer(modifier = Modifier.height(14.dp))
                Slider(
                    modifier = Modifier.padding(start = 16.dp,end = 16.dp),
                    value = sliderPositionState.floatValue,
                    steps = 5,
                    onValueChange = {newVal ->
                        sliderPositionState.floatValue = newVal;
                        tipPercentage.intValue = round(sliderPositionState.floatValue *100).toInt()
                        totalTipAmount.doubleValue = calculateTotalTip(totalBill = totalBillState.value.toDouble(), tipPercentage = tipPercentage.intValue)
                       totalPerPersonState.doubleValue = calculatetotalPerPerson(totalBillState.value.toDouble(), splitBy = splitByState.intValue, tipPercentage = tipPercentage.intValue);
                    },
                    onValueChangeFinished = {


                    }
                )

            }


        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApp{
        MainContent()

    }
}
