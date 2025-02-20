package com.example.spotter20

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate

class ChartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chart)

        val barChart = findViewById<BarChart>(R.id.barChart)

        // Sample Data (Modify this based on API data)
        val barEntries = ArrayList<BarEntry>()
        barEntries.add(BarEntry(0f, 1f))
        barEntries.add(BarEntry(1f, 1f))
        barEntries.add(BarEntry(2f, 2f))
        barEntries.add(BarEntry(3f, 0f))
        barEntries.add(BarEntry(4f, 0f))

        val dataSet = BarDataSet(barEntries, "Events")
        dataSet.colors = ColorTemplate.COLORFUL_COLORS.toList() // Set colors for bars

        val barData = BarData(dataSet)
        barChart.data = barData

        // Customize Chart
        barChart.description.isEnabled = false
        barChart.setFitBars(true)
        barChart.animateY(1000)

        // Customize X-Axis
        val xAxis = barChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)

        // Refresh Chart
        barChart.invalidate()
    }
}
