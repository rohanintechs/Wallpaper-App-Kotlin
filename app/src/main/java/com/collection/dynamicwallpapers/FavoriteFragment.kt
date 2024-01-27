package com.collection.dynamicwallpapers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridView
import com.collection.dynamicwallpapers.R.id.idGRV3


class FavoriteFragment : Fragment(), FavoriteInteractionListener {

    private lateinit var favoriteImages: MutableList<String>
    private lateinit var courseGRV: GridView
    private lateinit var imageUrlStorage: ImageUrlStorage  // Add this line
    private var isFavorite = false // Track favorite state
    private lateinit var courseAdapter: GridRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_favorite, container, false)

        courseGRV = view.findViewById(R.id.idGRV3)
        imageUrlStorage = ImageUrlStorage(requireContext())
        favoriteImages = loadFavoriteImagesFromStorage().toMutableList() // Convert to mutable list

        // Create and set the adapter with the favorite images list
        courseAdapter = GridRVAdapter(favoriteImages.map { GridViewModal(it) }, requireContext())
        courseGRV.adapter = courseAdapter


        // Handle item click to remove image from favorites
        courseGRV.setOnItemClickListener { _, _, position, _ ->
            val detailFragment = WallpaperDetailFragment()
            val args = Bundle()
            args.putString("imageUrl", favoriteImages[position]) // Pass the image URL directly from the list
            args.putStringArrayList("imageUrls", ArrayList(favoriteImages)) // Convert to ArrayList
            args.putInt("currentPosition", position)
            detailFragment.arguments = args

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, detailFragment)
                .addToBackStack(null)
                .commit()
        }

        return view
    }


    private fun loadFavoriteImagesFromStorage(): List<String> {
        val imageUrlStorage = ImageUrlStorage(requireContext())
        val favoriteImageUrls = imageUrlStorage.loadImageUrls()

        if (favoriteImageUrls.isEmpty()) {
            println("No favorite image URLs found.")
        } else {
            println("Favorite image URLs found:")
            for (imageUrl in favoriteImageUrls) {
                println(imageUrl)
            }
        }

        return favoriteImageUrls
    }



    override fun addToFavorites(imageUrl: String) {
        imageUrlStorage.saveImageUrl(imageUrl)
        favoriteImages.add(imageUrl)
        courseAdapter.notifyDataSetChanged()
    }

    override fun removeFromFavorites(imageUrl: String) {
        imageUrlStorage.removeImageUrl(imageUrl)
        favoriteImages.remove(imageUrl)
        courseAdapter.notifyDataSetChanged()
    }

    private fun updateFavoriteButtonIcon() {
        val favoriteButton = requireView().findViewById<Button>(R.id.favoriteButton)
        val iconResId = if (isFavorite) {
            R.drawable.favorite_button_selected
        } else {
            R.drawable.favorite_button
        }
        favoriteButton.setCompoundDrawablesWithIntrinsicBounds(0, iconResId, 0, 0)
    }

    // Add this function to toggle the favorite state and update the button icon
    private fun toggleFavoriteState() {
        isFavorite = !isFavorite
        updateFavoriteButtonIcon()
    }
}


