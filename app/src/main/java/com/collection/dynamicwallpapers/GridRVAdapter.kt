package com.collection.dynamicwallpapers

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import coil.load

internal class GridRVAdapter(
    private var courseList: List<GridViewModal>,
    private val context: Context
) : BaseAdapter() {

    private var layoutInflater: LayoutInflater? = null

    override fun getCount(): Int {
        return courseList.size
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var convertView = convertView
        if (layoutInflater == null) {
            layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }
        if (convertView == null) {
            convertView = layoutInflater!!.inflate(R.layout.gridview_item, null)
        }

        val courseIV: ImageView = convertView!!.findViewById(R.id.idIVCourse)

        courseIV.load(courseList[position].imageUrl) {
            crossfade(1000) // Enable crossfade animation
            placeholder(R.drawable.loading_indicator) // Set the placeholder image
        }

        return convertView
    }

    // Method to update the data in the adapter
    fun updateData(newData: List<GridViewModal>) {
        courseList = newData
        notifyDataSetChanged()
    }
}
