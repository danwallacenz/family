package family.domain;

import static com.jayway.restassured.RestAssured.expect;
import static com.jayway.restassured.RestAssured.given;
import static groovyx.net.http.ContentType.JSON;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;
import groovyx.net.http.ContentType;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;

public class DeletingAPersonIT extends FuncAbstract{

    public void shouldReturnThePersonsIncrementedVersionAsJsonInTheResponseBody(){
    	fail("Not implemented yet");
    }
	
    @Test
    public void shouldReturnA200OKStatusCodeInTheResponseHeader(){
    	fail("Not implemented yet");
    }

    @Test
    public void shouldReturnThePersonsNameAsJsonInTheResponseBody(){
    	fail("Not implemented yet");
    }
   
    @Test
    public void shouldReturnThePersonsSexAsJsonInTheResponseBody(){
    	fail("Not implemented yet");
    }
    
    @Test
    public void shouldReturnThePersonsIdAsJsonInTheResponseBody(){
    	fail("Not implemented yet");
    }
    
	@Test
	public void shouldReturnA400BAD_REQUESTStatusCodeWhenTheJsonInTheRequestBodyIsInvalid() {
		fail("fix");
		
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
	public void shouldReturnA406NOT_ACCEPTABLEStatusCodeWhenTheJsonInTheRequestBodyDoesNotContainAnId() {
		
		fail("fix");
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
	public void shouldReturnA406NOT_ACCEPTABLEStatusCodeWhenTheJsonInTheRequestBodyDoesNotContainAVersion() {
		fail("fix");
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
    
	/**
     * Tests creation of a new <code>Person</code> from a JSON <code>String</code> 
     * and validate the HTTP response code (201 CREATED) and the LOCATION header.
     * @throws Exception
     */
    @Test
    public void ensureThatCreatingOnePersonFromJSONIsCorrect() throws Exception {
	
		RestAssured.requestContentType(JSON);
		try {
			given().header("Accept", "application/json")
					.body("{ \"name\" : \"johnny\"}").then().expect()
					.statusCode(201).and().response()
					.header("Location", containsString(APP_URL)).when()
					.post("/family/people");
		} finally {
			RestAssured.reset();
		}
    }
    
	/**
     * Tests creation of a new <code>Person</code> from a JSON <code>String</code> 
     * and validate the HTTP response code (201 CREATED), the Response body,
     * and the LOCATION header.
     * @throws Exception
     */
    @Test
    public void ensureThatCreatingAPersonReturnsTheCorrectJsonInTheResponseBody() throws Exception {

        Response response = responseOfPosted("{ \"name\" : \"Daniel Wallace\",\"sex\":\"MALE\"}");
    	
        String location = response.headers().getValue("Location");
    	String newId = location.substring(location.lastIndexOf("/") + 1);
    	
        String expectedJSON = "{" +
        		"\"id\":" + newId + "," +	
        		"\"name\":\"Daniel Wallace\"," +
          		"\"version\":0," +
          		"\"sex\":\"MALE\"," +
          		"\"father\":\"null\"," +
          		"\"mother\":\"null\"," +
          		"\"children\":[]," +
          		"\"links\":" +
          			"[" +
        	  			"{" +
        	  				"\"rel\":\"self\"," +
        	  				"\"href\":\"http://localhost:8080/family/people/" + newId + "\"," +
        	  				"\"title\":\"Daniel Wallace\"" +
        	  			"}," +
        	   			"{" +
        	  				"\"rel\":\"father\"," +
        	  				"\"href\":\"http://localhost:8080/family/people/" + newId + "/father\"," +
        	  				"\"title\":\"Father\"" +
        	  			"}," +
        	   			"{" +
        	  				"\"rel\":\"mother\"," +
        	  				"\"href\":\"http://localhost:8080/family/people/" + newId + "/mother\"," +
        	  				"\"title\":\"Mother\"" +
        	  			"}," +
        	  			"{" +
        	  				"\"rel\":\"children\"," +
        	  				"\"href\":\"http://localhost:8080/family/people/" + newId + "/children\"," +
        	  				"\"title\":\"Children\"" +
        	  			"}" +
          		 	"]" +
        	"}";
    	     
    	assertEquals(expectedJSON, response.body().asString());
    }
    
	/**
	 * Tests getting a <code>Person</code>.
	 * <p>curl -i -H "Accept: application/json" http://localhost:8080/family/people/5
	 * @throws Exception
	 */	
    @Test
    public void ensureThatASimpleSaveAndReadAreCorrect() throws Exception {
    	String personAsJson ="{ \"name\" : \"jonathan\"}";
    	String newId = idOfPosted(personAsJson);
    	expect().log().all().body("name", equalTo("jonathan")).given().header("Accept", "application/json").when().get("/family/people/" + newId);     
    }
    
	/**
	 * Tests updating a single<code>Person</code>.
	 * @throws Exception
	 */	
    @Test
    public void ensureThatUpdatingAPersonIsCorrect() throws Exception {
    	
    	// create a test Person "dan2"
    	String personAsJson ="{ \"name\" : \"dan2\"}";    	
    	String newId = idOfPosted(personAsJson);
    	
    	// update him
    	String newName = "dan3";
    	String updatedPersonAsJson 
    		= "{\"class\":\"family.domain.Person\",\"father\":null,\"id\":" 
    			+ newId + ",\"mother\":null,\"name\":\""+newName+"\",\"version\":0}";
		RestAssured.requestContentType(JSON);
		try {
			given().header("Accept", "application/json")					
					.body(updatedPersonAsJson).then().expect()
					.statusCode(200)
					.and().response().header("Location", containsString("/family/people/" + newId))
					.when().put("/family/people/" + newId);
		} finally {
			RestAssured.reset();
		}
		
		// verify name and version have changed
		try {	
			expect().log().all().body("name", equalTo(newName))
			.given().header("Accept", "application/json").when().get("/family/people/" + newId); 
		} finally {
			RestAssured.reset();
		}
	}
   
	/**
	 * Tests updating a single<code>Person</code>.
	 * @throws Exception
	 */	
    @Test
    public void ensureThatRepeatedUpdatesToAPersonAreCorrect() throws Exception {
    	// create a test Person "dan2"
    	String personAsJson ="{ \"name\" : \"dan2\"}";    	
    	String newId = idOfPosted(personAsJson);
    	
    	// update him
    	// BEWARE that if no valid i.e., existing id, is present in the JSON, then
    	// then dan3 will be created rather than updated.
    	int version = 0;
    	String updatedPersonAsJson 
    		= "{\"id\":"+ newId +",\"name\":\"dan3\",\"version\":" + version + "}";

		RestAssured.requestContentType(JSON);
		try {
			given().header("Accept", "application/json")
					//.body("{\"id\" : \"" + newId +"\", \"name\" : \"dan3\",}").then().expect()
					.body(updatedPersonAsJson).then().expect()
					.statusCode(200)
					.and().response().header("Location", containsString("/family/people/" + newId))
					.when().put("/family/people/" + newId);
		} finally {
			RestAssured.reset();
		}
		
		try {
			// verify name and version have changed
			expect().log().all().body("name", equalTo("dan3")).and().that().body("version", equalTo(++version))
			.given().header("Accept", "application/json").when().get("/family/people/" + newId); 
		} finally {
			RestAssured.reset();
		}
		
    	// update him again
    	// BEWARE that if no valid i.e., existing id, is present in the JSON, then
    	// then dan3 will be created rather than updated.
		// NB increment the version
    	String reUpdatedPersonAsJson  
    		= "{\"id\":" + newId+ ",\"name\":\"dan4\",\"version\":" + version + "}";

		RestAssured.requestContentType(JSON);
		try {
			given().header("Accept", "application/json")
					//.body("{\"id\" : \"" + newId +"\", \"name\" : \"dan3\",}").then().expect()
					.body(reUpdatedPersonAsJson).then().expect()
					.statusCode(200)
					.and().response().header("Location", containsString("/family/people/" + newId))
					.when().put("/family/people/" + newId);
		} finally {
			RestAssured.reset();
		}
		
		try {
			// verify name and version have changed
			expect().log().all().body("name", equalTo("dan4")).and().that().body("version", equalTo(++version))
			.given().header("Accept", "application/json").when().get("/family/people/" + newId); 
		} finally {
			RestAssured.reset();
		}
    }
    
	/**
	 * Tests updating a single<code>Person</code>.
	 * @throws Exception
	 */	
    @Test
    public void ensureThatVersionIsIncrementedAfterAPersonIsUpdated() throws Exception {
    	
    	int version = 0;
    	
    	// create a test Person.
		String personJson = "{ \"name\" : \"Daniel Roy Wallace\",\"sex\" : \"MALE\"}";
		
		// POST person and retrieve the new id
    	String newId = idOfPosted(personJson);
    	
    	// update person with id, a new name, and version = 0
    	String newName = "Danny Wallace";
		
    	String personUpdatedToJson 
    		= "{\"id\" : " + newId + ",\"name\" : \"" + newName + "\",\"sex\" : \"MALE\", \"version\" : " + version + "}";
    	
		// PUT (update) person
		RestAssured.requestContentType(JSON);
		try {
			given().header("Accept", "application/json")					
					.body(personUpdatedToJson).then().expect()
					.statusCode(200)
					.and().response().header(
							"Location", containsString("/family/people/" + newId))
					.when().put("/family/people/" + newId);
		} finally {
			RestAssured.reset();
		}
				
		// verify name and version have changed by GETting person.
		try {	
			expect().log().all()
			.that().body("name", equalTo(newName))
			.and().that().body("version", equalTo(++version))
			.given().header("Accept", "application/json")
			.when().get("/family/people/" + newId); 
		} finally {
			RestAssured.reset();
		}
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
}
