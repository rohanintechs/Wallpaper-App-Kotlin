package com.collection.dynamicwallpapers

import android.Manifest
import android.app.WallpaperManager
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import coil.load
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class WallpaperDetailFragment : Fragment(), FavoriteInteractionListener {

    private lateinit var viewPager: ViewPager2
    private lateinit var imageUrls: ArrayList<String>
    private var currentPosition: Int = 0

    private var isFavorite = false // Track favorite state
    private lateinit var favoriteButton: Button // Declare a class-level variable

    private lateinit var imageUrlStorage: ImageUrlStorage
    private lateinit var favoriteImages: MutableList<String> // Define favoriteImages list
    private lateinit var courseAdapter: GridRVAdapter // Define courseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imageUrlStorage = ImageUrlStorage(requireContext())
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_wallpaper_detail, container, false)

        viewPager = view.findViewById(R.id.viewPager)
        favoriteButton = view.findViewById(R.id.favoriteButton) // Initialize the variable

        // Log the current item position for debugging
        println("Current ViewPager item position: $currentPosition")


        imageUrls = arguments?.getStringArrayList("imageUrls") ?: ArrayList()
        currentPosition = arguments?.getInt("currentPosition") ?: 0

        // Initialize favoriteImages list and courseAdapter
        favoriteImages = imageUrlStorage.loadImageUrls().toMutableList()
        courseAdapter = GridRVAdapter(favoriteImages.map { GridViewModal(it) }, requireContext())

        val adapter = ImagePagerAdapter(imageUrls)
        viewPager.adapter = adapter
        viewPager.setCurrentItem(currentPosition, false)

        val setWallpaperButton: Button = view.findViewById(R.id.setWallpaperButton)
        val downloadButton: Button = view.findViewById(R.id.downloadButton)
        val favoriteButton: Button = view.findViewById(R.id.favoriteButton)

        downloadButton.setOnClickListener {
            val currentPagePosition = viewPager.currentItem
            val currentPageUrl = imageUrls[currentPagePosition]
            downloadImage(currentPagePosition, currentPageUrl)
        }

        setWallpaperButton.setOnClickListener {
            val currentPagePosition = viewPager.currentItem
            val currentPageUrl = imageUrls[currentPagePosition]
            setWallpaper(currentPagePosition, currentPageUrl)
        }

        // Set up the favorite button's icon based on the current favorite status
        updateFavoriteButtonIcon(imageUrls[currentPosition])

        favoriteButton.setOnClickListener {
            // Toggle favorite state
            isFavorite = !isFavorite

            // Update button icon based on favorite state
            val iconResId = if (isFavorite) {
                R.drawable.favorite_button_selected
            } else {
                R.drawable.favorite_button
            }

            favoriteButton.setCompoundDrawablesWithIntrinsicBounds(
                0, iconResId, 0, 0
            )

            // Update the favorite status in the FavoriteFragment
            if (isFavorite) {
                addToFavorites(imageUrls[currentPosition])
            } else {
                removeFromFavorites(imageUrls[currentPosition])
            }
        }


        val backButton: Button = view.findViewById(R.id.backButton)
        backButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        return view
    }

    private val onPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            currentPosition = position
            val imageUrl = imageUrls[currentPosition]
            isFavorite = favoriteImages.contains(imageUrl)
            updateFavoriteButtonIcon(imageUrl)

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewPager.unregisterOnPageChangeCallback(onPageChangeCallback)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Check if the current image URL is in the favoriteImages list
        val imageUrl = imageUrls[currentPosition]
        if (favoriteImages.contains(imageUrl)) {
            isFavorite = true
            updateFavoriteButtonIcon(imageUrl)
        }

        viewPager.registerOnPageChangeCallback(onPageChangeCallback)


    }


    private fun updateFavoriteButtonIcon(imageUrl: String) {
        val iconResId = if (favoriteImages.contains(imageUrl)) {
            R.drawable.favorite_button_selected
        } else {
            R.drawable.favorite_button
        }
        favoriteButton.setCompoundDrawablesWithIntrinsicBounds(0, iconResId, 0, 0)
    }


    override fun addToFavorites(imageUrl: String) {
        if (!favoriteImages.contains(imageUrl)) {
            // Save the image URL to favorites using imageUrlStorage
            imageUrlStorage.saveImageUrl(imageUrl)
            favoriteImages.add(imageUrl)
            courseAdapter.notifyDataSetChanged()

            // Update the UI or perform other actions
            // For example, you can update the favorite button's icon or show a message
            favoriteButton.setCompoundDrawablesWithIntrinsicBounds(
                0, R.drawable.favorite_button_selected, 0, 0
            )
            updateFavoriteButtonIcon(imageUrl)

            Toast.makeText(requireContext(), "Image added to favorites", Toast.LENGTH_SHORT).show()
        } else {
            // The image is already in favorites, show a message or handle as desired
            Toast.makeText(requireContext(), "Image is already in favorites", Toast.LENGTH_SHORT).show()
        }


    }


    override fun removeFromFavorites(imageUrl: String) {
        // Remove the image URL from favorites using imageUrlStorage
        imageUrlStorage.removeImageUrl(imageUrl)
        favoriteImages.remove(imageUrl)
        courseAdapter.notifyDataSetChanged()

        // Update the UI or perform other actions
        // For example, you can update the favorite button's icon or show a message
        favoriteButton.setCompoundDrawablesWithIntrinsicBounds(
            0, R.drawable.favorite_button, 0, 0
        )

        updateFavoriteButtonIcon(imageUrl)

        Toast.makeText(requireContext(), "Image removed from favorites", Toast.LENGTH_SHORT).show()

    }






    private fun downloadImage(currentPagePosition: Int, imageUrl: String) {

        val currentImageView = viewPager.findViewWithTag<ImageView>(currentPagePosition)

        // Log the current item position for debugging
        println("Current ViewPager item position in downloadImage: ${viewPager.currentItem}")


        // Load the image using the Coil library
        currentImageView?.load(imageUrl) {
            // Handle the result of the image load
            listener(
                onSuccess = { _, _ ->
                    println("Image loaded successfully")

                    // Get the drawable from the ImageView
                    val drawable = currentImageView.drawable

                    // Convert the drawable to a Bitmap
                    val bitmap = (drawable as? BitmapDrawable)?.bitmap

                    bitmap?.let {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                            // Use MediaStore.createWriteRequest for Android 11 and higher
                            val resolver = requireContext().contentResolver
                            val contentValues = ContentValues().apply {
                                val currentTime = System.currentTimeMillis()
                                val fileName = "wallpaper_${currentTime}.jpg"
                                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/LivelyWallpaper")
                            }

                            val imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

                            imageUri?.let { uri ->
                                resolver.openOutputStream(uri)?.use { outputStream ->
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                                    outputStream.flush()
                                    outputStream.close()

                                    println("Image saved to external storage")

                                    // Get the image's saved path using the content URI
                                    val savedImagePath = getImagePathFromUri(uri)

                                    println("Image saved to: $savedImagePath")

                                    // Notify the user that the download is complete
                                    Toast.makeText(
                                        requireContext(),
                                        "Image downloaded",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        } else {
                            // Use traditional permission-based approach for Android versions prior to 11
                            val permission = Manifest.permission.WRITE_EXTERNAL_STORAGE
                            if (ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED) {
                                // Permission is granted

                                // Example using the App-Specific External Storage Directory
                                val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(
                                    Date()
                                )
                                val fileName = "wallpaper_$timestamp.jpg"

                                val directoryPath = Environment.getExternalStorageDirectory().toString() + "/LivelyWallpaper/"
                                val directory = File(directoryPath)
                                if (!directory.exists()) {
                                    directory.mkdirs()
                                }
                                val imageFile = File(directoryPath, fileName)

                                println("Saving image to directory: ${imageFile.absolutePath}")

                                try {
                                    val outputStream: OutputStream = FileOutputStream(imageFile)
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                                    outputStream.flush()
                                    outputStream.close()

                                    println("Image saved to external storage")

                                    // Notify the user that the download is complete
                                    Toast.makeText(
                                        requireContext(),
                                        "Image downloaded",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } catch (e: Exception) {
                                    println("Error saving image: ${e.message}")
                                }
                            } else {
                                // Permission is not granted
                                println("Permission to write external storage not granted")
                            }
                        }
                    } ?: run {
                        println("Bitmap is null")
                    }
                },
                onError = { _, throwable ->
                    // Handle image loading error
                    println("Error loading image")
                    Toast.makeText(
                        requireContext(),
                        "Error loading image",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
        } ?: run {
            println("Current ImageView is null")
        }
    }

    private fun getImagePathFromUri(uri: Uri): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = requireContext().contentResolver.query(uri, projection, null, null, null)
        val columnIndex = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor?.moveToFirst()
        val path = cursor?.getString(columnIndex ?: 0)
        cursor?.close()
        return path
    }



    private fun setWallpaper(currentPagePosition: Int, imageUrl: String) {
        val currentImageView = viewPager.findViewWithTag<ImageView>(currentPagePosition)

        // Load the image without the crossfade transition
        currentImageView.load(imageUrl) {
            crossfade(false) // Disable crossfade transition
            placeholder(R.drawable.loading_indicator)
            listener(
                onSuccess = { _, _ ->
                    // Get the drawable from the ImageView
                    val drawable = currentImageView.drawable

                    if (drawable == null) {
                        println("Drawable is null")
                    } else if (drawable is BitmapDrawable) {
                        val bitmap = drawable.bitmap
                        try {
                            WallpaperManager.getInstance(requireContext()).setBitmap(bitmap)
                            Toast.makeText(requireContext(), "Wallpaper set", Toast.LENGTH_SHORT).show()
                        } catch (e: Exception) {
                            // Handle exceptions
                            println("Error setting wallpaper: ${e.message}")
                        }
                    } else {
                        println("Drawable is not a BitmapDrawable")
                    }
                },
                onError = { _, throwable ->
                    // Handle image loading error
                    println("Error loading image")
                    Toast.makeText(
                        requireContext(),
                        "Error loading image",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
        }
    }

}
