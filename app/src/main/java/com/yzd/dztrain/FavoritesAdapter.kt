package com.yzd.dztrain

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

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // We will use a simple TextView and style it like a chip
        val chipText: TextView = view.findViewById(android.R.id.text1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val fav = favorites[position]
        holder.chipText.text = "${fav.from} ➔ ${fav.to}"

        // --- Styling the Chip ---
        holder.chipText.apply {
            textSize = 14f
            setTextColor(Color.WHITE)
            // Use the "El Affroun Blue" as the primary theme color
            setBackgroundResource(android.R.drawable.btn_default)
            background.setTint(Color.parseColor("#1976D2"))
            setPadding(30, 10, 30, 10)
        }

        holder.itemView.setOnClickListener { onClick(fav) }
        holder.itemView.setOnLongClickListener {
            onLongClick(fav)
            true
        }
    }

    override fun getItemCount() = favorites.size
}