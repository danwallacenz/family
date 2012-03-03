package family.util;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Set;

import family.domain.Person;
import flexjson.TypeContext;
import flexjson.transformer.AbstractTransformer;

public class SearchResultsTransformer extends AbstractTransformer {

	public SearchResultsTransformer() {
		super();
	}

	public SearchResultsTransformer(final String url) {
		super();
		this.url = url;
	}
	
	public SearchResultsTransformer(final String url, final List<Person>results) {
		super();
		this.url = url;
		this.results = results;
	}
	
	private String url = "http://localhost:8080/family/people";
	
	private List<Person> results;
	
    @Override
    public Boolean isInline() {
        return Boolean.TRUE;
    }

	@SuppressWarnings("unchecked")
	@Override
	public void transform(final Object results) {
		
		// Start the object
		TypeContext typeContext = getContext().writeOpenObject();
		typeContext.setFirst(false);
		
		this.results = (List<Person>) results;
				
	    if(results != null){
	    	 //getContext().writeComma();
	    	writeResults();
	    }
	  
		// Close the affected parties Object
	    getContext().writeCloseObject();
		
	}

	private void writeResults() {
		// Write out the affectedParties objects		
		getContext().writeName("searchResults");
	    // Open the Array of affectedParties
	    TypeContext itemTypeContext = getContext().writeOpenArray();
	    
	    for(Person person : results){
			// Add a comma after each affectedParty object is written
            if (!itemTypeContext.isFirst()){
            	getContext().writeComma();
            }
            itemTypeContext.setFirst(false);
			
            // Open the affectedParty object and write the fields
			getContext().writeOpenObject();
			
			// Write out the fields
		    getContext().writeName("id");
		    getContext().transform(person.getId());
		    getContext().writeComma();
		    
		    getContext().writeName("name");
		    getContext().transform(person.getName());
		    getContext().writeComma();

		    getContext().writeName("sex");
		    getContext().transform(person.getSex());
		    getContext().writeComma();
		    
		    getContext().writeName("dob");
		    getContext().transform(person.getDob() == null?"unknown":new SimpleDateFormat("dd/MM/yyyy").format(person.getDob()));
		    getContext().writeComma();
		  
		    getContext().writeName("dod");
		    getContext().transform(person.getDod() == null?"unknown":new SimpleDateFormat("dd/MM/yyyy").format(person.getDod()));
		    getContext().writeComma();
		    
		    getContext().writeName("placeOfBirth");
		    getContext().transform(person.getPlaceOfBirth());
		    getContext().writeComma();
		    
		    getContext().writeName("placeOfDeath");
		    getContext().transform(person.getPlaceOfDeath());
		    getContext().writeComma();
    	    getContext().writeName("href");
    	    getContext().transform(url + "/" + person.getId());
			
    	    // Close the affectedParty object
    	    getContext().writeCloseObject();
        }
		// Close the Array of affectedParties
        getContext().writeCloseArray();
	}
}

