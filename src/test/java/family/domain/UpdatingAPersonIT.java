package family.domain;

import static com.jayway.restassured.RestAssured.expect;
import static com.jayway.restassured.RestAssured.given;
import static groovyx.net.http.ContentType.JSON;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.*;
import groovyx.net.http.ContentType;

import org.hamcrest.core.IsNull;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;

public class UpdatingAPersonIT extends FuncAbstract{

	@Test
    public void shouldReturnThePersonsIncrementedVersionAsJsonInTheResponseBody(){
    	// create a test Person "dan2"
    	String personAsJson ="{ \"name\" : \"dan2\"}";    	
    	String newId = idOfPosted(personAsJson);
    	
    	// update him
    	String newName = "dan3";
    	String updatedPersonAsJson 
    		= "{\"class\":\"family.domain.Person\",\"father\":null,\"id\":" 
    			+ newId + ",\"mother\":null,\"name\":\""+newName+"\",\"version\":0}";
//		RestAssured.requestContentType(JSON);
//		try {
			given().header("Accept", "application/json")					
					.body(updatedPersonAsJson)
					.with().contentType(JSON)
			.then().expect()
//					.statusCode(200)
//					.and().response().header("Location", containsString("/family/people/" + newId))
			.body("version", equalTo(1))
			.log().all()
			.when().put("/family/people/" + newId);
//		} finally {
//			RestAssured.reset();
//		}
		
		// verify name and version have changed
//		try {	
//			expect().log().all().body("name", equalTo(newName))
//			.given().header("Accept", "application/json").when().get("/family/people/" + newId); 
//		} finally {
//			RestAssured.reset();
//		}
    }
	
	@Test
    public void shouldReturnThePersonsURLInTheResponseLocationHeader(){
    	// create a test Person "dan2"
    	String personAsJson ="{ \"name\" : \"dan2\"}";    	
    	String newId = idOfPosted(personAsJson);
    	
    	// update him
    	String newName = "dan3";
    	String updatedPersonAsJson 
    		= "{\"class\":\"family.domain.Person\",\"father\":null,\"id\":" 
    			+ newId + ",\"mother\":null,\"name\":\""+newName+"\",\"version\":0}";
		//RestAssured.requestContentType(JSON);
		//try {
			given().header("Accept", "application/json")					
					.body(updatedPersonAsJson)
					.with().contentType(JSON)
			.then().expect()
//					.statusCode(200)
					.and().response().header("Location", containsString(APP_URL + "/" + newId))
//					.body("version", equalTo(1))
			.when().put("/family/people/" + newId);
//		} finally {
//			RestAssured.reset();
//		}
		
		// verify name and version have changed
//		try {	
//			expect().log().all().body("name", equalTo(newName))
//			.given().header("Accept", "application/json").when().get("/family/people/" + newId); 
//		} finally {
//			RestAssured.reset();
//		}
    }
	
	@Test
    public void shouldReturnThePersonsIdAsJsonInTheResponseBody(){

	    	// create a test Person "dan2"
	    	String personAsJson ="{ \"name\" : \"dan2\"}";    	
	    	String newId = idOfPosted(personAsJson);
	    	
	    	// update him
	    	String newName = "dan3";
	    	String updatedPersonAsJson 
	    		= "{\"class\":\"family.domain.Person\",\"father\":null,\"id\":" 
	    			+ newId + ",\"mother\":null,\"name\":\""+newName+"\",\"version\":0}";
//			RestAssured.requestContentType(JSON);
//			try {
				given().header("Accept", "application/json")					
						.body(updatedPersonAsJson)
						.with().contentType(JSON)
				.then().expect()
//						.statusCode(200)
//						.and().response().header("Location", containsString("/family/people/" + newId))
				.body("id", equalTo(new Integer(newId)))
				.log().all()
				.when().put("/family/people/" + newId);
//			} finally {
//				RestAssured.reset();
//			}
			
			// verify name and version have changed
//			try {	
//				expect().log().all().body("name", equalTo(newName))
//				.given().header("Accept", "application/json").when().get("/family/people/" + newId); 
//			} finally {
//				RestAssured.reset();
//			}
//	    }	RestAssured.reset();
//		}
    }
	
