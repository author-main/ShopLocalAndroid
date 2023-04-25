package com.training.shoplocal

import com.training.shoplocal.classes.SERVER_URL
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import io.restassured.path.json.JsonPath
import org.hamcrest.Matchers.notNullValue
import org.junit.Assert
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class RestAssuredTest {
    private val SERVER_TEST_API = "http://192.168.0.10/api/test"
    @Test
    fun getUserToken(){
        val response = given()
            .contentType(ContentType.JSON)
            .get("$SERVER_TEST_API/get_user_token?email=myshansky@inbox.ru")
            .then().log().all()
            //.extract().body().jsonPath()
            .body("token", notNullValue())
            .extract().response()
        val jsonPath: JsonPath = response.jsonPath()
        //Assertions.assertEquals(jsonPath, null)
        val i = 0

    }

    @Test
    fun easyTest(){
        Assertions.assertNotSame(2,3)

    }



    /* @Test
     fun getListProducts(){

     }*/
}