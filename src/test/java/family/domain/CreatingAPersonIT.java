package family.domain;

import static com.jayway.restassured.RestAssured.expect;
import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.config.RestAssuredConfig.config;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static com.jayway.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;	

import java.io.PrintStream;
import java.io.StringWriter;

import org.apache.commons.io.output.WriterOutputStream;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNot;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import groovyx.net.http.ContentType;
import org.springframework.http.HttpStatus;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.config.LogConfig;
import com.jayway.restassured.response.Response;

public class CreatingAPersonIT extends FuncAbstract {

	@Test
	public void shouldReturnA201CREATEDStatusCode() { 
		given().log().all()
			.header("Accept", "application/json")
			.and().body("{ \"name\" : \"Daniel Roy Wallace\",\"sex\" : \"MALE\"}")
			.with().contentType(ContentType.JSON)
		.then().expect().log().all()
			.statusCode(201)
		.when().post(APP_URL);
	}

	@Test
	public void shouldReturnAUriIdentifyingThatPersonInTheLocationHeader() {
		
		// Ensure that the response location header contains APP_URL
		given()
			.header("Accept", "application/json")
			.and().body("{ \"name\" : \"Daniel Roy Wallace\",\"sex\" : \"MALE\"}")
			.with().contentType(ContentType.JSON)
		.then().expect()
			.response().header("Location", containsString(APP_URL))
		.when().post(APP_URL);
		
		// Ensure that the Response Location header has the new id number appended
        final String locationHeaderValue = 
	        given()
				.header("Accept", "application/json")
				.and().body("{ \"name\" : \"Daniel Roy Wallace\",\"sex\" : \"MALE\"}")
				.with().contentType(ContentType.JSON)
			.when().post(APP_URL)
				.headers().getValue("Location");
        
        // Ensure that something is appended
        assertThat(locationHeaderValue.length(), is(greaterThan(APP_URL.length())));
        // and that it is digits
        String appendedToAPP_URL 
        	= locationHeaderValue.substring(APP_URL.length() + 1, locationHeaderValue.length());
        
        assertTrue("Person id appended in location header value should be numeric but is'" + appendedToAPP_URL + "'.",
        		StringUtils.isNumeric(appendedToAPP_URL));
	}
	
	@Test
	public void shouldWriteDataToTheDatabase() {
		
		// Create a Person in the database
		String personAsJson = 
			"{ \"name\" : \"Daniel Roy Wallace\",\"sex\" : \"MALE\"}";

		String newId = idOfPosted(personAsJson);
		
		// Read him back from the server
		expect().log().all()
		.that().body("name", equalTo("Daniel Roy Wallace"))
		.and().that().body("version", equalTo(0))
		.and().that().body("sex", equalTo("MALE"))
		.and().that().body("version", equalTo(0))
		.given().header("Accept", "application/json")
		.when().get(appUrl() + "/" + newId); 
	}
	
	@Test
	public void shouldWriteDOBDataToTheDatabase() {
		
		// TODO fix date format
		// Create a Person in the database
		String personAsJson = 
				
			"{ \"name\" : \"Daniel Roy Wallace\",\"sex\" : \"MALE\",\"dob\" : \"06/27/1957\"}";// This is a problem

		String newId = idOfPosted(personAsJson);
		
		// Read him back from the server
		expect().log().all()
		.that().body("name", equalTo("Daniel Roy Wallace"))
		.and().that().body("version", equalTo(0))
		.and().that().body("dob", equalTo("27/06/1957"))
		.given().header("Accept", "application/json")
		.when().get(appUrl() + "/" + newId); 
	}
	
	@Test
	public void shouldWriteDODDataToTheDatabase() {
		
		// TODO fix date format
		// Create a Person in the database
		String personAsJson = 
				
			"{ \"name\" : \"Daniel Roy Wallace\",\"sex\" : \"MALE\",\"dod\" : \"06/27/1957\"}";// This is a problem

		String newId = idOfPosted(personAsJson);
		
		// Read him back from the server
		expect().log().all()
		.that().body("name", equalTo("Daniel Roy Wallace"))
		.and().that().body("version", equalTo(0))
		.and().that().body("dod", equalTo("27/06/1957"))
		.given().header("Accept", "application/json")
		.when().get(appUrl() + "/" + newId); 
	}
	
