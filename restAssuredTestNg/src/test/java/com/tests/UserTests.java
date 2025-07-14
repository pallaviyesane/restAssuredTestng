package com.tests;

import org.apache.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.pojo.response.UserRes;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class UserTests extends BaseClass {
	String tempUserId;
	
	@Test(priority = 1, groups= "user")
	public void createUserTest() {
		// Created user with serilization and get response using deserilization
		// POJO class

		UserRes user = httpRequest.when().body(createUser()).post(baseURI + "Account/v1/User").as(UserRes.class);

		tempUserId = user.getUserID();

		Assert.assertTrue(!user.getUserID().isEmpty());

		// =========================== Below is the response verification using JsonPath
		// liabrary

		// Response res = httpRequest.when().body(createUser())
		// .post(baseURI + "Account/v1/User");

		// userId = JsonPath.from(res.asString()).get("userID");
		System.out.println("createUser");
	}

	@Test(priority = 2, groups= "user")
	public void getUserTest() {
		Response res = httpRequest.auth().preemptive().basic(createUser().getUserName(), createUser().getPassword())
				.when().get(baseURI + "Account/v1/User/" + tempUserId);

		String userIdExpected = JsonPath.from(res.asString()).get("userId");

		Assert.assertEquals(res.getStatusCode(),HttpStatus.SC_OK);
		Assert.assertEquals(userIdExpected, tempUserId);
		System.out.println("getUser");

	}

	@Test(priority = 3, groups= "user")
	public void deleteUserTest() {
		//Delete User
		httpRequest.auth().preemptive().basic(createUser().getUserName(), createUser().getPassword()).when()
				.delete(baseURI + "Account/v1/User/" + tempUserId).then().statusCode(HttpStatus.SC_NO_CONTENT);

		//Get User should retrive error because same user with provided userId already deleted
		Response res = httpRequest.auth().preemptive().basic(createUser().getUserName(), createUser().getPassword())
				.when().get(baseURI + "Account/v1/User/" + tempUserId);

		String codeExpected = JsonPath.from(res.asString()).get("code");
		String messageExpected = JsonPath.from(res.asString()).get("message");

		Assert.assertEquals(codeExpected, "1207");
		Assert.assertEquals(messageExpected, "User not found!");
		System.out.println("deleteUser");


	}

}
