package com.tests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.testng.annotations.AfterGroups;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.BeforeSuite;

import com.pojo.request.User;
import com.pojo.response.UserRes;
import com.utility.ExtendReport;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class BaseClass {
	static RequestSpecification httpRequest;

	static String baseURI = "https://bookstore.toolsqa.com/";
	static public String userId;

	@BeforeSuite(alwaysRun = true)
	public void init() throws IOException {
		httpRequest = RestAssured.given().contentType(ContentType.JSON);
		RestAssured.baseURI = baseURI;
		System.out.println("BeforeSuite");

		// Reporting
		ExtendReport.setExtent();
	}

	@BeforeGroups(value = "auth")
	public void createUserBeforeTests() {
		UserRes user = httpRequest.when().body(createUser()).post(baseURI + "Account/v1/User").as(UserRes.class);
		userId = user.getUserID();

		httpRequest = httpRequest.auth().preemptive().basic(createUser().getUserName(), createUser().getPassword());

		System.out.println("beforeGroup");
		System.out.println("user : " + user.getUserID());

	}

	@AfterGroups(value = "auth")
	public void createUserAfterTests() {
		// Delete User
		httpRequest.when().delete(baseURI + "Account/v1/User/" + userId).then().log().all().assertThat()
				.statusCode(HttpStatus.SC_NO_CONTENT);
		System.out.println("AfterGroup");
	}

	@AfterSuite
	public void teardown() {
		ExtendReport.endReport();

	}

	public User createUser() {
		User user = new User();
		user.setUserName("Rashmi11");
		user.setPassword("Test@1234");
		return user;
	}

	public Map<String, Object> addBook() {
		Map<String, Object> isbn = new HashMap<String, Object>();
		isbn.put("isbn", "9781449325862");

		List<Map<String, Object>> collectionOfIsbns = new ArrayList<Map<String, Object>>();
		collectionOfIsbns.add(isbn);

		Map<String, Object> book = new HashMap<String, Object>();
		book.put("userId", userId);
		book.put("collectionOfIsbns", collectionOfIsbns);

		return book;
	}
}
