package com.tests;

import org.apache.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class BookTests extends BaseClass {

	String isbn;

	@Test(groups = "auth")
	public void addBookToUser() {

		Response res = httpRequest.body(addBook()).when().post(baseURI + "BookStore/v1/Books").then().log().ifError().statusCode(HttpStatus.SC_CREATED).extract().response();

		isbn = JsonPath.from(res.asString()).get("books[0].isbn");

		System.out.println("isbn" + isbn);
	}

	@Test(groups = "auth")
	public void getBook() {

		Response res = httpRequest.when().queryParam("ISBN", isbn).get(baseURI + "BookStore/v1/Book").then().statusCode( HttpStatus.SC_OK).extract().response();
		String Actualisbn = JsonPath.from(res.asString()).get("isbn");
		Assert.assertEquals(Actualisbn, isbn);
	}

}
