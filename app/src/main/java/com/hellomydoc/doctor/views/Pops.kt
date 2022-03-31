package com.hellomydoc.doctor.views

import android.content.Context
import android.view.View
import androidx.appcompat.widget.PopupMenu

class Pops(
    private val context: Context,
    private val view: View?,
    private val items: List<String>?,
    private val callback: (String)->Unit
) {

    fun show() {
        if (items == null) {
            return
        }
        if (items.size < 1) {
            return
        }
        if(view==null){
            return
        }
        val popupMenu = PopupMenu(
            context, view
        )
        val len = items.size
        for (i in 0 until len) {
            popupMenu.menu.add(items[i])
        }
        popupMenu.setOnMenuItemClickListener { item ->
            callback(item.title.toString())
            false
        }
        popupMenu.show()
    }
}