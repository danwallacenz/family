/*
 * Borrowed heavily from Andrew Burgess at Nettuts+ (andrew8080 at gmail.com),
 * Nicolas Zakas and Addy Osmani.
 * 
 */

/*******************************************************************************
 * JUST TEXT-ENTRY & A BUTTON.
 */
CORE.create_module("search-box", function(sb) {
    
	var input, button;

    return {
    	
        init : function () {
            input = sb.find("#search_input")[0],
            button = sb.find("#search_button")[0],
            
            sb.addEvent(button, "click", this.handleSearch);
        },
        
        destroy : function () {
            sb.removeEvent(button, "click", this.handleSearch);
            input = button = null;
        },
        
        handleSearch : function () {
            var query = input.value;
            if (query) {
                sb.notify({
                    type : 'perform-search',
                    data : query
                });
            }
        },
    };
});



/*******************************************************************************
 * Listen for a 'perform-search' event. When that happens, send an ajax call to
 * the server. Fire a 'results-returned' event when the search results arrive 
 * successfully..
 */
CORE.create_module("searcher", function(sb) {    

    return {
        init : function () {
        	sb.listen({
        		'perform-search' : function (searchTerm) {
        		
        			var ajaxRequest, doneCallback, failCallback;
        		
        			// ON SUCCESS
        			doneCallback = function(data, textStatus, jqXHR) {		
						if (data) { 
	   						sb.notify({
	   		                    type : 'results-returned',
	   		                    data : data  
	   		                })
						}
					};
					
					// ON FAILURE (how can I test this?)
					failCallback = function(jqXHR, textStatus, errorThrown) {
						log("ajax error:" + textStatus 
							+ " status:" + jqXHR.status 
							+ " errorThrown: " + errorThrown);
   						sb.notify({
   		                    		type : 	'error-ajax',
   		                    		data : 	{"jqXHR" 		: jqXHR,
   		                    				"textStatus"	: textStatus,
   		                    				"errorThrown"	: errorThrown 
   		                    			}
   		                		})
					};
					
					// SEND
					ajaxRequest = sb.getJSON(sb.baseUrl(), // Should sb call baseUrl()? Don't think so!
											doneCallback,
        									failCallback,
        									{ find: "ByNameLike",
						   					name: searchTerm });
					
        			// Experimental - shows how to attach a callback later
					// on a Deferred object.
        			ajaxRequest.done(function(data, textStatus, jqXHR){
        				log("Experimental Deferred-start");
        				log(data);
        				log("Experimental Deferred-end");
        			});
       			}
        	})
        },
        
        destroy : function () {
            sb.ignore('perform-search');
        },
        
    	/*
    	 * Returns the correct url for ajax calls whether this is hosted on:
    	 * 1. My local TC Server.
    	 * 2. My local CloudFoundry instance.
    	 * 3. My CloudFoundry.com instance.
    	 */
//    	url : function(){
//    		return location.href.slice(0, location.href.indexOf("js")) + "people";
//    	}
    };
});

/*******************************************************************************
 * DISPLAY SEARCH RESULTS
 * Listen for a 'results-returned' event. When this happens, display the results.
 * Add a 'person-selected' event which fires whenever a single result is clicked.
 */
CORE.create_module("search-results", function(sb) {

    return {
        init : function () {
            var resultsList 	= sb.find("ul")[0],
            	resultsTemplate	= sb.find('#templ-results')[0],
            	searchResults,
            	resultsListItems,
            	that = this;
            	
            sb.listen({
                'results-returned' : function (results) {
                	
                	// extract from ugly namespacing in json. TODO fix this
                	//searchResults = sb.parseJSON(results).searchResults;
                	searchResults = results.searchResults;
                	
            		log(searchResults);
            		
            		sb.removeEvent(resultsList, 'click', that.handleSelect);
            		
            		resultsListItems = sb.loadTemplate(resultsTemplate,
            								{"foundPeople" : searchResults});
            		resultsList.innerHTML = resultsListItems;
            		
            		// Publish 'person-selected' when a link is clicked.
            		sb.addEvent(resultsList, 'click', that.handleSelect);
                }
            });

        },
        
        destroy : function () {
            sb.removeEvent(resultsList, "click", this.handleSelect );
            sb.ignore(['results-returned']);
            compiledTmpl = null;
        },
        
        handleSelect : function (e) {
    		var target = e.target;
    		var tagName = target.tagName;
    		if(tagName === 'A'){
    			e.preventDefault();
    			// Publish
                sb.notify({
                    type : 'person-selected',
                    data : target
                });
    		}
        },
    };
});

