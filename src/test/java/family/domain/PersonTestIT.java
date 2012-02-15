package family.domain;

import static com.jayway.restassured.RestAssured.expect;
import static com.jayway.restassured.RestAssured.given;
import static groovyx.net.http.ContentType.JSON;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.*;
import static junit.framework.Assert.*;


import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
 
public class PersonTestIT{ 
   
	private static final String HOST_AND_PORT = "http://localhost:8080";
	private static final String APP_PATH = "/family/people/";
	private static final String APP_URL = HOST_AND_PORT + APP_PATH;
	
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
          		"\"father\":null," +
          		"\"mother\":null," +
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
      
    //================================================================================================
    // utility methods
    /**
     * Give a JSON <code>String</code> representing a <code>Person</code> ("{ \"name\" : \"johnny\"}"), 
     * will save it to the database, validate the HTTP response code and Location header, and return 
     * the id of the newly created <code>Person</code>.
     * @param json
     * @return new id
     */
    private String idOfPosted(String json) {
    	Response response = 
    			given().header("Accept", "application/json")
    			.body(json).then().expect()
    			.statusCode(201).and().response()
				.header("Location", containsString(APP_URL))
    			.when()
    			.post("/family/people");
    	String location = response.headers().getValue("Location");
    	String newId = location.substring(location.lastIndexOf("/") + 1);
    	return newId;
	}
    
    
    private Response responseOfPosted(String json) {
    	Response response = 
    			given().header("Accept", "application/json")
    			.body(json).then().expect()
    			.statusCode(201).and().response()
				.header("Location", containsString(APP_URL))
    			.when()
    			.post("/family/people");
    	return response;
	}
    
	/**
	 * Tests updating a single<code>Person</code>.
	 * @throws Exception
	 */	
    @Test
    public void ensureThatVersionIsIncrementedAfterAPersonIsUpdated() throws Exception {
    	
    	int version = 0;
    	
    	// create a test Person "dan1"
		Person person = new Person();
		person.setName("dan1");
		person.setSex(Sex.MALE);
		
		// serialize person to JSON
		String personJson = person.toJson(APP_URL); 
		
		// POST person and retrieve the new id
    	String newId = idOfPosted(personJson);
    	
    	// update person with id, a new name, and version = 0
    	String newName = "dan2";
		person.setId(new Long(newId));
		person.setName(newName);
		person.setSex(Sex.MALE);
		person.setVersion(version);
		
		// serialize updated person to JSON
		String personUpdatedToJson = person.toJson(APP_URL);

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
  
    /**
     * 
     */
    @Test
    public void ensureThatSettingAPersonsMotherIsCorrect(){
    	
    	String rachelJSON = "{ \"name\" : \"Rachel Margaret Wallace\",\"sex\":\"FEMALE\"}";
    	// Save Rachel
    	String rachelId = idOfPosted(rachelJSON);
    	
    	String isaacJSON = "{ \"name\": \"Issac Williams\",\"sex\": \"MALE\"}";
    	// Save Isaac
    	String isaacId = idOfPosted(isaacJSON); 
    	
		String expectedIsaacJSON 
			= "{" +
					"\"id\":" + isaacId + "," +
					"\"name\":\"Issac Williams\"" +
					",\"version\":1," +
					"\"sex\":\"MALE\"," +
					"\"father\":null," +
					"\"mother\":" +
						"{" +
							"\"id\":" + rachelId + "," +
							"\"name\":\"Rachel Margaret Wallace\"," +
							"\"sex\":\"FEMALE\"," +
							"\"version\":1," +
							"\"links\":" +
							"[" +
								"{\"rel\":\"self\",\"href\":\"http://localhost:8080/family/people/" + rachelId + "\",\"title\":\"Rachel Margaret Wallace\"}," +
								"{\"rel\":\"father\",\"href\":\"http://localhost:8080/family/people/" + rachelId + "/father\",\"title\":\"Father\"}," +
								"{\"rel\":\"mother\",\"href\":\"http://localhost:8080/family/people/" + rachelId + "/mother\",\"title\":\"Mother\"}," +
								"{\"rel\":\"children\",\"href\":\"http://localhost:8080/family/people/" + rachelId + "/children\",\"title\":\"Children\"}" +
							"]" +
						"}," +
					"\"children\":[]," +
					"\"links\":" +
						"[" +
							"{\"rel\":\"self\",\"href\":\"http://localhost:8080/family/people/" + isaacId + "\",\"title\":\"Issac Williams\"}," +
							"{\"rel\":\"father\",\"href\":\"http://localhost:8080/family/people/" + isaacId + "/father\",\"title\":\"Father\"}," +
							"{\"rel\":\"mother\",\"href\":\"http://localhost:8080/family/people/" + rachelId + "\",\"title\":\"Rachel Margaret Wallace\"}," +
							"{\"rel\":\"children\",\"href\":\"http://localhost:8080/family/people/" + isaacId + "/children\",\"title\":\"Children\"}" +
							"]" +
					",\"affectedParties\":" +
						"[" +
							"{" +		
								"\"id\":"+ rachelId +"," +
								"\"name\":\"Rachel Margaret Wallace\"," +
								"\"href\":\"http://localhost:8080/family/people/" + rachelId + "\"" +
							"}" +
						"]" + 		
			"}";

		// Set Rachel as Isaac's mother.
		RestAssured.requestContentType(JSON);
		try {
			Response addMotherResponse = 
    			given().header("Accept", "application/json")
    			.then().
    			expect()
    			.statusCode(200).and().response()
				.header("Location", containsString(APP_URL + isaacId))
    			.when()
    			.put("/family/people/" + isaacId + "/mother/" + rachelId);
			
			// Ensure that Isaac has been updated properly with Rachel as his mother
			String addMotherResponseBody = addMotherResponse.body().asString();
			System.out.println(addMotherResponseBody);
			
			assertEquals(expectedIsaacJSON, addMotherResponseBody);
			
		} finally {
			RestAssured.reset();
		}
    }
}