	@Test
	public void shouldReturnA400BAD_REQUESTWhenDODIsInTheFuture() {
		
		// TODO fix date format
		// Create a Person in the database
		String personAsJsonInvalidDOD = 
				
			"{ \"name\" : \"Daniel Roy Wallace\",\"sex\" : \"MALE\",\"dod\" : \"06/27/2057\"}";// This is a problem

		Response expectedErrorResponse 
		= given()
			.header("Accept", "application/json")
			.and().body(personAsJsonInvalidDOD)
			.with().contentType(ContentType.JSON)
			.log().all()
		.then()
		.expect().that()
		.body("exception.code",equalTo(HttpStatus.BAD_REQUEST.value()))
		.body("exception.message",containsString("2057 must be in the past"))
			.log().all()
		.when().post(APP_URL);
		 
		System.out.println(expectedErrorResponse.asString());
	}
	
	@Test
	public void shouldWritePlaceOfBirthDataToTheDatabase() {
		
		// Create a Person in the database
		String personAsJson = 
				
			"{ \"name\" : \"Daniel Roy Wallace\",\"sex\" : \"MALE\",\"placeOfBirth\" : \"Te Awamutu, New Zealand\"}";

		String newId = idOfPosted(personAsJson);
		
		// Read him back from the server
		expect().log().all()
		.that().body("name", equalTo("Daniel Roy Wallace"))
		.and().that().body("version", equalTo(0))
		.and().that().body("placeOfBirth", equalTo("Te Awamutu, New Zealand"))
		.given().header("Accept", "application/json")
		.when().get(appUrl() + "/" + newId); 
	}
	
	@Test
	public void shouldWritePlaceOfDeathDataToTheDatabase() {
		
		// Create a Person in the database
		String personAsJson = 
				
			"{ \"name\" : \"Daniel Roy Wallace\",\"sex\" : \"MALE\",\"placeOfDeath\" : \"Unknown\"}";

		String newId = idOfPosted(personAsJson);
		
		// Read him back from the server
		expect().log().all()
		.that().body("name", equalTo("Daniel Roy Wallace"))
		.and().that().body("version", equalTo(0))
		.and().that().body("placeOfDeath", equalTo("Unknown"))
		.given().header("Accept", "application/json")
		.when().get(appUrl() + "/" + newId); 
	}

	@Test
	public void shouldReturnThatPersonAsJsonInTheResponseBody() {
		given()
			.header("Accept", "application/json")
			.and().body("{ \"name\" : \"Daniel Roy Wallace\",\"sex\" : \"MALE\"}")
			.with().contentType(ContentType.JSON)
			.log().all()
		.then()
		.expect().that().body("name", equalTo("Daniel Roy Wallace"))
			.and().that().body("sex", equalTo("MALE"))
			.and().that().body("id", greaterThan(0))
			.and().that().body("version", equalTo(0))
			.and().that().body("father", equalTo("null"))
			.and().that().body("mother", equalTo("null"))
			.and().that().body("children.size()", equalTo(0))
			.and().that().body("links.size()", equalTo(4))
			.and().that().body("links.getAt(0).rel", equalTo("self"))
			.and().that().body("links.getAt(0).href", containsString(APP_URL + "/"))
			.and().that().body("links.getAt(0).href.length()", greaterThan(APP_URL.length() + 1))
			.and().that().body("links.getAt(0).title", equalTo("Daniel Roy Wallace"))
			.and().that().body("links.getAt(1).rel", equalTo("father"))
			.and().that().body("links.getAt(1).href", containsString(APP_URL + "/"))
			.and().that().body("links.getAt(1).href", containsString("/father"))
			.and().that().body("links.getAt(1).href.length()", greaterThan(APP_URL.length() + "/father".length()))
			.and().that().body("links.getAt(1).title", equalTo("Father"))
			.and().that().body("links.getAt(2).rel", equalTo("mother"))
			.and().that().body("links.getAt(2).href", containsString(APP_URL + "/"))
			.and().that().body("links.getAt(2).href", containsString("/mother"))
			.and().that().body("links.getAt(2).href.length()", greaterThan(APP_URL.length() + "/mother".length()))
			.and().that().body("links.getAt(2).title", equalTo("Mother"))
			.and().that().body("links.getAt(3).rel", equalTo("children"))
			.and().that().body("links.getAt(3).href", containsString(APP_URL + "/"))
			.and().that().body("links.getAt(3).href", containsString("/children"))
			.and().that().body("links.getAt(3).href.length()", greaterThan(APP_URL.length() + "/children".length()))
			.and().that().body("links.getAt(3).title", equalTo("Children"))
			.log().all()
		.when().post(APP_URL);
	}
	
