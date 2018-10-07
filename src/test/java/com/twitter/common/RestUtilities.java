package com.twitter.common;

import static org.hamcrest.Matchers.lessThan;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.twitter.constants.Auth;
import com.twitter.constants.Path;
import static io.restassured.RestAssured.given;

import io.restassured.RestAssured;
import io.restassured.authentication.AuthenticationScheme;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class RestUtilities {

	public static String ENDPOINT;
	public static RequestSpecBuilder REQUEST_BUILDER;
	public static ResponseSpecBuilder RESPONSE_BUILDER;
	public static RequestSpecification REQUEST_SPEC;
	public static ResponseSpecification RESPONSE_SPEC;

	/**
	 * Method to Set the Endpoint
	 * 
	 * @return
	 *
	 */
	public static void endPoint(String epoint) {
		ENDPOINT = epoint;
	}

	/**
	 * Method to Get Request Specification
	 * 
	 * @return
	 *
	 */

	public static RequestSpecification getRequestSpecification() {
		AuthenticationScheme authscheme = RestAssured.oauth(Auth.CONSUMER_KEY, Auth.CONSUMER_SECRET, Auth.ACCESS_TOKEN,
				Auth.TOKEN_SECRET);
		REQUEST_BUILDER = new RequestSpecBuilder();
		REQUEST_BUILDER.setBaseUri(Path.BASE_PATH);
		REQUEST_BUILDER.setAuth(authscheme);
		REQUEST_SPEC = REQUEST_BUILDER.build();
		return REQUEST_SPEC;
	}
	
	
	/**
	 * Method to Get Response Specification
	 * 
	 * @return
	 *
	 */

	public static ResponseSpecification getResponseSpecification() {
		
		RESPONSE_BUILDER= new ResponseSpecBuilder();
		RESPONSE_BUILDER.expectStatusCode(200);
		RESPONSE_BUILDER.expectResponseTime(lessThan(6L),TimeUnit.SECONDS);
		RESPONSE_SPEC=RESPONSE_BUILDER.build();
		return RESPONSE_SPEC;
	}
	
	/**
	 * Method to Create Query Param
	 * Basically query param will append to the existing request specification.
	 * @return
	 *
	 */
	
	public static RequestSpecification createQueryParam(RequestSpecification rspec , String param, String value) {
		
		return rspec.queryParam(param, value);
	}
	
	/**
	 * Method to Create Query Params , if there are multiple parameters to pass
	 * Basically query param will append to the existing request specification.
	 * @return
	 *
	 */
	
	public static RequestSpecification createQueryParam(RequestSpecification rspec , Map<String,String> querymap) {
		return rspec.queryParams(querymap);
	}
	
	
	
	/**
	 * Method to Create Path Param
	 * Basically query param will append to the existing request specification.
	 * @return
	 *
	 */
	
	public static RequestSpecification createpathParam(RequestSpecification rspec, String param , String value) {
		return rspec.pathParam(param, value);
	}
	
	
	/**
	 * 
	 * 
	 * This method Returns the Response
	 *
	 */

	public static Response getResponse() {
		return given().get(ENDPOINT);
		
	}
	
	

	public static Response getResponse(RequestSpecification reqspec, String type) {
		
		REQUEST_SPEC.spec(reqspec);
		
		Response response=null;
		
		if(type.equalsIgnoreCase("get")) {
			response=given().spec(REQUEST_SPEC).get(ENDPOINT);
			
		} else if(type.equalsIgnoreCase("post")) {
			response=given().spec(REQUEST_SPEC).post(ENDPOINT);
			
		} else if (type.equalsIgnoreCase("put")) {
			response=given().spec(REQUEST_SPEC).put(ENDPOINT);
			
		} else if(type.equalsIgnoreCase("delete")) {
			response=given().spec(REQUEST_SPEC).delete(ENDPOINT);
		} else {
			
			System.out.println("Type is not Supported");
		}
		
		response.then().log().all();
		response.then().spec(RESPONSE_SPEC);
		return response;
		
	}
	
	
	public static JsonPath getJsonPath(Response res) {
		
		String path= res.asString();
		return new JsonPath(path);
	}
	
    public static XmlPath getxmlPath(Response res) {
		
		String path= res.asString();
		return new XmlPath(path);
	  }
	
    
    public static void resetBasePath() {
    	
    	RestAssured.basePath=null;
    }
    
    public static void setContentType(ContentType type) {
    	
    	given().contentType(type);
    }
	
	
}
