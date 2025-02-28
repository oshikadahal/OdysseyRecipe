package com.example.odysseyrecipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import kotlin.coroutines.jvm.internal.CompletedContinuation.context

private lateinit var _binding : FragmentHomeBinding
private val binding get() = _binding!!
private lateinit var firebaseAuth: FirebaseAuth
private lateinit var arrayRecipe : ArrayList<Recipe>
private lateinit var recipeAdapter : RecipeAdapter
private lateinit var recyclerView: RecyclerView

override fun onCreateView (
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
): View? {
    // Inflate the layout for this fragment
    _binding = FragmentHomeBinding.inflate(inflater, container , false)

    recyclerView = binding.recyclerRecipe
    recyclerView.layoutManager = GridLayoutManager(context,1, GridLayoutManager.VERTICAL,false)
//        recycler view smooth
    recyclerView.setHasFixedSize(true)


    showRecipes()
    return binding.root
}

fun showRecipes() {
    val db = Firebase.firestore
//      println("Show recipe is called............")

    arrayRecipe = arrayListOf<Recipe>()



    db.collection("recipes").get()
        .addOnSuccessListener {
//              println("Show recipe success.................................")
            for (document in it){
                val recipeData = document.data

                try {

                    if(recipeData != null){
//                          println("TRY BLOCK.................................")

                        val recipeName = recipeData["Name"] as String
                        val recipeIngredient = recipeData["Ingredient"] as String
                        val recipeInstruction = recipeData["Instruction"] as String
                        val recipeImage = recipeData["Image"] as String
                        val recipeOwner = recipeData["Owner"] as String
                        val recipeId = document.id
                        // println("DATA MILA>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
                        // println("RECIPE ID >>>>>>>>>>>>>>>> $recipeId")

                        val recipe = Recipe(recipeName,recipeIngredient,recipeInstruction,recipeImage,recipeOwner , recipeId  )

                        arrayRecipe.add(recipe)
//                          println("NAME : $arrayRecipe ...................................................")


                    }

                }catch (e : Exception){
//                      println("EXCEPTION.................................")
                    e.printStackTrace()
                }

                if(arrayRecipe.isNotEmpty()){
                    recipeAdapter = RecipeAdapter("home",arrayRecipe,requireContext())

                    recyclerView.adapter = recipeAdapter


                }else{
                    println("NO RECIPE....................................")
                }
            }
        }
        .addOnFailureListener{
            val it = ""
            println("Fail to get Recipe Data........ $it")
        }
}

}
