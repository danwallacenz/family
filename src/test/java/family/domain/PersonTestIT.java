package family.domain;

import static com.jayway.restassured.RestAssured.expect;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static groovyx.net.http.ContentType.JSON;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.jayway.restassured.specification.ResponseSpecification;

 
public class PersonTestIT{ 

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	/**
	 * curl -i -H "Accept: application/json" http://localhost:8080/family/people/5
	 * @throws Exception
	 */
    //@Test
    public void simpleJSONAndHamcrestMatcher() throws Exception {
    	//Response response = get("/family/people/1");
    	Response response = given().contentType(JSON).when().get("/family/people/1");
    	String responseString = response.asString();
        //expect().contentType(ContentType.TEXT).body("name", equalTo("dan")).given().contentType(ContentType.JSON).when().get("/family/people/1");
        expect().log().all().body("name", equalTo("dan")).given().contentType(JSON).when().get("/family/people/1");
    }
    
    //@Test
    public void contentTypeSpecification() throws Exception {
        final RequestSpecification requestSpecification = given().contentType(JSON);
        final ResponseSpecification responseSpecification = expect().contentType(JSON);
        given(requestSpecification, responseSpecification).get("/family/people/1");
    }
	
    //@Test
    public void get() throws Exception {
//        final RequestSpecification requestSpecification = given().contentType(ContentType.JSON);
//        final ResponseSpecification responseSpecification = expect().contentType(ContentType.JSON).body("name", equalTo("dan"));
//        Response response = given(requestSpecification, responseSpecification).get("/family/people/1");
//        String responseString = response.asString();
//        System.out.println(responseString);     
    }
    
    /**
     * Accept=application/json
     * @throws Exception
     */
    @Test
    public void createFromJSON() throws Exception {
    	RestAssured.requestContentType(JSON);
    	try {
    		given().header("Accept", "application/json").body("{ \"name\" : \"johnny\"}").then().expect().statusCode(201).when().post("/family/people");
    	} finally {
    		RestAssured.reset();
    	}
    }
}