	@Test
	public void shouldReturnThatPersonsIdAsJsonInTheResponseBody() {
		given()
		.header("Accept", "application/json")
		.and().body("{ \"name\" : \"Daniel Roy Wallace\",\"sex\" : \"MALE\"}")
		.with().contentType(ContentType.JSON)
		.log().all()
	.then()
	.expect().that().body("name", equalTo("Daniel Roy Wallace"))
		.and().that().body("id", greaterThan(0))
		.log().all()
	.when().post(APP_URL);
	}
	
	@Test
	public void shouldReturnThatPersonsNameAsJsonInTheResponseBody() {
		given()
		.header("Accept", "application/json")
		.and().body("{ \"name\" : \"Daniel Roy Wallace\",\"sex\" : \"MALE\"}")
		.with().contentType(ContentType.JSON)
		.log().all()
	.then()
	.expect().that().body("name", equalTo("Daniel Roy Wallace"))
		.log().all()
	.when().post(APP_URL);
	}
	
	@Test
	public void shouldReturnThatPersonsVersionEqualToZeroAsJsonInTheResponseBody() {
		given()
		.header("Accept", "application/json")
		.and().body("{ \"name\" : \"Daniel Roy Wallace\",\"sex\" : \"MALE\"}")
		.with().contentType(ContentType.JSON)
		.log().all()
	.then()
	.expect().that().body("version", equalTo(0))
		.log().all()
	.when().post(APP_URL);
	}
	
	@Test
	public void shouldReturnThatPersonsSexAsJsonInTheResponseBody() {
		given()
		.header("Accept", "application/json")
		.and().body("{ \"name\" : \"Daniel Roy Wallace\",\"sex\" : \"MALE\"}")
		.with().contentType(ContentType.JSON)
		.log().all()
		.then()
		.expect().that().body("sex", equalTo("MALE"))
	
			.log().all()
		.when().post(APP_URL);
	}
	
	@Test
	public void shouldReturnThatPersonsFatherIsNullAsJsonInTheResponseBody() {
		given()
			.header("Accept", "application/json")
			.and().body("{ \"name\" : \"Daniel Roy Wallace\",\"sex\" : \"MALE\"}")
			.with().contentType(ContentType.JSON)
			.log().all()
		.then()
		.expect().that().body("father", equalTo("null"))
	
			.log().all()
		.when().post(APP_URL);
	}
	
	public void shouldReturnThatPersonsMotherIsNullAsJsonInTheResponseBody() {
		given()
			.header("Accept", "application/json")
			.and().body("{ \"name\" : \"Daniel Roy Wallace\",\"sex\" : \"MALE\"}")
			.with().contentType(ContentType.JSON)
			.log().all()
		.then()
		.expect().that().body("mother", equalTo("null"))
	
			.log().all()
		.when().post(APP_URL);
	}
	
	@Test
	public void shouldReturnThatPersonsChildrenIsEmptyAsJsonInTheResponseBody() {
		given()
			.header("Accept", "application/json")
			.and().body("{ \"name\" : \"Daniel Roy Wallace\",\"sex\" : \"MALE\"}")
			.with().contentType(ContentType.JSON)
			.log().all()
		.then()
		.expect().that().body("children.size()", equalTo(0))
			.log().all()
		.when().post(APP_URL);
	}
	
	@Test
	public void shouldReturnFourLinksForThatPersonInTheResponseBody() {
		given()
			.header("Accept", "application/json")
			.and().body("{ \"name\" : \"Daniel Roy Wallace\",\"sex\" : \"MALE\"}")
			.with().contentType(ContentType.JSON)
			.log().all()
		.then()
		.expect().that().body("links.size()", equalTo(4))
			.log().all()
		.when().post(APP_URL);
	}
	
