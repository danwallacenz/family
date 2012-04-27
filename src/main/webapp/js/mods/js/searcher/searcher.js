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
            sb.ignore(['perform-search']);
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