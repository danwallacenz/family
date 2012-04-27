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