	@Test
	public void shouldReturnALinkToThatPersonsSelfAsJsonInTheResponseBody() {
		given()
			.header("Accept", "application/json")
			.and().body("{ \"name\" : \"Daniel Roy Wallace\",\"sex\" : \"MALE\"}")
			.with().contentType(ContentType.JSON)
			.log().all()
		.then()
		.expect().that().body("links.getAt(0).rel", equalTo("self"))
			.and().that().body("links.getAt(0).href", containsString(APP_URL + "/"))
			.and().that().body("links.getAt(0).href.length()", greaterThan(APP_URL.length() + 1))
			.and().that().body("links.getAt(0).title", equalTo("Daniel Roy Wallace"))
			.log().all()
		.when().post(APP_URL);
	}
	
	@Test
	public void shouldReturnALinkToThatPersonsFatherAsJsonInTheResponseBody() {
		given()
			.header("Accept", "application/json")
			.and().body("{ \"name\" : \"Daniel Roy Wallace\",\"sex\" : \"MALE\"}")
			.with().contentType(ContentType.JSON)
			.log().all()
		.then()
		.expect().that().body("links.getAt(2).rel", equalTo("mother"))
			.and().that().body("links.getAt(2).href", containsString(APP_URL + "/"))
			.and().that().body("links.getAt(2).href", containsString("/mother"))
			.and().that().body("links.getAt(2).href.length()", greaterThan(APP_URL.length() + "/mother".length()))
			.and().that().body("links.getAt(2).title", equalTo("Mother"))
			.log().all()
		.when().post(APP_URL);
	}
	
	@Test
	public void shouldReturnALinkToThatPersonsMotherAsJsonInTheResponseBody() {
		given()
			.header("Accept", "application/json")
			.and().body("{ \"name\" : \"Daniel Roy Wallace\",\"sex\" : \"MALE\"}")
			.with().contentType(ContentType.JSON)
			.log().all()
		.then()
		.expect().that().body("links.getAt(1).rel", equalTo("father"))
		.and().that().body("links.getAt(1).href", containsString(APP_URL + "/"))
		.and().that().body("links.getAt(1).href", containsString("/father"))
		.and().that().body("links.getAt(1).href.length()", greaterThan(APP_URL.length() + "/father".length()))
		.and().that().body("links.getAt(1).title", equalTo("Father"))
			.log().all()
		.when().post(APP_URL);
	}
	
	@Test
	public void shouldReturnALinkToThatPersonsChildrenAsJsonInTheResponseBody() {
		given()
			.header("Accept", "application/json")
			.and().body("{ \"name\" : \"Daniel Roy Wallace\",\"sex\" : \"MALE\"}")
			.with().contentType(ContentType.JSON)
			.log().all()
		.then()
		.expect().that().body("links.getAt(3).rel", equalTo("children"))
			.and().that().body("links.getAt(3).href", containsString(APP_URL + "/"))
			.and().that().body("links.getAt(3).href", containsString("/children"))
			.and().that().body("links.getAt(3).href.length()", greaterThan(APP_URL.length() + "/children".length()))
			.and().that().body("links.getAt(3).title", equalTo("Children"))
			.log().all()
		.when().post(APP_URL);
	}
	
	@Test
	public void shouldReturnA400BAD_REQUESTStatusCodeWhenTheJsonInTheRequestBodyIsInvalid() {
		String invalidJson = "{ \"name : \"Daniel Roy Wallace\",\"sex\" : \"MALE\"}";
		given()
			.header("Accept", "application/json")
			.and().body(invalidJson)
			.with().contentType(ContentType.JSON)
			.log().all()
		.then()
		.expect().that().statusCode(HttpStatus.BAD_REQUEST.value()) 
			.log().all()
		.when().post(APP_URL);
	}
	
	@Test
	public void shouldReturnA400BAD_REQUESTStatusCodeInTheResponseErrorJsonWhenTheJsonInTheRequestBodyIsInvalid() {
		String invalidJson = "{ \"name : \"Daniel Roy Wallace\",\"sex\" : \"MALE\"}";
		given()
			.header("Accept", "application/json")
			.and().body(invalidJson)
			.with().contentType(ContentType.JSON)
			.log().all()
		.then()
		.expect().that()
		.body("exception.code",equalTo(HttpStatus.BAD_REQUEST.value())) 
			.log().all()
		.when().post(APP_URL);
	}
	