	@Test
	public void shouldReturnA200OKStatusCodeInTheResponseHeader() {
		
		// create a test Person "dan2"
		String personAsJson = "{ \"name\" : \"dan2\"}";
		String newId = idOfPosted(personAsJson);

		// update him
		String newName = "dan3";
		String updatedPersonAsJson = "{\"class\":\"family.domain.Person\",\"father\":null,\"id\":"
				+ newId
				+ ",\"mother\":null,\"name\":\""
				+ newName
				+ "\",\"version\":0}";

		given().header("Accept", "application/json").body(updatedPersonAsJson)
				.with().contentType(JSON).then().expect().statusCode(200)
				.when().put("/family/people/" + newId);

	}

    @Test
    public void shouldReturnThePersonsNameAsJsonInTheResponseBody(){
    	// create a test Person "dan2"
    	String personAsJson ="{ \"name\" : \"dan2\"}";    	
    	String newId = idOfPosted(personAsJson);
    	
    	// update him
    	String newName = "dan3";
    	String updatedPersonAsJson 
    		= "{\"class\":\"family.domain.Person\",\"father\":null,\"id\":" 
    			+ newId + ",\"mother\":null,\"name\":\""+newName+"\",\"version\":0}";
//		RestAssured.requestContentType(JSON);
//		try {
			given().header("Accept", "application/json")					
					.body(updatedPersonAsJson)
					.with().contentType(JSON)
			.then().expect()
//					.statusCode(200)
//					.and().response().header("Location", containsString("/family/people/" + newId))
			.body("name", equalTo(newName))
			.log().all()
			.when().put("/family/people/" + newId);
//		} finally {
//			RestAssured.reset();
//		}
		
		// verify name and version have changed
//		try {	
//			expect().log().all().body("name", equalTo(newName))
//			.given().header("Accept", "application/json").when().get("/family/people/" + newId); 
//		} finally {
//			RestAssured.reset();
//		}

    }
   
    @Test
    public void shouldReturnThePersonsSexAsJsonInTheResponseBody(){
    	// create a test Person "dan2"
    	String personAsJson ="{ \"name\" : \"dan2\", \"sex\" : \"MALE\"}";    	
    	String newId = idOfPosted(personAsJson);
    	
    	// update him
    	String newName = "dan3";
    	String updatedPersonAsJson 
    		= "{\"class\":\"family.domain.Person\",\"father\":null,\"id\":" 
    			+ newId + ",\"mother\":null,\"name\":\""+newName+"\", \"sex\" : \"MALE\", \"version\":0}";
//		RestAssured.requestContentType(JSON);
//		try {
			given().header("Accept", "application/json")					
					.body(updatedPersonAsJson)
					.with().contentType(JSON)
			.then().expect()
//					.statusCode(200)
//					.and().response().header("Location", containsString("/family/people/" + newId))
			.body("sex", equalTo("MALE"))
			.log().all()
			.when().put("/family/people/" + newId);
//		} finally {
//			RestAssured.reset();
//		}
		
		// verify name and version have changed
//		try {	
//			expect().log().all().body("name", equalTo(newName))
//			.given().header("Accept", "application/json").when().get("/family/people/" + newId); 
//		} finally {
//			RestAssured.reset();
//		}
    }
    
