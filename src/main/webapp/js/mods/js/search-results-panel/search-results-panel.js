/*******************************************************************************
 * DISPLAY SEARCH RESULTS
 * Listen for a 'results-returned' event. When this happens, display the results.
 * Add a 'person-selected' event which fires whenever a single result is clicked.
 */
CORE.create_module("search-results-panel", function(sb) {

    return {
        init : function () {
            var resultsList 	= sb.find("#templ-results-socket")[0],
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