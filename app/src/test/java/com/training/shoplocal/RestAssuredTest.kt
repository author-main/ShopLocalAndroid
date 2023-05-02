package com.training.shoplocal

import com.training.shoplocal.classes.Product
//import io.qameta.allure.Feature
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.builder.RequestSpecBuilder
import io.restassured.builder.ResponseSpecBuilder
import io.restassured.filter.log.LogDetail
import io.restassured.http.ContentType
import io.restassured.specification.RequestSpecification
import io.restassured.specification.ResponseSpecification
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.junit.runner.RunWith

public class Specification {
    companion object {
        fun requestSpec(URL: String): RequestSpecification =
            RequestSpecBuilder()
                .setBaseUri(URL)
                .setContentType(ContentType.JSON)
                //.addFilter(AllureRestAssured())
                .log(LogDetail.ALL)
                .build()

        fun responceSpecCode200(): ResponseSpecification =
            ResponseSpecBuilder()
                .expectStatusCode(200)
                .log(LogDetail.ALL)
                .build()

        fun installSpecifications(request: RequestSpecification, response: ResponseSpecification) {
            RestAssured.requestSpecification  = request
            RestAssured.responseSpecification = response
        }
    }
}
@DisplayName("Тестирование ShopLocal API")
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // при каждом тестирование не создается новый класс
class RestAssuredTest {
    /*
    Для отображения русской кодировки добавить в studio64.exe.vmoptions:
    -Dconsole.encoding=UTF-8
    -Dfile.encoding=UTF-8
    */
    private val SERVER_URI = "http://192.168.1.10"
    private val SERVER_TESTAPI = "/api/test"

    /*init {
        Specification.installSpecifications(
            request = Specification.requestSpec(SERVER_URI),
            response = Specification.responceSpecCode200()
        )
    }*/
    @BeforeAll
    fun initSpecifications(){
        Specification.installSpecifications(
            request = Specification.requestSpec(SERVER_URI),
            response = Specification.responceSpecCode200()
        )
    }
    //private val SERVER_TEST_API = "http://192.168.1.10/api/test"
    /*private fun getMail(): Stream<String> = Stream.of("myshansky@inbox.ru")
    @MethodSource("getMail")*/
    @ParameterizedTest
    @ValueSource(strings = ["myshansky@inbox.ru"]) // <- email для тестирования
    @DisplayName("Получение токена пользователя по email")
    //@Feature("Получение токена пользователя")
    fun getUserToken(email: String){
        val token = given()
            .get("$SERVER_TESTAPI/get_user_token?email=$email")
            .then()//.log().all()
            .extract().body().jsonPath().getString("token")
        println(" ")
        println(">>> token = $token")
        println(" ")
        Assertions.assertTrue(token.length > 1)
    }

    @Test
    @DisplayName("Получение списка продуктов")
    fun getProducts(){
        val token = "16a13d57dfaee75825ae07a8ce45e6e6"      // <- токен пользователя
        val page  = 3                                      // <- порция данных продуктов
        val response = given()
            //.contentType(ContentType.JSON)
            .pathParam("token", token)
            .pathParam("page", page)
            .get("$SERVER_TESTAPI/get_products?token={token}&page={page}")
            .then()
            //.extract().body().jsonPath().getList<Product>(".", Product::class.java)
            .extract().response()
        val jsonPath = response.jsonPath()
        val products = jsonPath.getList<Product>(".", Product::class.java)
        var success = false
        Assertions.assertAll(
            {
                success = products.isNotEmpty()
                if (!success)
                    println(">>> Продукты не загружены")
                Assertions.assertTrue(success)
            },
            {
                success = products.any { it.favorite > 0 }
                if (!success)
                    println(">>> У пользователя нет продуктов в избранном")
                Assertions.assertTrue(success)
            },
            {
                success = products.any { it.discount >= 2 }
                if (!success)
                    println(">>> Скидок нет")
                Assertions.assertTrue(success)
            }
        )
        /*/*
        проверка на наличие продуктов
        */
        success = products.isNotEmpty()
        if (!success)
            println(">>> Продукты не загружены")
        Assertions.assertTrue(success)

        /*
        проверка на избранные продукты
        */
        success = products.any { it.favorite > 0 }
        if (!success)
            println(">>> У пользователя нет продуктов в избранном")
        Assertions.assertTrue(success)


        /*
        проверка на продукты со скидкой
        */
        success = products.any { it.discount > 0 }
        if (!success)
            println(">>> Скидок нет")
        Assertions.assertTrue(success)*/
    }


}


/*
From now you just need to execute your test and after tests run gradle task allureReport
that creates Allure report or allureServethat creates Allure report and opens it
in the default browser.
*/

/*
Создаём новую конфигурацию запуска: Run — Edit Configurations… — + — Gradle.
Gradle project = kotlinallure, Tasks = clean test allureReport
*/