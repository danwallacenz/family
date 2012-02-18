package family.web;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriTemplate;

import family.domain.Person;

@RooWebJson(jsonObject = Person.class)
@Controller
@RequestMapping("/people")
@RooWebScaffold(path = "people", formBackingObject = Person.class)
@RooWebFinder
public class PersonController {

    private static Logger log = LoggerFactory.getLogger(PersonController.class);
    
    
    /**
     * curl -i -X PUT -H "Accept: application/json" http://localhost:8080/family/people/1/mother/2
     * @param id child id
     * @param motherId
     * @return child as JSON
     * 
     * TODO do we need the Accept Header?
     */
    @RequestMapping(value = "/{id}/mother/{motherId}", method = RequestMethod.PUT, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> addMother(
    		@PathVariable("id") java.lang.Long id, @PathVariable("motherId") java.lang.Long motherId, HttpServletRequest httpServletRequest) {
        
    	Person child = Person.findPerson(id);
        Person mother = Person.findPerson(motherId);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (child == null || mother == null ) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        
        Person oldMother = child.getMother();
        child.addMother(mother);
        mother.merge();
        
        // TODO tidy this 
        StringBuffer requestUrl = httpServletRequest.getRequestURL();       
        requestUrl.delete(requestUrl.lastIndexOf("mother") - 1, requestUrl.length());
		String location = requestUrl.toString();
		String baseURL = requestUrl.delete(requestUrl.lastIndexOf("/"), requestUrl.length()).toString();
        
		headers.add("Location", location);
    
		// affected parties i.e. mother (and possibly the old mother)
		Set<Person> affectedParties = new HashSet<Person>();
		affectedParties.add(mother);
		if(oldMother != null){
			affectedParties.add(oldMother);
		}
		
        log.debug("location=" + location +
        		" added mother (id=" + motherId + ") to child (id=" + id + "). json='" 
        		+ child.toJson(baseURL, affectedParties) + "'");
		
        return new ResponseEntity<String>(child.toJson(baseURL, affectedParties), headers, HttpStatus.OK);
    }
    
    /**
     * curl -i -X PUT -H "Accept: application/json" http://localhost:8080/family/people/1/father/3
     * @param id
     * @param motherId
     * @return
     * 
     * TODO remove child from previous father's children if present. 
     * 
     */
    @RequestMapping(value = "/{id}/father/{fatherId}", method = RequestMethod.PUT, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> addFather(
    		@PathVariable("id") java.lang.Long id, @PathVariable("fatherId") java.lang.Long fatherId, HttpServletRequest httpServletRequest) {
        
    	Person child = Person.findPerson(id);
        Person father = Person.findPerson(fatherId);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (child == null || father == null ) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        child.setFather(father);
        father.merge();
        String hostUrl = httpServletRequest.getRequestURL().toString();
        
        log.debug("added father (id=" + fatherId + ") to child (id=" + id + "). json='" + child.toJson(hostUrl) + "'");
        return new ResponseEntity<String>(child.toJson(hostUrl), headers, HttpStatus.OK);
    }
    
    /**
     * 
     * Example comment
     * 
     * "This method accepts a JSON document sent via HTTP POST 
     * converts it into an Owner instance and persists that 
     * new instance before returning a HTTP 201 (CREATED) status code. 
     * The accompanying curl command is as follows:"
     * 
     * @param parentId
     * @return
     */
    
    @RequestMapping(params = "find=children", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> findChildren(@RequestParam("parentId") Long parentId, HttpServletRequest httpServletRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/text; charset=utf-8");
        return new ResponseEntity<String>(Person.toJsonArray(Person.findChildren(parentId).getResultList()),
        		headers, HttpStatus.OK);
    }    
    
    /**
     * 
     * @param id
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> showJson(@PathVariable("id") java.lang.Long id,
    		HttpServletRequest httpServletRequest) {
        Person person = Person.findPerson(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (person == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        
        StringBuffer hostUrl = httpServletRequest.getRequestURL();
        hostUrl.delete(hostUrl.lastIndexOf("/"), hostUrl.length());
        String personToJson = person.toJson(hostUrl.toString());
        return new ResponseEntity<String>(personToJson, headers, HttpStatus.OK);
    }
    
    /**
     * 
     * @param json
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> createFromJson(@RequestBody java.lang.String json,
    		HttpServletRequest httpServletRequest) {
        
    	HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        
        Person person = null;
    	try{
    		person = Person.fromJsonToPerson(json);
    	}catch(Exception e){
    		return new ResponseEntity<String>(json, headers, HttpStatus.BAD_REQUEST);
    	}
    	// Disallow id or version in a create request body.
    	if(json.indexOf("\"id\"") != -1 || json.indexOf("'id'") != -1 
    			|| json.indexOf("\"version\"") != -1 || json.indexOf("'version'") != -1){
    		return new ResponseEntity<String>(json, headers, HttpStatus.NOT_ACCEPTABLE);
    	}
        person.persist();

        String location = getLocationForChildResource(httpServletRequest, person.getId());
        headers.add("Location", location);
        String hostUrl = httpServletRequest.getRequestURL().toString();
        String personToJson = person.toJson(hostUrl);
        return new ResponseEntity<String>(personToJson, headers, HttpStatus.CREATED);
    }
    
    /**
     * 
     * @param id
     * @param json
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/{id}",method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> updateFromJson(@PathVariable("id") java.lang.Long id, 
    		@RequestBody java.lang.String json,
    		HttpServletRequest httpServletRequest) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        
        // if no valid existing id, is present in the JSON, then
    	// then the Person will be created rather than updated.
       if(!(json.indexOf("\"id\"") == -1 || json.indexOf("'id'") == -1)){
    	   log.error("No id present in json:" + json);
    	   // TODO add descriptive status JSON to body 
    	   return new ResponseEntity<String>(headers, HttpStatus.BAD_REQUEST);
       }
        
       try{
    	   	Person personContainingUpdates = Person.fromJsonToPerson(json);        

	        if (personContainingUpdates.merge() == null) {
	            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
	        }
        }catch(Exception e){
        	String errorMsg = "\nERROR updating Person with id" + id + "\n" + json +"\n" + e.getMessage();
        	log.error(errorMsg);
        	return new ResponseEntity<String>(errorMsg, headers, HttpStatus.NOT_MODIFIED);
        }
        StringBuffer url = httpServletRequest.getRequestURL();
        //url.delete(url.lastIndexOf("/"), url.length());
        String location = url.toString();
        headers.add("Location", location);
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    /**
     * 
     * @param httpServletRequest
     * @param childId
     * @return
     */
	private String getLocationForChildResource(
			HttpServletRequest httpServletRequest, Object childId) {
		StringBuffer url = httpServletRequest.getRequestURL();
		//url.delete(url.lastIndexOf("/") + 1, url.length());
		UriTemplate template = new UriTemplate(url.append("/{childId}").toString());
		String location = template.expand(childId).toASCIIString();
		return location;
	}
}
