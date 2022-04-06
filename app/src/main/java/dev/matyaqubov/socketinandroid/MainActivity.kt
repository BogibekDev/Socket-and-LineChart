package dev.matyaqubov.socketinandroid

import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import dev.matyaqubov.socketinandroid.manager.SocketListener
import dev.matyaqubov.socketinandroid.manager.WebSocketBts
import dev.matyaqubov.socketinandroid.model.BitCoin
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    lateinit var tv_socket: TextView
    private lateinit var webSocketBts: WebSocketBts
    private val lineValue = ArrayList<Entry>()
    private var count = 0
    lateinit var lineChart: LineChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
    }

    private fun initViews() {
        webSocketBts = WebSocketBts()
        webSocketBts.connectToSocket()
        tv_socket = findViewById(R.id.tv_responce)
        lineChart = findViewById(R.id.line_chart)
        configureLineChart()
        webSocketBts.socketListener(object : SocketListener {
            override fun onSuccess(bitCoin: BitCoin) {
                count++
                runOnUiThread {
                    if (bitCoin.event == "bts:subscription_succeeded") {
                        tv_socket.text = "Successfull Connected ,please wait"
                        Toast.makeText(
                            this@MainActivity,
                            "Successfull Connected ,please wait",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        lineValue.add(Entry(count.toFloat(), bitCoin.data.price.toFloat()))
                        setLineChartData(lineValue)
                        tv_socket.text = "1 BTC = \$${bitCoin.data.price_str}"
                    }
                }
            }

            override fun onFailure(message: String) {
                runOnUiThread {
                    tv_socket.text = message
                }
            }

        })


    }

    private fun setLineChartData(pricesHigh: ArrayList<Entry>) {
        val dataSets: ArrayList<ILineDataSet> = ArrayList()

        val highLineDataSet = LineDataSet(pricesHigh, "Bitcoin prices")
        highLineDataSet.setDrawCircles(true)
        highLineDataSet.circleRadius = 4f
        highLineDataSet.setDrawValues(false)
        highLineDataSet.lineWidth = 3f
        highLineDataSet.color = Color.GREEN
        highLineDataSet.setCircleColor(Color.GREEN)
        dataSets.add(highLineDataSet)

        val lineData = LineData(dataSets)
        lineChart.data = lineData
        lineChart.invalidate()
    }

    private fun configureLineChart() {
        val desc = Description()
        desc.text = "Bitcoin Price History"
        desc.textSize = 10F
        lineChart.description = desc
        val xAxis: XAxis = lineChart.xAxis
        xAxis.valueFormatter = object : ValueFormatter() {
            @RequiresApi(Build.VERSION_CODES.N)
            private val mFormat: SimpleDateFormat = SimpleDateFormat("dd MMM", Locale.getDefault())

            @RequiresApi(Build.VERSION_CODES.N)
            override fun getFormattedValue(value: Float): String {
                return mFormat.format(Date(System.currentTimeMillis()))
            }
        }
    }


}