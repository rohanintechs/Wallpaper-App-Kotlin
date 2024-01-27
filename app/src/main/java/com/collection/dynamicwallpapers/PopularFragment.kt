package com.collection.dynamicwallpapers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView

class PopularFragment : Fragment() {

    private lateinit var courseList: ArrayList<GridViewModal>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {

        val view = inflater.inflate(R.layout.fragment_popular, container, false)

        val courseGRV: GridView = view.findViewById(R.id.idGRV2)

        courseList = ArrayList()

        courseList.add(GridViewModal("https://w.forfun.com/fetch/c8/c858342b6d573b6032463d152d5a6248.jpeg"))
        courseList.add(GridViewModal("https://w0.peakpx.com/wallpaper/805/996/HD-wallpaper-dream-house-luxury-thumbnail.jpg"))
        courseList.add(GridViewModal("https://downloadwap.com/thumbs2/wallpapers/p2/2019/misc/12/32e3025013586170.jpg"))
        courseList.add(GridViewModal("https://images.wallpapersden.com/image/download/forest-house-covered-in-snow-4k_bGdnaGuUmZqaraWkpJRmZW1lrWdpZWU.jpg"))
        courseList.add(GridViewModal("https://1.bp.blogspot.com/-gWvPL9GY6aM/XgWZPNW254I/AAAAAAAAK8s/uVllgMA-wNokUP_Oenxrc2DiV9EGYLHsACLcBGAsYHQ/s1600/Switzerland-Zermatt.jpg"))
        courseList.add(GridViewModal("https://images.unsplash.com/photo-1563416854-0e5e3a0cfdb5?q=80&w=1635&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"))

        val courseAdapter = GridRVAdapter(courseList, requireContext())
        courseGRV.adapter = courseAdapter

        courseGRV.setOnItemClickListener { _, _, position, _ ->
            val detailFragment = WallpaperDetailFragment()
            val args = Bundle()
            args.putString("imageUrl", courseList[position].imageUrl) // Pass the image URL here
            args.putStringArrayList("imageUrls", getImageUrls())
            args.putInt("currentPosition", position)
            detailFragment.arguments = args

            // Navigate to the detailFragment using FragmentTransaction
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, detailFragment) // Replace 'fragment_container' with your container ID
                .addToBackStack(null)
                .commit()


        }

        return view
    }

    private fun getImageUrls(): ArrayList<String> {
        val imageUrls = ArrayList<String>()
        for (course in courseList) {
            imageUrls.add(course.imageUrl)
        }
        return imageUrls
    }

}
