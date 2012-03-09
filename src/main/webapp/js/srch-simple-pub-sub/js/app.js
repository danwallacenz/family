


(function($) {

	$.subscribe('/search/term', function(term) {

//		$.getJSON(
//			'http://localhost:8080/family/people/?find=ByNameLike&name=Brendon',
//			function(resp) {
//	    		console.log(resp);
//	    		if (!resp.length) { return; }
//	    		$.publish('/search/results', [ resp ]);
//	    	}
//		);

		
		

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
			});
		
	});

	// look ma, a new feature!
	$.subscribe('/search/term', function(term) {
	  $('#searches').append('<li>' + term + '</li>');
	});

	$.subscribe('/search/results', function(results) {
		console.log(results);
		var searchResults = $.parseJSON(results).searchResults;
		console.log(searchResults);
		var tmpl = '<li><p><a href="{{url}}">{{title}}</a></p></li>',
		html = $.map(searchResults, function(searchResults) {
	        return tmpl
	          .replace('{{url}}', searchResults.href)
	          .replace('{{title}}', searchResults.name)
	      }).join('');
	  $('#results').html(html);
	});

	//	function getQuery(term) {
	//	  return 'select title,url from search.news where query="' + term + '"';
	//	}

	// Event handlers
	$(document).ready(function() {
		$('#searchForm').submit(function(e) {
			e.preventDefault();
			var term = $.trim($(this).find('input[name="q"]').val());
			if (!term) { return; }
			$.publish('/search/term', [ term ]);
		});
	});
	
	// Initialize ajax.
	$(document).ready(function() {
		$.ajaxSetup({
			//cache: false,
			dataType: "json",
			//type: "GET",
		});
	});
	
})(jQuery);