	@Test
	public void shouldReturnA400BAD_REQUESTStatusCodeWhenTheJsonInTheRequestBodyIsInvalid() {
		
    	// create a test Person "dan2"
    	String personAsJson ="{ \"name\" : \"dan2\", \"sex\" : \"MALE\"}";    	
    	String newId = idOfPosted(personAsJson);
    	
    	// update him
    	String newName = "dan3";
    	String updatedPersonAsInvalidJson 
    		= "{father\":null,\"id\":" 
    			+ newId + ",\"mother\":null,\"name\":\""+newName+"\", \"sex\" : \"MALE\", \"version\":0}";
		given().header("Accept", "application/json")					
				.body(updatedPersonAsInvalidJson)
				.with().contentType(JSON)
		.then().expect()
				.statusCode(400)
		.log().all()
		.when().put("/family/people/" + newId);
	}
	
	
	@Test
	public void shouldReturnA400NOT_ACCEPTABLEStatusCodeWhenTheJsonInTheRequestBodyDoesNotContainAVersion() {
    	// create a test Person "dan2"
    	String personAsJson ="{ \"name\" : \"dan2\", \"sex\" : \"MALE\"}";    	
    	String newId = idOfPosted(personAsJson);
    	
    	// update him
    	String newName = "dan3";
    	String updatedPersonAsInvalidJson 
    		= "{\"father\":null,\"id\":" 
    			+ newId + ",\"mother\":null,\"name\":\""+newName+"\", \"sex\" : \"MALE\"}";
		given().header("Accept", "application/json")					
				.body(updatedPersonAsInvalidJson)
				.with().contentType(JSON)
		.then().expect()
				.statusCode(400)
		.log().all()
		.when().put("/family/people/" + newId);

	}
    
	/**
     * Tests creation of a new <code>Person</code> from a JSON <code>String</code> 
     * and validate the HTTP response code (201 CREATED) and the LOCATION header.
     * @throws Exception
     */
    //@Test
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
    //@Test
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
    //@Test
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
		
	    	// create a test Person "dan2"
	    	String personAsJson ="{ \"name\" : \"dan2\"}";    	
	    	String newId = idOfPosted(personAsJson);
	    	
