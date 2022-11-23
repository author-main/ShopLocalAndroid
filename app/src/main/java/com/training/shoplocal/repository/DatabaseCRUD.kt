package com.training.shoplocal.repository

import com.training.shoplocal.classes.Category
import com.training.shoplocal.classes.Product
import com.training.shoplocal.classes.Products
import com.training.shoplocal.classes.User
import com.training.shoplocal.log
import com.training.shoplocal.repository.retrofit.DatabaseApi
import retrofit2.Call
import retrofit2.Response

class DatabaseCRUD: DatabaseCRUDInterface {
    override fun getPromotionProduct(action: (product: Product) -> Unit ){
       // log("getPromotionProduct")
        DatabaseApi.getPromotionProduct(object: retrofit2.Callback<Product> {

            override fun onResponse(call: Call<Product>, response: Response<Product>) {
                response.body()?.let {
                        action.invoke(it)
                }
            }

            override fun onFailure(call: Call<Product>, t: Throwable) {
                log("error")
            }

        })
    }

    override fun getCategories() {
        TODO("Not yet implemented")
    }

}