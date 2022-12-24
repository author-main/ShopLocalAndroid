package com.training.shoplocal.repository

import com.training.shoplocal.classes.Brand
import com.training.shoplocal.classes.Product
import com.training.shoplocal.log
import com.training.shoplocal.repository.retrofit.DatabaseApi
import retrofit2.Call
import retrofit2.Response

class DatabaseCRUD: DatabaseCRUDInterface {

    override fun getBrands(action: (brands: List<Brand>) -> Unit) {
        DatabaseApi.getBrands(object: retrofit2.Callback<List<Brand>>{
            override fun onResponse(call: Call<List<Brand>>, response: Response<List<Brand>>) {
                response.body()?.let {
                    if (it.isNotEmpty()) {
                        val outlist = it
                        action.invoke(outlist)
                    }
                }
            }
            override fun onFailure(call: Call<List<Brand>>, t: Throwable) {
            }
        })
    }

    override fun getProducts(id: Int, part: Int, order: String, reserved: String, action: (products: List<Product>) -> Unit) {
        DatabaseApi.getProducts(id, part, order, reserved, object: retrofit2.Callback<List<Product>>{
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {

                    response.body()?.let {
                        if (it.isNotEmpty()) {
                            action.invoke(it)
                        }
                    }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                //log(t.message ?: "ошибка")
                //log("ошибка")
                action.invoke(mutableListOf<Product>())
            }
        })
    }


    override fun getFoundProducts(
        query: String,
        order: String,
        portion: Int,
        uuid: String,
        userid: Int,
        reserved: String,
        action: (products: List<Product>) -> Unit
    ) {
        DatabaseApi.getFoundProducts(query,
            order,
            portion,
            uuid,
            userid,
            reserved,
            object: retrofit2.Callback<List<Product>>{
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                response.body()?.let {
                    if (it.isNotEmpty()) {
                        action.invoke(it)
                    }
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                action.invoke(mutableListOf<Product>())
            }
        })
    }

    override fun getProduct(id: Int, action: (product: Product) -> Unit ){
       // log("getPromotionProduct")
        DatabaseApi.getProduct(id, object: retrofit2.Callback<Product> {

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

    }

    override suspend fun updateFavorite(id_user: Int, id_product: Int, value: Byte): Boolean {
        return DatabaseApi.updateFavorite(id_user, id_product, value)
    }

   /* override suspend fun getProducts(id: Int, part: Int): List<Product> {
        return DatabaseApi.getProducts(id, part)
    }*/
}