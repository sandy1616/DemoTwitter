package com.twitter.statuses;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.lessThan;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.twitter.common.RestUtilities;
import com.twitter.constants.EndPoints;
import com.twitter.constants.Path;

import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class UserTimelineTest {
	
	
	RequestSpecification reqSpec;
	ResponseSpecification resSpec;
	
	@BeforeClass
	public void setup() {
		reqSpec=RestUtilities.getRequestSpecification();
		reqSpec.queryParam("screen_name", "sandy16864");
		reqSpec.queryParam("count", "1");
		reqSpec.basePath(Path.STATUSES);
		
		resSpec= RestUtilities.getResponseSpecification();
		
	}
	
	@Test
	public void readTweet() {
		
		given()
	      .spec(reqSpec)
		//.spec(RestUtilities.createQueryParam(reqSpec, "count", "1"))
	.when()
	      .get(EndPoints.STATUSES_USER_TIMELINE)
	.then()
	      .log().all()
	      .spec(resSpec)
	      .body("user.name",hasItem("Sandeep"))
	      .body("entities.hashtags[0].text", hasItem("myfirstTweet"))
	      .body("entities.hashtags[0].size()",equalTo(1))
	      .body("entities.hashtags[0].size()",lessThan(2));
		
		
	}
	
	

}
