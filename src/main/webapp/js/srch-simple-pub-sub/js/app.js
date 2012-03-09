
(function($) {

	/*
	 * Returns the correct url for ajax calls whether this is hosted on:
	 * 1. My local TC Server.
	 * 2. My local CloudFoundry instance.
	 * 3. My CloudFoundry.com instance.
	 */
	var url = function url(){
		return location.href.slice(0, location.href.indexOf("js")) + "people";
	}
	
	/*
	 * When notified that the input form has been submitted by clicking the 
	 * search button, an ajax call is made which searches for people with names
	 * like the search term.
	 * 
	 * Topic: '/search/term'
	 */
	$.subscribe('/search/term', function(term) {
		$.getJSON(
			url(),
			//'http://localhost:8080/family/people',
			//$("#host").val(),
			{find: "ByNameLike", name: term},
			function(resp, textStatus, jqXHR) {
				console.log(jqXHR.responseText);
				if (!jqXHR.responseText) {
					return; 
				}
				$.publish('/search/results', [ jqXHR.responseText ]);
			}
		);
		
		/*
		 * $.ajax version
		 */
		/*
		var jqxhr = $.ajax({
			  url: 'http://localhost:8080/family/people',
			  data: "find=ByNameLike&name=" + term,
			  //dataType: "json",
			  success:
				  function(resp, textStatus, jqXHR) {//data, textStatus, jqXHR	
		    		console.log("resp"+resp);
		    		if (!jqXHR.responseText){
		    			return; 
		    		}
		    		$.publish('/search/results', [ jqXHR.responseText ]);	
		    	},
		    	error:
		    		function(resp, textStatus, errorThrown){
		    			console.log(resp.responseText);
		    			console.log(textStatus);
		    			console.log(errorThrown);
		    			alert("error:"+textStatus);
		    	},
		    	complete: 
		    		function(){
		    			console.log("finished searching for '" + term + "'");
		    	}
			});*/
		});
		
	/*
	 * Subscribe to the ajax call returning event (Topic:'/search/results') 
	 * and display the search results after ajax call returns.
	 */
	$.subscribe('/search/results', function(results) {

		var searchResults = $.parseJSON(results).searchResults;
		console.log(searchResults);
		
		var tmpl = '<li><p><a href="{{url}}">{{name}}, born on:{{dob}} at: {{placeOfBirth}}, died on:{{dod}}, at:{{placeOfDeath}}</a></p></li>',
		
		html = $.map(searchResults, function(searchResults) {
	        return tmpl
	          .replace('{{url}}', searchResults.href)
	          .replace('{{name}}', searchResults.name)
	          .replace('{{dob}}', searchResults.dob)
	          .replace('{{placeOfBirth}}', searchResults.placeOfBirth)
	          .replace('{{dod}}', searchResults.dod)
	          .replace('{{placeOfDeath}}', searchResults.placeOfDeath)
	          
	      }).join('');
	  $('#results').html(html);
	});

	/* 
	 * When the input form is submitted by clicking the search button, this 
	 * event is published with the search term riding along.
	 * Topic: '/search/term'
	 */
	$(document).ready(function() {
		$('#searchForm').submit(function(e) {
			e.preventDefault();
			var term = $.trim($(this).find('input[name="q"]').val());
			if (!term) {
				return;
			}
			// Publish
			$.publish('/search/term', [ term ]);
		});
	});
	
	/*
	 * Display the search term in the previous search list when notified by the 
	 * publication of the search form submit event.
	 * Topic: '/search/term'
	 */
	$.subscribe('/search/term', function(term) {
	  $('#searches').append('<li>' + term + '</li>');
	});
	
	/*
	 * Initialize ajax. Do we need this?
	 */
	$(document).ready(function() {
		$.ajaxSetup({
			cache: false,		// ??
			dataType: "json",	// Not relevant to getJSON.
			//type: "GET",		// Default.
		});
	});
	
})(jQuery);