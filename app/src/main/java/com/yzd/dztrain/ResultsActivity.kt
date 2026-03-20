package com.yzd.dztrain

import com.google.android.material.chip.ChipGroup
import android.view.View
import android.graphics.Color
import android.app.AlertDialog
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ImageSpan
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ResultsActivity : AppCompatActivity() {

    private lateinit var adapter: JourneyAdapter
    private var allResults: List<Journey> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        val toolbar = findViewById<Toolbar>(R.id.toolbarResults)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.navigationIcon?.setTint(Color.WHITE)
        toolbar.setNavigationOnClickListener { finish() }

        allResults = TrainRepository.latestResults

        val rvResults = findViewById<RecyclerView>(R.id.rvResults)
        rvResults.layoutManager = LinearLayoutManager(this)

        // Setup Adapter with the Popup Logic
        adapter = JourneyAdapter(allResults) { journey ->
            showStopsPopup(journey)
        }
        rvResults.adapter = adapter

        val chipGroup = findViewById<ChipGroup>(R.id.chipGroupFilters)

        chipGroup.setOnCheckedChangeListener { group, checkedId ->
            applyFilters(
                isFirst = checkedId == R.id.chipFirstDep,
                isArrival = checkedId == R.id.chipArrival,
                isShortest = checkedId == R.id.chipShortest,
                isDirect = checkedId == R.id.chipDirect
            )
        }
    }

    private fun applyFilters(isFirst: Boolean, isArrival: Boolean, isShortest: Boolean, isDirect: Boolean) {
        var filtered = allResults.toList()

        // 1. "Direct Only" Hard Filter
        if (isDirect) {
            filtered = filtered.filter { it is Journey.Direct }
        }

        // Helper to get numeric values without re-calculating (Matches findJourneys logic)
        fun Journey.getNumericDuration(): Int = when(this) {
            is Journey.Direct -> this.durationMins
            is Journey.Connection -> this.totalDurationMins
        }

        fun Journey.getNumericArrival(): Int = when(this) {
            is Journey.Direct -> this.arrivalAbsMins
            is Journey.Connection -> this.arrivalAbsMins
        }

        fun Journey.getNumericDeparture(): Int {
            val timeStr = when(this) {
                is Journey.Direct -> this.train.depTime
                is Journey.Connection -> this.firstTrain.depTime
            }
            // Convert to minutes for sorting (0..1439)
            val parts = timeStr.split(":")
            return parts[0].toInt() * 60 + parts[1].toInt()
        }

        // 2. Multi-Level Sorting using Metadata
        val sorted = when {
            isFirst -> {
                // Departure -> Arrival -> Direct First -> Duration
                filtered.sortedWith(
                    compareBy<Journey> { it.getNumericDeparture() }
                        .thenBy { it.getNumericArrival() }
                        .thenBy { if (it is Journey.Direct) 0 else 1 }
                        .thenBy { it.getNumericDuration() }
                )
            }
            isArrival -> {
                // Arrival -> Departure -> Direct First -> Duration
                filtered.sortedWith(
                    compareBy<Journey> { it.getNumericArrival() }
                        .thenBy { it.getNumericDeparture() }
                        .thenBy { if (it is Journey.Direct) 0 else 1 }
                        .thenBy { it.getNumericDuration() }
                )
            }
            isShortest -> {
                // Duration -> Direct First -> Arrival Time -> Departure Time
                // This ensures 20min < 1440min (1d)
                filtered.sortedWith(
                    compareBy<Journey> { it.getNumericDuration() }
                        .thenBy { if (it is Journey.Direct) 0 else 1 }
                        .thenBy { it.getNumericArrival() }
                        .thenBy { it.getNumericDeparture() }
                )
            }
            else -> {
                // Default: Direct Trips first, then by Departure
                filtered.sortedWith(
                    compareBy<Journey> { if (it is Journey.Direct) 0 else 1 }
                        .thenBy { it.getNumericDeparture() }
                )
            }
        }

        adapter.updateList(sorted)
    }

    private fun showStopsPopup(journey: Journey) {
        val builder = AlertDialog.Builder(this)
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_stops, null)
        val tvTitle = view.findViewById<TextView>(R.id.tvPopupTitle)
        val tvContent = view.findViewById<TextView>(R.id.tvPopupContent)
        val btnToggle = view.findViewById<Button>(R.id.btnToggleStops)

        var isShowingFullRoute = false

        // --- Internal Helpers for Text Formatting ---
        fun formatRow(name: String, time: String): String {
            val cleanName = if (name.length > 18) name.substring(0, 15) + ".." else name
            return String.format("%-20s %5s", cleanName, time).replace(" ", ".")
        }

        fun appendStyled(sb: SpannableStringBuilder, text: String, color: Int = -1, isBg: Boolean = false, isBold: Boolean = false) {
            val start = sb.length
            sb.append(text)
            if (color != -1) {
                val span = if (isBg) android.text.style.BackgroundColorSpan(color) else android.text.style.ForegroundColorSpan(color)
                sb.setSpan(span, start, sb.length, android.text.Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
            if (isBold) {
                sb.setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD), start, sb.length, android.text.Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }

        fun appendIcon(sb: SpannableStringBuilder, drawableResId: Int, sizeDp: Int = 20) {
            val density = resources.displayMetrics.density
            val sizePx = (sizeDp * density).toInt()
            val drawable: Drawable? = ContextCompat.getDrawable(this, drawableResId)
            drawable?.setBounds(0, 0, sizePx, sizePx)
            val start = sb.length
            sb.append("  ") // Placeholder
            drawable?.let {
                val span = ImageSpan(it, ImageSpan.ALIGN_BOTTOM)
                sb.setSpan(span, start, start + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }

        // --- Main Update Logic ---
        fun updatePopupText() {
            val sb = SpannableStringBuilder()

            when (journey) {
                is Journey.Direct -> {
                    val stops = if (isShowingFullRoute) journey.train.fullTrainStops else journey.train.stops
                    stops.forEachIndexed { index, stop ->
                        if (index == 0) {
                            appendIcon(sb, R.drawable.ic_train_green)
                            sb.append(" ")
                        }
                        sb.append(formatRow(stop.name, stop.time) + "\n")
                    }
                }
                is Journey.Connection -> {
                    // Use the SAME logic as the Adapter
                    val waitStr = journey.totalDuration.substringAfter("Wait: ").replace(")", "").trim()
                    val waitMinutes = parseWaitToMinutes(waitStr)
                    val transferIconRes = when {
                        waitMinutes < 10 -> R.drawable.ic_running
                        waitMinutes < 15 -> R.drawable.ic_hurrying
                        waitMinutes < 20 -> R.drawable.ic_walking
                        else -> R.drawable.ic_sitting
                    }
                    val isUrgent = waitMinutes < 10

                    if (isShowingFullRoute) {
                        appendStyled(sb, "\n [ TRAIN ${journey.firstTrain.id} ] \n", Color.parseColor("#E3F2FD"), isBg = true)
                        journey.firstTrain.fullTrainStops.forEach { sb.append(formatRow(it.name, it.time) + "\n") }

                        sb.append("\n ")
                        appendIcon(sb, transferIconRes)

                        if (isUrgent) {
                            appendStyled(sb, " URGENT CHANGE: ${journey.transferStation.uppercase()} \n", Color.RED, isBold = true)
                            appendStyled(sb, " (Wait: $waitStr) - Tight Connection!\n\n", Color.RED)
                        } else {
                            appendStyled(sb, " CHANGE AT ${journey.transferStation.uppercase()} \n", Color.parseColor("#1976D2"), isBold = true)
                            sb.append(" (Wait: $waitStr) \n\n")
                        }

                        appendStyled(sb, " [ TRAIN ${journey.secondTrain.id} ] \n", Color.parseColor("#E3F2FD"), isBg = true)
                        journey.secondTrain.fullTrainStops.forEach { sb.append(formatRow(it.name, it.time) + "\n") }
                    } else {
                        journey.firstTrain.stops.forEach { sb.append(formatRow(it.name, it.time) + "\n") }

                        sb.append("\n ")
                        appendIcon(sb, transferIconRes)

                        if (isUrgent) {
                            appendStyled(sb, " URGENT: ${journey.transferStation.uppercase()} ($waitStr) \n\n", Color.RED, isBold = true)
                        } else {
                            appendStyled(sb, " CHANGE: ${journey.transferStation.uppercase()} ($waitStr) \n\n", Color.parseColor("#1976D2"), isBold = true)
                        }

                        journey.secondTrain.stops.forEach { sb.append(formatRow(it.name, it.time) + "\n") }
                    }
                }
            }
            tvContent.text = sb
            btnToggle.text = if (isShowingFullRoute) "Show My Journey Only" else "Show Full Schedules"
        }

        tvTitle.text = if (journey is Journey.Direct) "Train ${journey.train.id}" else "Connection Details"
        updatePopupText()

        btnToggle.setOnClickListener {
            isShowingFullRoute = !isShowingFullRoute
            updatePopupText()
        }

        builder.setView(view)
        builder.setPositiveButton("Close", null)
        builder.show()
    }

    // Ensure this helper is also in your Activity (or accessible to it)
    private fun parseWaitToMinutes(waitStr: String): Int {
        return try {
            val numbers = Regex("\\d+").findAll(waitStr).map { it.value.toInt() }.toList()
            when {
                waitStr.contains("h") && numbers.size >= 2 -> (numbers[0] * 60) + numbers[1]
                waitStr.contains("h") && numbers.size == 1 -> numbers[0] * 60
                numbers.isNotEmpty() -> numbers[0]
                else -> 30
            }
        } catch (e: Exception) { 30 }
    }

    object JourneyUIUtils {
        fun parseWaitToMinutes(waitStr: String): Int {
            return try {
                val numbers = Regex("\\d+").findAll(waitStr).map { it.value.toInt() }.toList()
                when {
                    waitStr.contains("h") && numbers.size >= 2 -> (numbers[0] * 60) + numbers[1]
                    waitStr.contains("h") && numbers.size == 1 -> numbers[0] * 60
                    numbers.isNotEmpty() -> numbers[0]
                    else -> 30
                }
            } catch (e: Exception) { 30 }
        }

        fun getTransferIcon(waitMinutes: Int): Int {
            return when {
                waitMinutes < 10 -> R.drawable.ic_running
                waitMinutes < 15 -> R.drawable.ic_hurrying
                waitMinutes < 20 -> R.drawable.ic_walking
                else -> R.drawable.ic_sitting
            }
        }
    }
}