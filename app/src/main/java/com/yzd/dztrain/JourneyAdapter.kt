package com.yzd.dztrain

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView // Change this
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class JourneyAdapter(
    private var journeys: List<Journey>,
    private val onItemClick: (Journey) -> Unit
) : RecyclerView.Adapter<JourneyAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTrainId: TextView = view.findViewById(R.id.tvTrainId)
        val tvTotalDuration: TextView = view.findViewById(R.id.tvTotalDuration)
        val tvLineBadge: TextView = view.findViewById(R.id.tvLineBadge)
        val tvRoute: TextView = view.findViewById(R.id.tvRouteTitle)
        val tvVia: TextView = view.findViewById(R.id.tvViaStation)
        val tvDepTime: TextView = view.findViewById(R.id.tvDepTime)
        val tvArrTime: TextView = view.findViewById(R.id.tvArrTime)
        val transferDot: ImageView = view.findViewById(R.id.transferDot)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_journey, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val journey = journeys[position]

        when (journey) {
            is Journey.Direct -> {
                val train = journey.train
                holder.tvTrainId.text = "Train ${train.id}"
                holder.tvTotalDuration.text = calculateDuration(train.depTime, train.arrTime)
                holder.tvRoute.text = "${train.from} ➔ ${train.to}"
                holder.tvVia.visibility = View.GONE
                holder.transferDot.visibility = View.GONE
                holder.tvDepTime.text = train.depTime
                holder.tvArrTime.text = train.arrTime
                holder.tvLineBadge.text = train.lineName
                holder.tvLineBadge.backgroundTintList = ColorStateList.valueOf(Color.parseColor(getLineColor(train.lineName)))

                if (train.isHoliday) {
                    holder.tvTrainId.append(" (Hol/Fri)")
                    holder.tvTrainId.setTextColor(Color.RED)
                } else {
                    holder.tvTrainId.setTextColor(Color.parseColor("#6B7280"))
                }
            }
            is Journey.Connection -> {
                holder.tvTrainId.text = "Train ${journey.firstTrain.id} + ${journey.secondTrain.id}"
                holder.tvTotalDuration.text = journey.totalDuration
                holder.tvRoute.text = "${journey.firstTrain.from} ➔ ${journey.secondTrain.to}"
                holder.tvVia.text = "via ${journey.transferStation}"
                holder.tvVia.visibility = View.VISIBLE
                holder.tvDepTime.text = journey.firstTrain.depTime
                holder.tvArrTime.text = journey.secondTrain.arrTime
                holder.tvLineBadge.text = "Connection"
                holder.tvLineBadge.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#374151"))

                // --- DYNAMIC ICON LOGIC ---
                holder.transferDot.visibility = View.VISIBLE

                // 1. Extract wait time from string like "1h05 (Wait: 12 min)"
                val waitStr = journey.totalDuration.substringAfter("Wait: ").replace(")", "").trim()
                val waitMinutes = parseWaitToMinutes(waitStr)

                // 2. Set icon based on minutes
                val iconRes = when {
                    waitMinutes < 10 -> R.drawable.ic_running       // Urgent
                    waitMinutes < 15 -> R.drawable.ic_hurrying      // Fast
                    waitMinutes < 20 -> R.drawable.ic_walking       // Relaxed
                    else -> R.drawable.ic_sitting           // Long wait
                }
                holder.transferDot.setImageResource(iconRes)

                // 3. Optional: Tint Red if urgent
                if (waitMinutes < 10) {
                    holder.transferDot.imageTintList = ColorStateList.valueOf(Color.RED)
                } else {
                    holder.transferDot.imageTintList = null
                }
            }
        }
        holder.itemView.setOnClickListener { onItemClick(journey) }
    }

    // Helper to parse "15 min" or "1h 05" into Int minutes
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

    override fun getItemCount() = journeys.size

    fun updateList(newList: List<Journey>) {
        journeys = newList
        notifyDataSetChanged()
    }

    private fun getLineColor(lineName: String): String {
        return when {
            lineName.contains("Zeralda", true) -> "#2E7D32"
            lineName.contains("Thenia", true) -> "#1976D2"
            lineName.contains("Affroun", true) -> "#C62828"
            else -> "#374151"
        }
    }

    private fun calculateDuration(dep: String, arr: String): String {
        val d = dep.split(":").let { it[0].toInt() * 60 + it[1].toInt() }
        val a = arr.split(":").let { it[0].toInt() * 60 + it[1].toInt() }
        val diff = if (a >= d) a - d else (a + 1440) - d
        val hours = diff / 60
        val minutes = diff % 60
        return if (hours > 0) "${hours}h${minutes.toString().padStart(2, '0')}" else "${minutes}min"
    }
}