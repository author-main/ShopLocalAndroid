package com.training.shoplocal

import com.fasterxml.jackson.databind.JsonNode
import com.training.shoplocal.classes.Product
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RestAssuredTest {
    /*
    Для отображения русской кодировки добавить в studio64.exe.vmoptions:
    -Dconsole.encoding=UTF-8
    -Dfile.encoding=UTF-8
    */
    private val SERVER_TEST_API = "http://192.168.1.10/api/test"
    /*private fun getMail(): Stream<String> = Stream.of("myshansky@inbox.ru")
    @MethodSource("getMail")*/
    @ParameterizedTest
    @ValueSource(strings = ["myshansky@inbox.ru"]) // <- email для тестирования
    @DisplayName("Получение токена пользователя по email")
    fun getUserToken(email: String){
        //val email = "myshansky@inbox.ru";
        val token = given()
            .contentType(ContentType.JSON)
            //.pathParam("email", "myshansky@inbox.ru")
            .get("$SERVER_TEST_API/get_user_token?email=$email")
            //.get("$SERVER_TEST_API/get_user_token?email={email}", email)
            .then().log().all()
            .extract().body().jsonPath().getString("token")
        println(" ")
        println(">>> token = $token")
        println(" ")
        Assertions.assertTrue(token.length > 1)
    }

    @Test
    @DisplayName("Получение списка продуктов")
    fun getProducts(){
        val token = "542ed70b4602f422ce1dba45a9a59c86"      // <- токен пользователя
        val page  = 2                                       // <- порция данных продуктов
        val response = given()
            .contentType(ContentType.JSON)
            .pathParam("token", token)
            .pathParam("page", page)
            .get("$SERVER_TEST_API/get_products?token={token}&page={page}")
            .then()
            //.extract().body().jsonPath().getList<Product>(".", Product::class.java)
            .extract().response()
        val jsonPath = response.jsonPath()
        val products = jsonPath.getList<Product>(".", Product::class.java)

        /*
        проверка на наличие продуктов
        */
        Assertions.assertTrue(products.isNotEmpty())

        /*
        проверка на избранные продукты
        */
        Assertions.assertTrue(products.any { it.favorite > 0 })

        /*
        проверка на продукты со скидкой
        */
        Assertions.assertTrue(products.any { it.discount > 0 })
    }


}