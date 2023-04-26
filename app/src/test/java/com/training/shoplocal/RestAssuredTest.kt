package com.training.shoplocal

import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.provider.ValueSource
import java.util.stream.Stream


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
    @ValueSource(strings = ["myshansky@inbox.ru"]) // <- введите email для тестирования
    @DisplayName("Получение токена пользователя по email")
    fun getUserToken(email: String){
        val token = given()
            .contentType(ContentType.JSON)
            .get("$SERVER_TEST_API/get_user_token?email=$email")
            .then()//.log().all()
            .extract().body().jsonPath().getString("token")
        println()
        println(">>> token = $token")
        println()
        Assertions.assertTrue(token.length > 1)
    }
}