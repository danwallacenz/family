package family.util;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Set;

import family.domain.Person;
import flexjson.TypeContext;
import flexjson.transformer.AbstractTransformer;

public class AffectedPartiesTransformer extends AbstractTransformer {

	public AffectedPartiesTransformer() {
		super();
	}

	public AffectedPartiesTransformer(final String url) {
		super();
		this.url = url;
	}
	
	public AffectedPartiesTransformer(final String url, final List<Person>affectedParties) {
		super();
		this.url = url;
		this.affectedParties = affectedParties;
	}
	
	private String url = "http://localhost:8080/family/people";
	
	private List<Person> affectedParties;
	
    @Override
    public Boolean isInline() {
        return Boolean.TRUE;
    }

	@SuppressWarnings("unchecked")
	@Override
	public void transform(Object affectedParties) {
		
		// Start the object
		TypeContext typeContext = getContext().writeOpenObject();
		typeContext.setFirst(false);
		
		this.affectedParties = (List<Person>) affectedParties;
				
	    if(affectedParties != null){
	    	 //getContext().writeComma();
	    	writeAffectedParties();
	    }
	  
		// Close the affected parties Object
	    getContext().writeCloseObject();
		
	}

	private void writeAffectedParties() {
		// Write out the affectedParties objects		
		getContext().writeName("affectedParties");
	    // Open the Array of affectedParties
	    TypeContext itemTypeContext = getContext().writeOpenArray();
	    
	    for(Person affectedParty : affectedParties){
			// Add a comma after each affectedParty object is written
            if (!itemTypeContext.isFirst()){
            	getContext().writeComma();
            }
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
}

