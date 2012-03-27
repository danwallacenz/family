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
			log(e);
			log(e.target);
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
		
		searchResults = $.parseJSON(results).searchResults;
		console.log(searchResults);
		
		tmpl = '<li><p><a href="{{url}}">{{name}}, born on:{{dob}} at: {{placeOfBirth}}, died on:{{dod}}, at:{{placeOfDeath}}</a></p></li>';
		
		html = $.map(searchResults, function(searchResults) {
	        return tmpl
	          .replace('{{url}}', searchResults.href)
	          .replace('{{name}}', searchResults.name)
	          .replace('{{dob}}', searchResults.dob)
	          .replace('{{placeOfBirth}}', searchResults.placeOfBirth)
	          .replace('{{dod}}', searchResults.dod)
	          .replace('{{placeOfDeath}}', searchResults.placeOfDeath)         
	      }).join('');
		
		// remove previous event listeners from #results a
		//$('#results a').unbind('click', clickHandler);
		$('#results').unbind('click', clickHandler);
		
		// populate results ul
		$('#results').html(html);
	  
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
		var relationTmpl = '<p><a href="{{url}}">{{name}}, born on:{{dob}} at: {{placeOfBirth}}, died on:{{dod}}, at:{{placeOfDeath}}</a></p>';
		var personTmpl = '<p><strong>{{name}}</strong>  born on {{dob}} at {{placeOfBirth}} died on {{dod}} at {{placeOfDeath}}</p>';
		var motherStr = "Mother unknown";
		var fatherStr = "Father unknown";
		var childrenStr = "No children";
		
		$('#person').unbind('click', clickHandler);
		
		log("person.name = "  + person.name);
		var personStr = personTmpl
	        .replace('{{name}}', person.name)
	        .replace('{{dob}}', person.dob)
	        .replace('{{placeOfBirth}}', person.placeOfBirth)
	        .replace('{{dod}}', person.dod)
	        .replace('{{placeOfDeath}}', person.placeOfDeath);
			log("personStr=" + personStr);
		
		if(person.father !== null){
			 fatherStr = relationTmpl
			 .replace('{{url}}', href(person.father))
	        .replace('{{name}}', person.father.name)
	        .replace('{{dob}}', person.father.dob)
	        .replace('{{placeOfBirth}}', person.father.placeOfBirth)
	        .replace('{{dod}}', person.father.dod)
	        .replace('{{placeOfDeath}}', person.father.placeOfDeath);
			log("fatherStr=" + fatherStr);
		} 
		
		if(person.mother !== null){
			motherStr = relationTmpl
			.replace('{{url}}', href(person.mother))
	        .replace('{{name}}', person.mother.name)
	        .replace('{{dob}}', person.mother.dob)
	        .replace('{{placeOfBirth}}', person.mother.placeOfBirth)
	        .replace('{{dod}}', person.mother.dod)
	        .replace('{{placeOfDeath}}', person.mother.placeOfDeath);
			log("motherStr=" + motherStr);
		}

		childrenStr = $.map(person.children,  function(child) {
			return relationTmpl
			.replace('{{url}}', href(child))
	        .replace('{{name}}', child.name)
	        .replace('{{dob}}', child.dob)
	        .replace('{{placeOfBirth}}', child.placeOfBirth)
	        .replace('{{dod}}', child.dod)
	        .replace('{{placeOfDeath}}', child.placeOfDeath);
			}).join('');
		
        $('#person').empty().append(personStr).append(fatherStr).append(motherStr).append(childrenStr);
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




