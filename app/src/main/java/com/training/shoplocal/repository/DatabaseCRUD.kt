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
                TODO("Not yet implemented")
            }
        })
    }

    override fun getPromoProducts(action: (products: List<Product>) -> Unit) {
        DatabaseApi.getPromoProducts(object: retrofit2.Callback<Product.Products>{
            override fun onResponse(call: Call<Product.Products>, response: Response<Product.Products>) {
                response.body()?.let {
                    if (it.isNotEmpty()) {
                        val outlist = it.getItems()
                        if (outlist != null)
                            action.invoke(outlist)
                    }
                }
            }

            override fun onFailure(call: Call<Product.Products>, t: Throwable) {
                log(t.message ?: "ошибка")
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
        TODO("Not yet implemented")
    }

}