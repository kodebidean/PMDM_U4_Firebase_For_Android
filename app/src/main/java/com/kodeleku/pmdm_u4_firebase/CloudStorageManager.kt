package com.kodeleku.pmdm_u4_firebase

import android.net.Uri
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await

class CloudStorageManager {
    private val storageReference: StorageReference = FirebaseStorage.getInstance().reference

    suspend fun uploadAdvertisementImage(uri:Uri):String? {
        // Debemos cambiar las SlideBar por guiones bajos para que CloudStorage maneje bien la estructura una vez reciba el enlace
        val imageName = "$uri".replace(
            "/",
            "_"
        )
        // Variable para almacenar el enlace de Descarga
        var imageUrl:String?=null
        // Ceamos la referencia dentro de la carpeta de advertisement a nuestra imagen
        val advertisementReference = storageReference.child("advertisement/$imageName")

        // Mediante el método .putFile() subimos el archivo a Firebase
        advertisementReference.putFile(uri).continueWithTask { task ->
            // Si no se ha podido subir lanzamos la excepción
            if (!task.isSuccessful) {
                Log.e("CloudStorageManager", "Error al subir la imagen")
                task.exception?.let { throw it }
            }
            advertisementReference.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful){
                // Recuperamos el enlace donde se subió la imagen
                imageUrl = "${task.result}"
                Log.i("NewAdv", "Enlace de la foto: $imageUrl")
            }else{
                Log.e("NewAdv","Error al subir la imagen")
            }
        }.await() // La función no continua hasta que la tarea termine
        return imageUrl

    }
}