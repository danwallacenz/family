package family.util;

import java.util.Set;

import family.domain.Person;
import flexjson.TypeContext;
import flexjson.transformer.AbstractTransformer;

public class PersonTransformer extends AbstractTransformer {

	public PersonTransformer() {
		super();
	}

	public PersonTransformer(final String url) {
		super();
		this.url = url;
	}
	
	public PersonTransformer(final String url, final Set<Person>affectedParties) {
		super();
		this.url = url;
		this.affectedParties = affectedParties;
	}
	
	private String url = "http://localhost:8080/family/people";
	
	private Set<Person> affectedParties;
	
    @Override
    public Boolean isInline() {
        return Boolean.TRUE;
    }

	@Override
	public void transform(Object object) {
		
		// Start the object
		TypeContext typeContext = getContext().writeOpenObject();
		typeContext.setFirst(false);

	    Person person = (Person) object;
		
		// Write out the fields
	    getContext().writeName("id");
	    getContext().transform(person.getId());
	    getContext().writeComma();
	    getContext().writeName("name");
	    getContext().transform(person.getName());
	    getContext().writeComma();
	    getContext().writeName("version");
	    getContext().transform(person.getVersion());
	    getContext().writeComma();
	    getContext().writeName("sex");
	    getContext().transform(person.getSex());
	    getContext().writeComma(); 
	    
	    writeFather(person);
	    
	    getContext().writeComma();
	    
	    writeMother(person);
	    
	    getContext().writeComma();
			    
	    writeChildren(person);
	    
	    getContext().writeComma();
        
	    writeLinks(person);

	    if(affectedParties != null){
	    	 getContext().writeComma();
	    	writeAffectedParties(person);
	    }
	    
		// Close the Person Object
	    getContext().writeCloseObject();
		
	}

	private void writeAffectedParties(Person  person) {
		// Write out the affectedParties objects		
		getContext().writeName("affectedParties");
	    // Open the Array of affectedParties
	    TypeContext itemTypeContext = getContext().writeOpenArray();
	    
	    for(Person affectedParty : affectedParties){
			// Add a comma after each affectedParty object is written
            if (!itemTypeContext.isFirst()) getContext().writeComma();
            itemTypeContext.setFirst(false);
			// Open the affectedParty object and write the fields
			getContext().writeOpenObject();
    	    getContext().writeName("id");
    	    getContext().transform(affectedParty.getId());
    	    getContext().writeComma();
    	    getContext().writeName("name");
    	    getContext().transform(affectedParty.getName());
    	    getContext().writeComma();
    	    getContext().writeName("href");
    	    getContext().transform(url + "/" + affectedParty.getId());
			// Close the affectedParty object
    	    getContext().writeCloseObject();
        }
		// Close the Array of affectedParties
        getContext().writeCloseArray();
	}

	private void writeMother(Person person) {
	    getContext().writeName("mother");
	    Person mother = person.getMother();
	    if(mother == null){
	    	getContext().transform(null);
	    }else{
	    	getContext().writeOpenObject();
	    	getContext().writeName("id");
	    	getContext().transform(mother.getId());
	    	getContext().writeComma();
	    	getContext().writeName("name");
	    	getContext().transform(mother.getName());
	    	getContext().writeComma();
	    	getContext().writeName("sex");
	    	getContext().transform(mother.getSex());
	    	getContext().writeComma();
	    	getContext().writeName("version");
	    	getContext().transform(mother.getVersion());
	    	getContext().writeComma();
	    	writeLinks(mother);
	    	getContext().writeCloseObject();
	    }
	}

