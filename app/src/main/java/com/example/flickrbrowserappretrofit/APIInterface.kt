package com.example.flickrbrowserappretrofit

import android.provider.ContactsContract
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface APIInterface {
    @GET("/services/rest/?method=flickr.photos.search&api_key=9cf6eae618774b19d20c9c0d747c820a&tags=&format=json&nojsoncallback=1")
    fun doGetListResources(searchWord:String): Call<ImageDetails?>?

    @GET("/services/rest/?nojsoncallback=1")
    fun getPhoto(@Query("method")method: String,@Query("api_key")apiKey: String ,
               @Query("tags")tags: String , @Query("format")format: String ): Call<ImageDetails?>?
}