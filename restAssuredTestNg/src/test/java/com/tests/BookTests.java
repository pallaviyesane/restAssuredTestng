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

		Response res = httpRequest.when().body(addBook()).post(baseURI + "BookStore/v1/Books").thenReturn();

		isbn = JsonPath.from(res.asString()).get("books[0].isbn");
		Assert.assertEquals(res.getStatusCode(), HttpStatus.SC_CREATED);

		System.out.println("isbn" + isbn);
	}

	@Test(groups = "auth")
	public void getBook() {

		Response res = httpRequest.when().queryParam("ISBN", isbn).get(baseURI + "BookStore/v1/Book");
		Assert.assertEquals(res.getStatusCode(), HttpStatus.SC_OK);
	}

}
