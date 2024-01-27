package com.collection.dynamicwallpapers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView

class  HomeFragment : Fragment() {

    private lateinit var courseList: ArrayList<GridViewModal> // Declare it as a member variable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {

        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val courseGRV: GridView = view.findViewById(R.id.idGRV)

        courseList = ArrayList()

        courseList.add(GridViewModal("https://w0.peakpx.com/wallpaper/310/1013/HD-wallpaper-mustang-car-cars.jpg"))
        courseList.add(GridViewModal("https://w0.peakpx.com/wallpaper/324/853/HD-wallpaper-dodge-challenger-blue-blue-car-car-charger-sport-sport-car-sports-car-thumbnail.jpg"))
        courseList.add(GridViewModal("https://e1.pxfuel.com/desktop-wallpaper/22/662/desktop-wallpaper-cars-mobile-full-1080x1920-in-2021-toyota-supra-736x1308-for-your-mobile-tablet-vertical-car.jpg"))
        courseList.add(GridViewModal("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTEVP6LaxkQWjNVv3yURBv6J1tN_OlC6TEAMg&usqp=CAU"))
        courseList.add(GridViewModal("https://www.mobilesmspk.net/user/images/upload_images/2020/06/19/www.mobilesmspk.net_car-image-1.jpg"))
        courseList.add(GridViewModal("https://images.unsplash.com/photo-1605559424843-9e4c228bf1c2?q=80&w=1664&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"))

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
