package family.web;

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
     * @param id
     * @param motherId
     * @return
     */
    @RequestMapping(value = "/{id}/mother/{motherId}", method = RequestMethod.PUT, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> addMother(
    		@PathVariable("id") java.lang.Long id, @PathVariable("motherId") java.lang.Long motherId) {
        
    	Person child = Person.findPerson(id);
        Person mother = Person.findPerson(motherId);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/text");
        if (child == null || mother == null ) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        child.setMother(mother);
        mother.merge();
        log.debug("added mother (id=" + motherId + ") to child (id=" + id + "). json='" + child.toJson() + "'");
        return new ResponseEntity<String>(child.toJson(), headers, HttpStatus.OK);
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
    		@PathVariable("id") java.lang.Long id, @PathVariable("fatherId") java.lang.Long fatherId) {
        
    	Person child = Person.findPerson(id);
        Person father = Person.findPerson(fatherId);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/text");
        if (child == null || father == null ) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        child.setFather(father);
        father.merge();
        log.debug("added father (id=" + fatherId + ") to child (id=" + id + "). json='" + child.toJson() + "'");
        return new ResponseEntity<String>(child.toJson(), headers, HttpStatus.OK);
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
    public ResponseEntity<java.lang.String> findChildren(@RequestParam("parentId") Long parentId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/text; charset=utf-8");
        return new ResponseEntity<String>(Person.toJsonArray(Person.findChildren(parentId).getResultList()), headers, HttpStatus.OK);
    }    
    
    @RequestMapping(value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> showJson(@PathVariable("id") java.lang.Long id) {
        Person person = Person.findPerson(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (person == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(person.toJson(), headers, HttpStatus.OK);
    }
    
    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> createFromJson(@RequestBody java.lang.String json, HttpServletRequest httpServletRequest) {
        Person person = Person.fromJsonToPerson(json);
        person.persist();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        String location = getLocationForChildResource(httpServletRequest, person.getId());
        headers.add("Location", location);
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

	private String getLocationForChildResource(
			HttpServletRequest httpServletRequest, Object childId) {
		StringBuffer url = httpServletRequest.getRequestURL();
		UriTemplate template = new UriTemplate(url.append("/{childId}").toString());
		String location = template.expand(childId).toASCIIString();
		return location;
	}
}
