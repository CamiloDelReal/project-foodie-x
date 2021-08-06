package org.xapps.apps.foodiex.core.remote

import org.xapps.apps.foodiex.core.models.RecipeResponse
import retrofit2.http.GET
import retrofit2.http.Query


interface SpoonacularApi {

    @GET("recipes/complexSearch")
    suspend fun listRecipes(
        @Query("apiKey") apiKey: String,
        @Query("offset") offset: Int,
        @Query("number") number: Int
    ): RecipeResponse

    @GET("recipes/complexSearch?type=drink&sort=popularity&sortDirection=desc")
    suspend fun listPopularDrinks(
        @Query("apiKey") apiKey: String,
        @Query("offset") offset: Int,
        @Query("number") number: Int
    ): RecipeResponse

    @GET("recipes/complexSearch?type=main%20course&sort=popularity&sortDirection=desc")
    suspend fun listPopularMeals(
        @Query("apiKey") apiKey: String,
        @Query("offset") offset: Int,
        @Query("number") number: Int
    ): RecipeResponse

}