package com.kodeleku.pmdm_u4_firebase

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class RealTimeDatabaseManager {

    private val databaseReference = FirebaseDatabase.getInstance().reference

    // ESCRIBIR DATOS
    suspend fun addFavourite(favourite:FavouriteAdvertisement): FavouriteAdvertisement? {
        // Conexión al nodo "faves", si el nodo no se encuentra creado en la base de datos lo creara automáticamente.
        // Si quisieramos almacenar más objetos en otras funciones, deberíamos conectarnos a otro child para organizar bien la información
        val connection: DatabaseReference = databaseReference.child("faves")
        // Creamos una key
        val key: String? = connection.push().key
        // Si no es nula guardamos el anuncio en el nodo "faves", favoritos
        if (key!=null){
            // Copia del favorito asignandole la key
            val favouriteWithKey: FavouriteAdvertisement = favourite.copy(key=key)
            // Añadimos a la conexión un objecto hijo con ID del anuncio y seleccionamos la data class que recibimos en esta función. Con await esperamos a que finalice la tarea
            connection.child("${favourite.advertisementId}").setValue(favouriteWithKey).await()
            // Si marcha, retornamos el objeto con su key para que la vista que lo solicito lo reciba indicandole que el valor se almaceno correctamente.
            Log.d("addFavourite", "guardado")
            return favouriteWithKey
        }else {
            Log.e("addFavourite", "fallo")
            // En caso de no poder crear la key retornamos null para indicar que no se guardó
            return null
        }

    }


    // LEER DATOS
    // recibirá el id del anuncio en el que estamos para filtrar la lista de favoritos y devolverá la lista de elementos que recibamos desde Firebase ya filtrada
    suspend fun readFavourite(advertisementId: String): FavouriteAdvertisement? {
        // Nos conectamos al nodo "faves"
        val connection: DatabaseReference = databaseReference.child("faves")

        // Recoger la lista
        val snapshot: Task<DataSnapshot> = connection.get()
        // Esperar la conexión para recoger la lista
        snapshot.await()

        snapshot.result.children.mapNotNull { dataSnapshot ->
            // Recogemos cada favorito y le asignamos su key si no son nulos
            val favourite: FavouriteAdvertisement? = dataSnapshot.getValue(FavouriteAdvertisement::class.java)

            val key: String? = dataSnapshot.key
            if (key!=null && favourite!=null && favourite.advertisementId == advertisementId){
                return favourite
            }
        }
        // Si no encontramos el anuncio que coincida con el ID, devolvemos un null
        return null



    }


}