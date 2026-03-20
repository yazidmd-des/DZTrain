package com.yzd.dztrain

import java.io.Serializable
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FavoritesAdapter(
    private val favorites: List<FavoriteRoute>,
    private val onClick: (FavoriteRoute) -> Unit,
    private val onLongClick: (FavoriteRoute) -> Unit
) : RecyclerView.Adapter<FavoritesAdapter.ViewHolder>() {

    // 1. Define a list of soft pastel colors
    private val pastelColors = listOf(
        "#FFF9C4", // Pastel Yellow
        "#F1F8E9", // Pastel Green
        "#E1F5FE", // Pastel Blue
        "#F3E5F5", // Pastel Purple
        "#FFF3E0", // Pastel Orange
        "#FCE4EC", // Pastel Pink
        "#E0F2F1", // Pastel Teal
        "#EFEBE9"  // Pastel Brown/Grey
    )

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val card: com.google.android.material.card.MaterialCardView = view.findViewById(R.id.favCardContainer) // Added ID to the CardView in XML
        val tvFrom: TextView = view.findViewById(R.id.tvFavFrom)
        val tvTo: TextView = view.findViewById(R.id.tvFavTo)
        val tvDetails: TextView = view.findViewById(R.id.tvFavDetails)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_favorite_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = favorites[position]

        // 2. Assign a consistent color based on the name
        // We use Math.abs because hashCode can be negative
        val colorIndex = Math.abs("${item.from}${item.to}".hashCode()) % pastelColors.size
        val selectedColor = Color.parseColor(pastelColors[colorIndex])

        holder.card.setCardBackgroundColor(selectedColor)

        holder.tvFrom.text = item.from
        holder.tvTo.text = item.to
        holder.tvDetails.text = "Dep: ${item.time}"

        holder.itemView.setOnClickListener { onClick(item) }
        holder.itemView.setOnLongClickListener { onLongClick(item); true }
    }

    override fun getItemCount() = favorites.size
}