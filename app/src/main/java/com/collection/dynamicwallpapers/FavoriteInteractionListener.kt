package com.collection.dynamicwallpapers

interface FavoriteInteractionListener {
    fun addToFavorites(imageUrl: String)
    fun removeFromFavorites(imageUrl: String)
}