/*******************************************************************************
 * Listen for a 'person-selected' event. When that happens, send an ajax call to
 * the server to retrieve the selected person's json. 
 * Fire a 'person-loaded' event when the json arrives successfully.
 */
CORE.create_module("person-loader", function(sb) {    

    return {
        init : function () {
        	sb.listen({
        		'person-selected' : function (selected) {
        		
        			var ajaxRequest, doneCallback, failCallback;
        		
        			// ON SUCCESS
        			doneCallback = function(data, textStatus, jqXHR) {		
						if (data) { 
	   						sb.notify({
	   		                    type : 'person-loaded',
	   		                    data : data  
	   		                })
						}
					};
					
					// ON FAILURE (how can I test this?)
					failCallback = function(jqXHR, textStatus, errorThrown) {
						log("ajax error:" + textStatus 
							+ " status:" + jqXHR.status 
							+ " errorThrown: " + errorThrown);
   						sb.notify({
   		                    		type : 	'error-ajax',
   		                    		data : 	{"jqXHR" 		: jqXHR,
   		                    				"textStatus"	: textStatus,
   		                    				"errorThrown"	: errorThrown 
   		                    			}
   		                		})
					};
					
					// SEND
					ajaxRequest = sb.getJSON(selected.href, 
											doneCallback,
        									failCallback);
					
        			// Experimental - shows how to attach a callback later
					// on a Deferred object.
//        			ajaxRequest.done(function(data, textStatus, jqXHR){
//        				log("Experimental Deferred-start");
//        				log(data);
//        				log("Experimental Deferred-end");
//        			});
       			}
        	})
        },
        
        destroy : function () {
            sb.ignore(['person-selected']);
        },
        
    	/*
    	 * Returns the correct url for ajax calls whether this is hosted on:
    	 * 1. My local TC Server.
    	 * 2. My local CloudFoundry instance.
    	 * 3. My CloudFoundry.com instance.
    	 */
//    	url : function(){
//    		return location.href.slice(0, location.href.indexOf("js")) + "people";
//    	}
    };
});

/*******************************************************************************
 * DISPLAY A PERSON'S FAMILY
 * Listen for a 'person-selected' event. When this happens, 
 * display the person and their family.
 * Add a 'person-selected' event which fires whenever a family member is clicked.
 */
CORE.create_module("family-panel", function(sb) {

    return {
        init : function () {
        	var	panel 			= sb.find("#person")[0],
        	 	familyTemplate	= sb.find('#templ-person')[0],
        	 	that = this,
        	 	familyHTML;
        	
        	sb.listen({'person-loaded':function (person){
        		log(person);
        		
        		sb.removeEvent(panel, 'click', that.handleSelect);
        		
        		familyHTML = sb.loadTemplate(familyTemplate,
        				{"person" : person});
        		panel.innerHTML = familyHTML;

        		//        		resultsListItems = sb.loadTemplate(resultsTemplate,
//				{"foundPeople" : searchResults});
//				resultsList.innerHTML = resultsListItems;
        		
        		// underscore template
        		//var templMarkup = $('#templ-person').html();
        		//var compiledTmpl = _.template(templMarkup, {"person" : person});

        		
        		// populate results ul
        		//$('#person').html(compiledTmpl);
        		
        		// Publish 'person-selected' when a link is clicked.
        		
        		sb.addEvent(panel, 'click', that.handleSelect);
        		
        	}
        		
        	});
        },
        
        destroy : function () {
            sb.removeEvent(panel, "click", this.handleSelect );
            sb.ignore(['person-selected']);
            compiledTmpl = null;
        },
        
        handleSelect : function (e) {
    		var target = e.target;
    		var tagName = target.tagName;
    		if(tagName === 'A'){
    			e.preventDefault();
    			// Publish
                sb.notify({
                    type : 'person-selected',
                    data : target
                });
    		}
        },
    };
});

CORE.start_all();