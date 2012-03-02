package family.web;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.metadata.ConstraintDescriptor;

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
import family.util.PersonTransformer;
import flexjson.JSONSerializer;

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
     * TODO SHOULD THIS BE A POST AS A PUT WILL NOT BE IDEMPOTENT IF THERE IS AN EXISTING MOTHER?
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
    
		// TODO Add self to affected parties??
		// affected parties i.e. mother (and possibly the old mother)
		List<Person> affectedParties = new ArrayList<Person>();
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
     * Remove a mother. Don't delete her.
     * curl -i -X PUT -H "Accept: application/json" http://localhost:8080/family/people/1/mother
     * @param id
     * @param fatherId
     * @return
     * 
     */
    @RequestMapping(value = "/{id}/mother", method = RequestMethod.PUT, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> removeMother(
    		@PathVariable("id") java.lang.Long id, HttpServletRequest httpServletRequest) {
        
    	Person child = Person.findPerson(id);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (child == null ) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        Person oldMother = child.getMother();
        child.removeMother();
        child.merge();
        
        // TODO tidy this 
        StringBuffer requestUrl = httpServletRequest.getRequestURL();       
        requestUrl.delete(requestUrl.lastIndexOf("mother") - 1, requestUrl.length());
		String location = requestUrl.toString();
		String baseURL = requestUrl.delete(requestUrl.lastIndexOf("/"), requestUrl.length()).toString();
        
		headers.add("Location", location);
    
		// TODO Add self to affected parties??
		// affected parties = old mother
		List<Person> affectedParties = new ArrayList<Person>();
		if(oldMother != null){
			affectedParties.add(oldMother);
		}
		
        log.debug("location=" + location +
        		" removed mother (" + (oldMother == null?"not found":"id = "+oldMother.getId()) + ") " +
        				"for child (id=" + id + "). json='" 
        		+ child.toJson(baseURL, affectedParties) + "'");
		
        return new ResponseEntity<String>(child.toJson(baseURL, affectedParties), headers, HttpStatus.OK);
    }
    
    
    /**
     * curl -i -X PUT -H "Accept: application/json" http://localhost:8080/family/people/1/father/3
     * @param id
     * @param motherId
     * @return
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
        Person oldFather = child.getFather();
        child.addFather(father);
        father.merge();
        
        // TODO tidy this 
        StringBuffer requestUrl = httpServletRequest.getRequestURL();       
        requestUrl.delete(requestUrl.lastIndexOf("father") - 1, requestUrl.length());
		String location = requestUrl.toString();
		String baseURL = requestUrl.delete(requestUrl.lastIndexOf("/"), requestUrl.length()).toString();
        
		headers.add("Location", location);
    
		// TODO Add self to affected parties??
		// affected parties i.e. father (and possibly the old father)
		List<Person> affectedParties = new ArrayList<Person>();
		affectedParties.add(father);
		if(oldFather != null){
			affectedParties.add(oldFather);
		}
		
        log.debug("location=" + location +
        		" added father (id=" + fatherId + ") to child (id=" + id + "). json='" 
        		+ child.toJson(baseURL, affectedParties) + "'");
		
        return new ResponseEntity<String>(child.toJson(baseURL, affectedParties), headers, HttpStatus.OK);
    }
    
    
    /**
     * Remove a father. Don't delete him.
     * curl -i -X PUT -H "Accept: application/json" http://localhost:8080/family/people/1/father
     * @param id
     * @param fatherId
     * @return
     * 
     */
    @RequestMapping(value = "/{id}/father", method = RequestMethod.PUT, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> removeFather(
    		@PathVariable("id") java.lang.Long id, HttpServletRequest httpServletRequest) {
        
    	Person child = Person.findPerson(id);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (child == null ) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        Person oldFather = child.getFather();
        child.removeFather();
        child.merge();
        
        // TODO tidy this 
        StringBuffer requestUrl = httpServletRequest.getRequestURL();       
        requestUrl.delete(requestUrl.lastIndexOf("father") - 1, requestUrl.length());
		String location = requestUrl.toString();
		String baseURL = requestUrl.delete(requestUrl.lastIndexOf("/"), requestUrl.length()).toString();
        
		headers.add("Location", location);
    
		// TODO Add self to affected parties??
		// affected parties i.e. father (and possibly the old father)
		List<Person> affectedParties = new ArrayList<Person>();
		if(oldFather != null){
			affectedParties.add(oldFather);
		}
		
        log.debug("location=" + location +
        		" removed father (" + (oldFather == null?"not found":"id = "+oldFather.getId()) + ") " +
        				"for child (id=" + id + "). json='" 
        		+ child.toJson(baseURL, affectedParties) + "'");
		
        return new ResponseEntity<String>(child.toJson(baseURL, affectedParties), headers, HttpStatus.OK);
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
     * I'm not sure that we need this. We can just get the person
     * @param parentId
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/{parentId}/children", method = RequestMethod.GET,  headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> showJsonChildren(@PathVariable("parentId") Long parentId, HttpServletRequest httpServletRequest) {
        Person parent = Person.findPerson(parentId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (parent == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(Person.toJsonArray(parent.getChildren()),
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
     * Get a mother without her id
     * @param id
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/{id}/mother", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> showJsonMother(@PathVariable("id") java.lang.Long id,
    		HttpServletRequest httpServletRequest) {
        Person person = Person.findPerson(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (person == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        Person mother = person.getMother();
        if (mother == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        StringBuffer hostUrl = httpServletRequest.getRequestURL();
        hostUrl.delete(hostUrl.lastIndexOf("/mother"), hostUrl.length());
        hostUrl.delete(hostUrl.lastIndexOf("/"), hostUrl.length());
        String motherToJson = mother.toJson(hostUrl.toString());
        return new ResponseEntity<String>(motherToJson, headers, HttpStatus.OK);
    }
    
    
    /**
     * Get a father without his id
     * @param id
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/{id}/father", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> showJsonFather(@PathVariable("id") java.lang.Long id,
    		HttpServletRequest httpServletRequest) {
        Person person = Person.findPerson(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (person == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        Person father = person.getFather();
        if (father == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        StringBuffer hostUrl = httpServletRequest.getRequestURL();
        hostUrl.delete(hostUrl.lastIndexOf("/father"), hostUrl.length());
        hostUrl.delete(hostUrl.lastIndexOf("/"), hostUrl.length());
        String fatherToJson = father.toJson(hostUrl.toString());
        return new ResponseEntity<String>(fatherToJson, headers, HttpStatus.OK);
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
    		person = Person.fromJsonToPerson(json);// bad json
    	}catch(Exception e){
    		log.error("Bad JSON:" + json);
    		return new ResponseEntity<String>(
    				createExceptionJson(HttpStatus.BAD_REQUEST, e), headers, HttpStatus.BAD_REQUEST);
    	}
    	// Disallow id or version in a create request body.
    	if(json.indexOf("\"id\"") != -1 || json.indexOf("'id'") != -1 
    			|| json.indexOf("\"version\"") != -1 || json.indexOf("'version'") != -1){
    		log.error("JSON contains an 'id' or a 'version':" + json);
    		return new ResponseEntity<String>(createExceptionJson(HttpStatus.NOT_ACCEPTABLE, "JSON contains an 'id' or a 'version':" + json), headers, HttpStatus.NOT_ACCEPTABLE);
    	} 
    	try{
    		person.persist();
    	}catch(Exception e){
    		log.error("Bad JSON:" + json);
    		return new ResponseEntity<String>(
    				createExceptionJson(HttpStatus.BAD_REQUEST, e), headers, HttpStatus.BAD_REQUEST);
    	}

        String location = getLocationForChildResource(httpServletRequest, person.getId());
        headers.add("Location", location);
        String hostUrl = httpServletRequest.getRequestURL().toString();
        String personToJson = person.toJson(hostUrl);
        return new ResponseEntity<String>(personToJson, headers, HttpStatus.CREATED);
    }
    
    private String createExceptionJson(HttpStatus badRequest, String msg) {
		StringBuffer exceptionJson 
			= new StringBuffer("{\"exception\" :")
		.append("{\"code\" : ")
		.append(badRequest.value()).append(",")
		.append("\"name\" : \"").append(badRequest.name()).append("\"")
		.append("}}");
		return exceptionJson.toString();
	}

    private String createExceptionJson(HttpStatus badRequest, Exception e) {
		StringBuffer exceptionJson 
			= new StringBuffer("{\"exception\" :")
		.append("{\"code\" : ")
		.append(badRequest.value()).append(",")
		.append("\"name\" : \"").append(badRequest.name()).append("\",");
		if(e instanceof ConstraintViolationException){
			ConstraintViolationException constraintViolationException = (ConstraintViolationException)e;
			Set<ConstraintViolation<?>> constraintViolations = constraintViolationException.getConstraintViolations();
			for (ConstraintViolation<?> constraintViolation : constraintViolations) {
				ConstraintDescriptor<?> constraintDescriptor = constraintViolation.getConstraintDescriptor();
				Object invalidValue = constraintViolation.getInvalidValue();
				String errorMessage = constraintViolation.getMessage();
				log.debug(invalidValue + " "  + errorMessage);
				exceptionJson.append("\"message\" : \"").append(invalidValue + " "  + errorMessage);
			}
		}else{
			exceptionJson.append("\"message\" : \"").append(e.getMessage());
		}
		exceptionJson.append("\"")
		.append("}}");
		return exceptionJson.toString();
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
       if(json.indexOf("\"id\"") == -1 && json.indexOf("'id'") == -1){
    	   log.error("No id present in json:" + json);
    	   // TODO add descriptive status JSON to body 
    	   return new ResponseEntity<String>("No id present in json:" + json, headers, HttpStatus.BAD_REQUEST);
       }
       // must have a version too.
       if(json.indexOf("version") == -1){
    	   log.error("No version present in json:" + json);
    	   // TODO add descriptive status JSON to body 
    	   return new ResponseEntity<String>("No version present in json:" + json, headers, HttpStatus.BAD_REQUEST);
       }
        
       	Person personContainingUpdates = null;
       	Person updatedPerson = null;
       try{
    	   personContainingUpdates = Person.fromJsonToPerson(json);        
	   	}catch(Exception e){
			return new ResponseEntity<String>(e.getMessage()  + "\n" + json, headers, HttpStatus.BAD_REQUEST);
		}
       	try{
    	   updatedPerson = personContainingUpdates.merge();
	        if (updatedPerson == null) {
	            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
	        }
        }catch(Exception e){
    		log.error("Bad JSON:" + json);
    		return new ResponseEntity<String>(
    				createExceptionJson(HttpStatus.BAD_REQUEST, e), headers, HttpStatus.BAD_REQUEST);
        }
        StringBuffer url = httpServletRequest.getRequestURL();
        String location = url.toString();
        headers.add("Location", location);
        String hostUrl = httpServletRequest.getRequestURL().toString();
        String updatedPersonToJson = updatedPerson.toJson(hostUrl);
        return new ResponseEntity<String>(updatedPersonToJson, headers, HttpStatus.OK);
    }

    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> deleteFromJson(@PathVariable("id") java.lang.Long id, HttpServletRequest httpRequest) {
        Person person = Person.findPerson(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (person == null) {
        	//TODO return a json exception object in the body
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        
        // save the relations for returning in the response
        // TODO no need to save these here, just call the new 
        // List<Person> person.getAffectedParties() method mentioned below.
        Set<Person> oldChildren = person.getChildren();
        Set<Person> nonPersistentChildren = new HashSet<Person>();
        for (Person child : oldChildren) {
        	nonPersistentChildren.add(child);
		}
        Person oldMother = person.getMother();
        Person oldFather = person.getFather();
        
        person.removeMother();
        person.removeFather();
        // TODO refactor this into a new method on Person
        // Remove person as mother or father
        if(person.getChildren() != null){
        	for (Person child : person.getChildren()) {
				if(child.getMother() != null && child.getMother().equals(person)){
					child.removeMother();
				}
				if(child.getFather() != null && child.getFather().equals(person)){
					child.removeFather();
				}
			}
        }
        person.remove();
        
        // TODO Make this a method on person (List<Person> person.getAffectedParties())
        // and invoke before calling removeMother(), etc.
		List<Person> affectedParties = new ArrayList<Person>();
		if(oldMother != null){
			affectedParties.add(oldMother);
		}
		if(oldFather != null){
			affectedParties.add(oldFather);
		}
		for (Person child : nonPersistentChildren) {
			affectedParties.add(child);
		}
		
        // TODO tidy this 
        StringBuffer requestUrl = httpRequest.getRequestURL();       
		String baseURL = requestUrl.delete(requestUrl.lastIndexOf("/"), requestUrl.length()).toString();
        log.debug("baseURL=" + baseURL +
        		" json='" +
        		 person.toJson(baseURL, affectedParties) + "'");
        // TODO don't return the deleted person in the response body, just the affected parties.
        /*
         * This should be something like:
        JSONSerializer serializer = new JSONSerializer();
    	serializer.transform(new AffectedPartiesTransformer(appUrl), List<Person>.class);
		String json =  serializer.serialize( affectedParties );
         */
        return new ResponseEntity<String>(person.toJson(baseURL, affectedParties), headers, HttpStatus.OK);        
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
		UriTemplate template = new UriTemplate(url.append("/{childId}").toString());
		String location = template.expand(childId).toASCIIString();
		return location;
	}
	
	/**
	 * TODO return Array of name,id,sex,dob,dod,placeOfBirth,placeOfDeath,and URL.
	 * @param name
	 * @return
	 */
    @RequestMapping(params = "find=ByNameLike", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> jsonFindPeopleByNameLike(@RequestParam("name") java.lang.String name) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(Person.toJsonArray(Person.findPeopleByNameLike(name).getResultList()), headers, HttpStatus.OK);
    }
}
