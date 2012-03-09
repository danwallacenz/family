package family.domain;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;

import family.web.PersonController;

public abstract class FuncAbstract {

    private static Logger log = LoggerFactory.getLogger(FuncAbstract.class);
    
    /**
     * Switch this on to target the local micro CloudFoundry then
     *  clean and build this project.
     */
	protected boolean useMicroCloudFoundry = false;
	
	protected boolean useCloudFoundryDotCom = true;
	
	protected String MICRO_CLOUD_FOUNDRY_URL = "http://family.danwallacenz.cloudfoundry.me";
	protected String CLOUD_FOUNDRY_DOT_COM_URL = "http://familypeople.cloudfoundry.com";
	protected  String DEFAULT_HOST_URL = "http://localhost:8080/family"; 
	
	protected String APP_URL = null;
	
    //================================================================================================
    // utility methods
    /**
     * Give a JSON <code>String</code> representing a <code>Person</code> ("{ \"name\" : \"johnny\"}"), 
     * will save it to the database, validate the HTTP response code and Location header, and return 
     * the id of the newly created <code>Person</code>.
     * @param json
     * @return new id
     */
	protected String idOfPosted(String json) {
    	Response response = 
    			given().log().all().header("Accept", "application/json")
    			.body(json).then().expect().log().all()
    			.statusCode(201).and().response()
				.header("Location", containsString(APP_URL))
    			.when() 
    			.post(appUrl() + "/");
    	String location = response.headers().getValue("Location");
    	String newId = location.substring(location.lastIndexOf("/") + 1);
    	return newId;
	}
    
    
	protected Response responseOfPosted(String json) {
    	Response response = 
    			given().header("Accept", "application/json")
    			.body(json).then().expect()
    			.statusCode(201).and().response()
				.header("Location", containsString(APP_URL))
    			.when()
    			.post(appUrl());
    	return response;
	}
	
	protected Response responseOfPostedNoValidation(String json) {
    	Response response = 
    			given().header("Accept", "application/json")
    			.body(json)
    			.when()
    			.post(appUrl());
    	return response;
	}
	
	protected String appUrl(){
		return APP_URL;
	}
	
	@Before
	public void init(){
		 	
		if(!useMicroCloudFoundry && !useCloudFoundryDotCom){
			APP_URL = DEFAULT_HOST_URL;
		}
		if(useMicroCloudFoundry && useCloudFoundryDotCom){
			String msg = "Both System properties USE_MICRO_CLOUD_FOUNDRY and USE_CLOUD_FOUNDRY_DOT_COM are set to true. Please sort this out.";
			throw new RuntimeException(msg);
		}
		if(useMicroCloudFoundry){
			APP_URL = MICRO_CLOUD_FOUNDRY_URL;
		}
		if(useCloudFoundryDotCom){
			APP_URL = CLOUD_FOUNDRY_DOT_COM_URL;
		}
		APP_URL += "/people";
		log.info("Using host: " + APP_URL );
		log.debug("USE_MICRO_CLOUD_FOUNDRY = " + useMicroCloudFoundry);
		log.debug("MICRO_CLOUD_FOUNDRY_URL = " + MICRO_CLOUD_FOUNDRY_URL);
		log.debug("USE_CLOUD_FOUNDRY_DOT_COM = " + useCloudFoundryDotCom);
		log.debug("CLOUD_FOUNDRY_DOT_COM_URL = " + CLOUD_FOUNDRY_DOT_COM_URL);
	}
	
	@After
	public void afterClass(){
		
		APP_URL = null;
	
	}
}
