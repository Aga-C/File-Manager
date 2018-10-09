package com.simplemobiletools.filemanager.activities

import android.graphics.Paint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.simplemobiletools.commons.dialogs.FilePickerDialog
import com.simplemobiletools.commons.extensions.beVisibleIf
import com.simplemobiletools.commons.extensions.getAdjustedPrimaryColor
import com.simplemobiletools.commons.interfaces.RefreshRecyclerViewListener
import com.simplemobiletools.filemanager.R
import com.simplemobiletools.filemanager.adapters.ManageFavoritesAdapter
import com.simplemobiletools.filemanager.extensions.config
import kotlinx.android.synthetic.main.activity_favorites.*

class FavoritesActivity : SimpleActivity(), RefreshRecyclerViewListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)
        updateFavorites()
    }

    private fun updateFavorites() {
        val favorites = ArrayList<String>()
        config.favorites.mapTo(favorites, { it })
        manage_favorites_placeholder.beVisibleIf(favorites.isEmpty())
        manage_favorites_placeholder.setTextColor(config.textColor)

        manage_favorites_placeholder_2.apply {
            paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG
            beVisibleIf(favorites.isEmpty())
            setTextColor(getAdjustedPrimaryColor())
            setOnClickListener {
                addFavorite()
            }
        }

        ManageFavoritesAdapter(this, favorites, this, manage_favorites_list) { }.apply {
            manage_favorites_list.adapter = this
            initSelectionTracker()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_favorites, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_favorite -> addFavorite()
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    override fun refreshItems() {
        updateFavorites()
    }

    private fun addFavorite() {
        FilePickerDialog(this, pickFile = false, showHidden = config.shouldShowHidden) {
            config.addFavorite(it)
            updateFavorites()
        }
    }
}
