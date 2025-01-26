package com.kodeleku.pmdm_u4_firebase

data class FavouriteAdvertisement(
    // Key - guardará más adelante la que reciba de Firebase, se define como null
    var key:String? = null,
    var userId:String,
    var advertisementId: String,
    var title: String
)