	    	// update him
	    	String newName = "dan3";
	    	String updatedPersonAsJson 
	    		= "{\"id\":" + newId + ",\"name\":\""+newName+"\",\"version\":0}";
			given().header("Accept", "application/json")					
						.body(updatedPersonAsJson)
						.with().contentType(JSON)
			.then().expect()
				.body("father", equalTo("null"))
				.log().all()
			.when().put("/family/people/" + newId);
	}
	
	public void shouldReturnThatPersonsMotherIsNullAsJsonInTheResponseBody() {

    	// create a test Person "dan2"
    	String personAsJson ="{ \"name\" : \"dan2\"}";    	
    	String newId = idOfPosted(personAsJson);
    	
    	// update him
    	String newName = "dan3";
    	String updatedPersonAsJson 
    		= "{\"id\":" + newId + ",\"name\":\""+newName+"\",\"version\":0}";
		given().header("Accept", "application/json")					
					.body(updatedPersonAsJson)
					.with().contentType(JSON)
		.then().expect()
			.body("mother", equalTo("null"))
			.log().all()
		.when().put("/family/people/" + newId);	
		}
	
	@Test
	public void shouldReturnThatPersonsChildrenIsEmptyAsJsonInTheResponseBody() {
    	// create a test Person "dan2"
    	String personAsJson ="{ \"name\" : \"dan2\"}";    	
    	String newId = idOfPosted(personAsJson);
    	
    	// update him
    	String newName = "dan3";
    	String updatedPersonAsJson 
    		= "{\"id\":" + newId + ",\"name\":\""+newName+"\",\"version\":0}";
    	
		given().header("Accept", "application/json")					
					.body(updatedPersonAsJson)
					.with().contentType(JSON)
		.then().expect()
			.body("children.size()", equalTo(0))
			.log().all()
		.when().put("/family/people/" + newId);	
	}
	
	@Test
	public void shouldReturnFourLinksForThatPersonInTheResponseBody() {
    	// create a test Person "dan2"
    	String personAsJson ="{ \"name\" : \"dan2\"}";    	
    	String newId = idOfPosted(personAsJson);
    	
    	// update him
    	String newName = "dan3";
    	String updatedPersonAsJson 
    		= "{\"id\":" + newId + ",\"name\":\""+newName+"\",\"version\":0}";
    	
		given().header("Accept", "application/json")					
					.body(updatedPersonAsJson)
					.with().contentType(JSON)
		.then().expect()
			.that().body("links.size()", equalTo(4))
			.log().all()
		.when().put("/family/people/" + newId);	
	}
	
	@Test
	public void shouldReturnALinkToThatPersonsSelfAsJsonInTheResponseBody() {
	   	// create a test Person "dan2"
    	String personAsJson ="{ \"name\" : \"dan2\"}";    	
    	String newId = idOfPosted(personAsJson);
    	
    	// update him
    	String newName = "dan3";
    	String updatedPersonAsJson 
    		= "{\"id\":" + newId + ",\"name\":\""+newName+"\",\"version\":0}";
    	
		given().header("Accept", "application/json")					
					.body(updatedPersonAsJson)
					.with().contentType(JSON)
		.then().expect().that().body("links.getAt(0).rel", equalTo("self"))
			.and().that().body("links.getAt(0).href", containsString(APP_URL + "/"))
			.and().that().body("links.getAt(0).href.length()", greaterThan(APP_URL.length() + 1))
			.and().that().body("links.getAt(0).title", equalTo("dan3"))
			.log().all()
			.when().put("/family/people/" + newId);	
	}
	
	@Test
	public void shouldReturnALinkToThatPersonsFatherAsJsonInTheResponseBody() {
    	// create a test Person "dan2"
    	String personAsJson ="{ \"name\" : \"dan2\"}";    	
    	String newId = idOfPosted(personAsJson);
    	
    	// update him
    	String newName = "dan3";
    	String updatedPersonAsJson 
    		= "{\"id\":" + newId + ",\"name\":\""+newName+"\",\"version\":0}";
    	
		given().header("Accept", "application/json")					
					.body(updatedPersonAsJson)
					.with().contentType(JSON)
		.then().expect().that()
			.body("links.getAt(2).rel", equalTo("mother"))
			.and().that().body("links.getAt(2).href", containsString(APP_URL + "/"))
			.and().that().body("links.getAt(2).href", containsString("/mother"))
			.and().that().body("links.getAt(2).href.length()", greaterThan(APP_URL.length() + "/mother".length()))
			.and().that().body("links.getAt(2).title", equalTo("Mother"))
			.log().all()
			.when().put("/family/people/" + newId);	
	}
	
	@Test
	public void shouldReturnALinkToThatPersonsMotherAsJsonInTheResponseBody() {
    	// create a test Person "dan2"
    	String personAsJson ="{ \"name\" : \"dan2\"}";    	
    	String newId = idOfPosted(personAsJson);
    	
    	// update him
    	String newName = "dan3";
    	String updatedPersonAsJson 
    		= "{\"id\":" + newId + ",\"name\":\""+newName+"\",\"version\":0}";
    	
		given().header("Accept", "application/json")					
					.body(updatedPersonAsJson)
					.with().contentType(JSON)
		.then().expect().that()
			.body("links.getAt(1).rel", equalTo("father"))
			.and().that().body("links.getAt(1).href", containsString(APP_URL + "/"))
			.and().that().body("links.getAt(1).href", containsString("/father"))
			.and().that().body("links.getAt(1).href.length()", greaterThan(APP_URL.length() + "/father".length()))
			.and().that().body("links.getAt(1).title", equalTo("Father"))
			.log().all()
		.when().put("/family/people/" + newId);	
	}
	
	@Test
	public void shouldReturnALinkToThatPersonsChildrenAsJsonInTheResponseBody() {
    	// create a test Person "dan2"
    	String personAsJson ="{ \"name\" : \"dan2\"}";    	
    	String newId = idOfPosted(personAsJson);
    	
    	// update him
    	String newName = "dan3";
    	String updatedPersonAsJson 
    		= "{\"id\":" + newId + ",\"name\":\""+newName+"\",\"version\":0}";
    	
		given().header("Accept", "application/json")					
					.body(updatedPersonAsJson)
					.with().contentType(JSON)
		.then().expect().that()
			.body("links.getAt(1).rel", equalTo("father"))
			.and().that().body("links.getAt(1).href", containsString(APP_URL + "/"))
			.and().that().body("links.getAt(1).href", containsString("/father"))
			.and().that().body("links.getAt(1).href.length()", greaterThan(APP_URL.length() + "/father".length()))
			.and().that().body("links.getAt(1).title", equalTo("Father"))
			.log().all()
		.when().put("/family/people/" + newId);	
	}
}
