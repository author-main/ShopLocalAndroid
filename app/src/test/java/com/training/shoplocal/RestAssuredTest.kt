package com.training.shoplocal

import com.training.shoplocal.classes.SERVER_URL
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import io.restassured.path.json.JsonPath
import org.hamcrest.Matchers.notNullValue
import org.junit.Assert
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.matchesPattern
import org.hamcrest.Matchers.matchesRegex


class RestAssuredTest {
    /*
    Для отображения русской кодировки добавить в studio64.exe.vmoptions:
    -Dconsole.encoding=UTF-8
    -Dfile.encoding=UTF-8
    */
    private val SERVER_TEST_API = "http://192.168.1.10/api/test"
    @Test
    @DisplayName("Получение токена пользователя по email")
    fun getUserToken(){
        val token = given()
            .contentType(ContentType.JSON)
            .get("$SERVER_TEST_API/get_user_token?email=myshansky@inbox.ru")
            .then().log().all()
            .extract().body().jsonPath().getString("token")
        println()
        println(">>> token = $token")
        println()
        Assertions.assertTrue(token.length > 1)
    }
}