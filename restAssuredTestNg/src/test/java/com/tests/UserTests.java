package com.tests;

import org.apache.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.pojo.request.User;
import com.pojo.response.UserRes;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

public class UserTests {
	RequestSpecification httpRequest;
	static String baseURI = "https://bookstore.toolsqa.com/";
	String userId;

	@BeforeMethod
	public void init() {
		RestAssured.baseURI = baseURI;
		httpRequest = RestAssured.given();
	}

	@Test(priority = 1)
	public void createUserTest() {
		UserRes user = httpRequest.when().contentType("application/json").body(createUser())
				.post(baseURI + "Account/v1/User").as(UserRes.class);

		userId = user.getUserID();
		
		Assert.assertTrue(!user.getUserID().isEmpty());
	}

	@Test(priority = 2)
	public void deleteUserTest() {
		httpRequest.auth().preemptive().basic(createUser().getUserName(), createUser().getPassword()).when()
				.delete(baseURI + "Account/v1/User/" + userId).then().statusCode(HttpStatus.SC_NO_CONTENT);
	}

	public User createUser() {
		User user = new User();
		user.setUserName("Rashmi8");
		user.setPassword("Test@1234");
		return user;
	}

}
