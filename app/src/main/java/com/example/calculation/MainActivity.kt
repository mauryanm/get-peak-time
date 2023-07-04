package com.example.calculation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.calculation.ui.theme.CalculationTheme
import com.example.peaktimer.PeakTimer
import org.json.JSONObject


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalculationTheme {

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    val startDate: String = "2014-01-22 10:30:00"
    val endDate: String = "2014-01-22 17:30:00"
    val pts: String = "15:45:00"
    val pte: String = "16:00:00"
    var nd = PeakTimer.getPeakTimeDetail(startDate,endDate,pts,pte).toString()
    val json = JSONObject(nd) // String instance holding the above json
    val peakTime = json.getInt("peak_time")
    val totalTime = json.getInt("total_time")
    Text(
        text = "Peak Time:  $peakTime! and Total time :$totalTime",

        modifier = modifier
    )

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CalculationTheme {
        Greeting("Android")
    }
}