	@Test
	public void shouldReturnA406NOT_ACCEPTABLEStatusCodeWhenTheJsonInTheRequestBodyContainsAnId() {
		String invalidJson = "{ \"name\" : \"Grant Thomas Wallace\",\"sex\" : \"MALE\", \"id\" : 1}";
		given()
			.header("Accept", "application/json")
			.and().body(invalidJson)
			.with().contentType(ContentType.JSON)
			.log().all()
		.then()
		.expect().that().statusCode(HttpStatus.NOT_ACCEPTABLE.value()) 
			.log().all()
		.when().post(APP_URL);
		
		invalidJson = "{ \"name\" : \"Grant Thomas Wallace\",\"sex\" : \"MALE\", 'id' : 1}";
		given()
			.header("Accept", "application/json")
			.and().body(invalidJson)
			.with().contentType(ContentType.JSON)
			.log().all()
		.then()
		.expect().that().statusCode(HttpStatus.NOT_ACCEPTABLE.value()) 
			.log().all()
		.when().post(APP_URL);
	}
	
	@Test
	public void shouldReturnA406NOT_ACCEPTABLEStatusCodeInTheResponseErrorJsonWhenTheJsonInTheRequestBodyContainsAnId() {

		String invalidJson = "{ \"name\" : \"Grant Thomas Wallace\",\"sex\" : \"MALE\", \"id\" : 1}";
		given()
			.header("Accept", "application/json")
			.and().body(invalidJson)
			.with().contentType(ContentType.JSON)
			.log().all()
		.then()
		.expect().that()
		.body("exception.code",equalTo(HttpStatus.NOT_ACCEPTABLE.value()))  
			.log().all()
		.when().post(APP_URL);
		
		invalidJson = "{ \"name\" : \"Grant Thomas Wallace\",\"sex\" : \"MALE\", 'id' : 1}";
		given()
			.header("Accept", "application/json")
			.and().body(invalidJson)
			.with().contentType(ContentType.JSON)
			.log().all()
		.then()
		.expect().that()
		.body("exception.code",equalTo(HttpStatus.NOT_ACCEPTABLE.value()))  
			.log().all()
		.when().post(APP_URL);
	}
	
	@Test
	public void shouldReturnA406NOT_ACCEPTABLEStatusCodeWhenTheJsonInTheRequestBodyContainsAVersion() {
		// Double quoted version
		String invalidJson = "{ \"name\" : \"Grant Thomas Wallace\",\"sex\" : \"MALE\", \"version\" : 1}";
		given()
			.header("Accept", "application/json")
			.and().body(invalidJson)
			.with().contentType(ContentType.JSON)
			.log().all()
		.then()
		.expect().that().statusCode(HttpStatus.NOT_ACCEPTABLE.value()) 
			.log().all()
		.when().post(APP_URL);
		
		// Single quotes
		invalidJson = "{ \"name\" : \"Grant Thomas Wallace\",\"sex\" : \"MALE\", 'version' : 1}";
		given()
			.header("Accept", "application/json")
			.and().body(invalidJson)
			.with().contentType(ContentType.JSON)
			.log().all()
		.then()
		.expect().that().statusCode(HttpStatus.NOT_ACCEPTABLE.value()) 
			.log().all()
		.when().post(APP_URL);
	}
	@Test
	public void shouldReturnA406NOT_ACCEPTABLEStatusCodeInTheResponseErrorJsonWhenTheJsonInTheRequestBodyContainsAVersion() {
		// Double quoted version
		String invalidJson = "{ \"name\" : \"Grant Thomas Wallace\",\"sex\" : \"MALE\", \"version\" : 1}";
		given()
			.header("Accept", "application/json")
			.and().body(invalidJson)
			.with().contentType(ContentType.JSON)
			.log().all()
		.then()
		.expect().that()
		.body("exception.code",equalTo(HttpStatus.NOT_ACCEPTABLE.value())) 
			.log().all()
		.when().post(APP_URL);
		
		// Single quotes
		invalidJson = "{ \"name\" : \"Grant Thomas Wallace\",\"sex\" : \"MALE\", 'version' : 1}";
		given()
			.header("Accept", "application/json")
			.and().body(invalidJson)
			.with().contentType(ContentType.JSON)
			.log().all()
		.then()
		.expect().that()
		.body("exception.code",equalTo(HttpStatus.NOT_ACCEPTABLE.value()))  
			.log().all()
		.when().post(APP_URL);
	}
	
}
