package com.kodeleku.pmdm_u4_firebase

import android.net.Uri
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ListResult
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

    suspend fun getAllImages(): List<String> {
        // Accedemos al nodo de las imagenes de los anuncios
        val advertisementReference = storageReference.child("advertisement")

        // Creamos lista vacía para añadir los enlaces de las imagenes
        val imageList = mutableListOf<String>()
        // Pedimos el resultado de la referencia
        val result: ListResult = advertisementReference.listAll().await()
        // Iteramos la lista de items de la referencia una vez cargados con un forEach
        result.items.forEach{ item ->
            // Añadimos a la lista vacía de los enlaces de descarga de cada item
            imageList.add(item.downloadUrl.toString())
        }
        return imageList
    }

    suspend fun deleteImage (url:String): Boolean {
        // Recuperaos la referencia a partir del enlace
        val reference = FirebaseStorage.getInstance().getReferenceFromUrl(url)
        // Creamos la variable que devolveremos
        var wasSuccess = true
        if(reference !=null){
            refernce.delete().addOnFailureListener{
                // Si falla devolvemos error
                wasSuccess= false
            }.await()
        }else {
            // Si no hay referencia, también devolvemos error
            wasSuccess=false
        }
        return wasSuccess
    }

}