/* 
 * Author: Daniel Wallace - daniel.wallace.nz@gmail.com
*/

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
	
	var href = function href(person){
		return person.links[0].href;
	}
	
	var clickHandler = function(e) {
		var target = e.target;
		var tagName = target.tagName;
		if(tagName === 'A'){
			e.preventDefault();
			// Publish
			$.publish('/results/select', [ target ]);
		}
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
			{find: "ByNameLike", name: term},
			function(resp, textStatus, jqXHR) {
				console.log(jqXHR.responseText);
				if (!jqXHR.responseText) {
					return; 
				}
				$.publish('/search/results', [ jqXHR.responseText ]);
			}
		);
	});
		
	/*
	 * Subscribe to the ajax call returning event (Topic:'/search/results') 
	 * and display the search results after ajax call returns.
	 */
	$.subscribe('/search/results', function(results) {

		var searchResults, tmpl, html;
		
		// extract from ugly namespacing
		searchResults = $.parseJSON(results).searchResults;
		console.log(searchResults);
				
		// remove previous event listeners from #results a
		$('#results').unbind('click', clickHandler);
		
		// underscore template
		var templMarkup = $('#templ-results').html();
		var compiledTmpl = _.template(templMarkup, {"foundPeople" : searchResults});

		// populate results ul
		$('#results').html(compiledTmpl);
		
		// Publish '/results/select' when a link is clicked.
		$('#results').click(clickHandler);	  
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
	
	/*------------------------------------------------------------------*/

	/* Get person data from host when selected in the results list */
	$.subscribe('/results/select', function(selected) {
		log(selected);
		
		$.getJSON(
				selected.href,
				function(resp, textStatus, jqXHR) {
					console.log(jqXHR.responseText);
					if (!jqXHR.responseText) {
						return; 
					}
					// Publish
					$.publish('/person/loaded', [ jqXHR.responseText ]);
				}
			);
	});

	
	// Display a person when JSON returned from server.
	$.subscribe('/person/loaded', function(personJSON) {
		var person = $.parseJSON(personJSON);
		
		$('#person').unbind('click', clickHandler);
		
		log("person.name = "  + person.name);
		
		// underscore template
		var templMarkup = $('#templ-person').html();
		var compiledTmpl = _.template(templMarkup, {"person" : person});

		// populate results ul
		$('#person').html(compiledTmpl);
		 

		// Publish '/results/select' when a link is clicked.
		$('#person').click(clickHandler);
	});
	
	/*------------------------------------------------------------------*/
	
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