	private void writeFather(Person person) {
	    getContext().writeName("father");
	    Person father = person.getFather();
	    if(father == null){
	    	getContext().transform(null);
	    }else{
	    	getContext().writeOpenObject();
	    	getContext().writeName("id");
	    	getContext().transform(father.getId());
	    	getContext().writeComma();
	    	getContext().writeName("name");
	    	getContext().transform(father.getName());
	    	getContext().writeComma();
	    	getContext().writeName("sex");
	    	getContext().transform(father.getSex());
	    	getContext().writeComma();
	    	getContext().writeName("version");
	    	getContext().transform(father.getVersion());
	    	getContext().writeComma();
	    	writeLinks(father);
	    	getContext().writeCloseObject();
	    }
	}

	private void writeChildren(Person person) {
	    // Write out the children
	    getContext().writeName("children");	    
		// Open the Array of Children
	    TypeContext itemTypeContext = getContext().writeOpenArray();
		
	    Set<Person> children = person.getChildren();
	    for(Person child : children){
			// Add a comma after each child object is written
            if (!itemTypeContext.isFirst()) getContext().writeComma();
            itemTypeContext.setFirst(false);
			// Open the child object and write the fields
			getContext().writeOpenObject();
    	    getContext().writeName("id");
    	    getContext().transform(child.getId());
    	    getContext().writeComma();
    	    getContext().writeName("version");
    	    getContext().transform(child.getVersion());
    	    getContext().writeComma();
    	    getContext().writeName("name");
    	    getContext().transform(child.getName());
    	    getContext().writeComma();
    	    getContext().writeName("sex");
    	    getContext().transform(child.getSex());
    	    getContext().writeComma();
    	    getContext().writeName("father");
    	    getContext().transform(child.getFather()==null?"null":child.getFather().getId());
    	    getContext().writeComma();
    	    getContext().writeName("mother");
    	    getContext().transform(child.getMother()==null?"null":child.getMother().getId());
    	    
    	    getContext().writeComma();
    	    writeLinks(child);
    	    
			// Close the child object
    	    getContext().writeCloseObject();
        }
		// Close the Array of children
        getContext().writeCloseArray();
	
}

	private void writeLinks(Person person) {
		// Write out the link objects		
		getContext().writeName("links");
		getContext().writeOpenArray();
		// self
		getContext().writeOpenObject();
		getContext().writeName("rel");
		getContext().transform("self");
		getContext().writeComma();
		getContext().writeName("href");
		getContext().transform(url + "/" + person.getId());
		getContext().writeComma();
		getContext().writeName("title");
		getContext().transform(person.getName());
		getContext().writeCloseObject();
		getContext().writeComma();

		// father
		getContext().writeOpenObject();
		getContext().writeName("rel");
		getContext().transform("father");
		getContext().writeComma();
		getContext().writeName("href");
		if (person.getFather() == null) {
			getContext().transform(url + "/" + person.getId() + "/father");
		} else {
			getContext().transform(url + "/" + person.getFather().getId());
		}
		getContext().writeComma();
		getContext().writeName("title");
		getContext().transform(person.getFather()==null?"Father":person.getFather().getName());
		getContext().writeCloseObject();
		getContext().writeComma();

		// mother
		getContext().writeOpenObject();
		getContext().writeName("rel");
		getContext().transform("mother");
		getContext().writeComma();
		getContext().writeName("href");
		if (person.getMother() == null) {
			getContext().transform(url + "/" + person.getId() + "/mother");
		} else {
			getContext().transform(url + "/" + person.getMother().getId());
		}
		getContext().writeComma();
		getContext().writeName("title");
		getContext().transform(person.getMother()==null?"Mother":person.getMother().getName());
		getContext().writeCloseObject();
		getContext().writeComma();

		// children
		getContext().writeOpenObject();
		getContext().writeName("rel");
		getContext().transform("children");
		getContext().writeComma();
		getContext().writeName("href");
		getContext().transform(url + "/" + person.getId() + "/children");
		getContext().writeComma();
		getContext().writeName("title");
		getContext().transform("Children");
		getContext().writeCloseObject();

		// Close the Array of link objects
		getContext().writeCloseArray();

	}
}

