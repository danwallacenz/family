**This implements a service which exposes a RESTful JSON interface which manipulates genealogical data.
It is designed to be used as a service upon which different experimental Javascript clients can be evaluated.**

# Family API

--------------------------------------------------------------------------------------------------------------------
## Create a Person - POST
    
    
### Request
    
    POST http://localhost:8080/family/people
    Host: localhost:8080
    Content-Type : application/json
    Content-Length: ...
    
    { 	
    	"name" : "Daniel Wallace",
  		"sex" : "MALE"
	}
     
     
### Response
    
	201 Created
	Location: http://localhost:8080/family/people/1
	Content-Type: Content-Type : application/json 
	Content-Length: ...
     
	{
		"id" : 1,	
		"name" : "Daniel Wallace",
  		"sex" : "MALE",
  		"father": null,
  		"mother": null,
  		"children" : null,
  		"version" : 0,
  		"links" :
  			[
	  			{
	  				"rel" : "self",
	  				"href": "http://localhost:8080/family/people/1",
	  				"title": "Daniel Wallace"
	  			},
	   			{
	  				"rel" : "father",
	  				"href": "http://localhost:8080/family/people/1/father",
	  				"title": "Father"
	  			},
	   			{
	  				"rel" : "mother",
	  				"href": "http://localhost:8080/family/people/1/mother",
	  				"title": "Mother"
	  			},
	  			{
	  				"rel" : "children",
	  				"href": "http://localhost:8080/family/people/1/children",
	  				"title": "children"
	  			}
  		 	] 
	}        
        
        
--------------------------------------------------------------------------------------------------------------------

## Update a person (instance variables, not relationships)
     
PUT - $URL/person/{id}  Update a Person's instance state given a JSON representation.  Do not update relationships.
Return a URI for that Person.  
Return status: 200 OK.
     
### Request
     
  	PUT http://localhost:8080/family/people/1
    Host: localhost:8080
    Content-Type : application/json
    Content-Length: ...
    
    { 	
    	"id" : 1,
    	"name" : "Daniel Roy Wallace",
  		"sex" : "MALE",
  		"version": 0
	}
     
     
### Response
    
	200 OK
	Location: http://localhost:8080/family/people/1
	Content-Type: Content-Type : application/json 
	Content-Length: ...
     
	{
		"id" : 1,	
		"name" : "Daniel Roy Wallace",
  		"sex" : "MALE",
  		"father": null,
  		"mother": null,
  		"children" : null,
  		"version" : 1,
  		"links" :
  			[
	  			{
	  				"rel" : "self",
	  				"href": "http://localhost:8080/family/people/1",
	  				"title": "Daniel Wallace"
	  			},
	   			{
	  				"rel" : "father",
	  				"href": "http://localhost:8080/family/people/1/father",
	  				"title": "Father"
	  			},
	   			{
	  				"rel" : "mother",
	  				"href": "http://localhost:8080/family/people/1/mother",
	  				"title": "Mother"
	  			},
	  			{
	  				"rel" : "children",
	  				"href": "http://localhost:8080/family/people/1/children",
	  				"title": "children"
	  			}
  		 	]
	}   
  
  
---------------------------------------------------------------------------------------
## Create and add a Mother
 
#### Steps   
1. Create a person who will be the mother 
2. Link her to the previously created person.
    
## Step 1 - Create a person who will be the mother.    
### Request

    
    POST http://localhost:8080/family/people
    Host: localhost:8080
    Content-Type : application/json
    Content-Length: ...
    
    { 	
    	"name" : "Joan Wallace",
  		"sex" : "FEMALE"
	}
     
     
### Response
    
	201 Created
	Location: http://localhost:8080/family/people/2
	Content-Type: Content-Type : application/json 
	Content-Length: ...
     
	{
		"id" : 2,	
		"name" : "Joan Wallace",
  		"sex" : "FEMALE",
  		"father": null,
  		"mother": null,
  		"children" : null,
  		"version" : 0,
  		"links" :
  			[
	  			{
	  				"rel" : "self",
	  				"href": "http://localhost:8080/family/people/2",
	  				"title": "Joan Wallace"
	  			},
	   			{
	  				"rel" : "father",
	  				"href": "http://localhost:8080/family/people/2/father",
	  				"title": "Father"
	  			},
	   			{
	  				"rel" : "mother",
	  				"href": "http://localhost:8080/family/people/2/mother",
	  				"title": "Mother"
	  			},
	  			{
	  				"rel" : "children",
	  				"href": "http://localhost:8080/family/people/2/children",
	  				"title": "children"
	  			}
  		 	] 
	}  
 
 
## Step 2 - Link her to the previously created person.##
  
### Request
     
  	PUT http://localhost:8080/family/people/1/mother/2
    Host: localhost:8080
     
     
### Response
    
	200 OK
	Location: http://localhost:8080/family/people/1
	Content-Type: Content-Type : application/json 
	Content-Length: ...
     
	{
		"id" : 1,	
		"name" : "Daniel Roy Wallace",
  		"sex" : "MALE",
  		"father": null,
  		"mother": {
			"id" : 2,	
			"name" : "Joan Wallace",
  			"sex" : "FEMALE",
  			"version": 1
  		},
  		"children" : null,
  		"version" : 2,
  		"links" :
  			[
	  			{
	  				"rel" : "self",
	  				"href": "http://localhost:8080/family/people/1",
	  				"title": "Daniel Wallace"
	  			},
	   			{
	  				"rel" : "father",
	  				"href": "http://localhost:8080/family/people/1/father",
	  				"title": "Father"
	  			},
	   			{
	  				"rel" : "mother",
	  				"href": "http://localhost:8080/family/people/2",
	  				"title": "Mother"
	  			},
	  			{
	  				"rel" : "children",
	  				"href": "http://localhost:8080/family/people/1/children",
	  				"title": "children"
	  			}
  		 	],
  		"related-updates": 
  			[
  				{ "rel": "mother", "href": "http://localhost:8080/family/people/2" }
  			] 
	}     
  
#### Create a Person
	
		->curl -i -X POST -H "Content-Type: application/json" -H "Accept: application/json" -d 
		'{"name":"Daniel Roy Wallace",
   		"sex":"MALE",
   		"dob":"06/27/1957",
   		"placeOfBirth":"Te Awamutu, New Zealand"
		}'
	 	http://family.danwallacenz.cloudfoundry.me/people
		HTTP/1.1 201 Created
		Server: nginx
		Date: Sat, 03 Mar 2012 22:58:43 GMT
		Content-Type: application/json;charset=UTF-8
		Connection: keep-alive
		Keep-Alive: timeout=20
		Location: http://family.danwallacenz.cloudfoundry.me/people/3
		Content-Length: 630

		{
		   "id":3,
		   "name":"Daniel Roy Wallace",
		   "version":0,
		   "sex":"MALE",
		   "dob":"27/06/1957",
		   "dod":"unknown",
		   "placeOfBirth":"Te Awamutu, New Zealand",
		   "placeOfDeath":null,
		   "father":"null",
		   "mother":"null",
		   "children":[
		
		   ],
		   "links":[
		      {
		         "rel":"self",
		         "href":"http://family.danwallacenz.cloudfoundry.me/people/3",
		         "title":"Daniel Roy Wallace"
		      },
		      {
		         "rel":"father",
		         "href":"http://family.danwallacenz.cloudfoundry.me/people/3/father",
		         "title":"Father"
		      },
		      {
		         "rel":"mother",
		         "href":"http://family.danwallacenz.cloudfoundry.me/people/3/mother",
		         "title":"Mother"
		      },
		      {
		         "rel":"children",
		         "href":"http://family.danwallacenz.cloudfoundry.me/people/3/children",
		         "title":"Children"
		      }
		   ]
		}

		{"id":3,"name":"Daniel Roy Wallace","version":0,"sex":"MALE","dob":"27/06/1957","dod":"unknown","placeOfBirth":"Te Awamutu, New Zealand","placeOfDeath":null,"father":"null","mother":"null","children":[],"links":[{"rel":"self","href":"http://family.danwallacenz.cloudfoundry.me/people/3","title":"Daniel Roy Wallace"},{"rel":"father","href":"http://family.danwallacenz.cloudfoundry.me/people/3/father","title":"Father"},{"rel":"mother","href":"http://family.danwallacenz.cloudfoundry.me/people/3/mother","title":"Mother"},{"rel":"children","href":"http://family.danwallacenz.cloudfoundry.me/people/3/children","title":"Children"}]}

#### Read Him Back

		->curl -i -H "Accept: application/json" http://family.danwallacenz.cloudfoundry.me/people/3
		HTTP/1.1 200 OK
		Server: nginx
		Date: Sat, 03 Mar 2012 23:42:24 GMT
		Content-Type: application/json;charset=utf-8
		Connection: keep-alive
		Keep-Alive: timeout=20
		Content-Length: 630

		{"id":3,"name":"Daniel Roy Wallace","version":0,"sex":"MALE","dob":"27/06/1957","dod":"unknown","placeOfBirth":"Te Awamutu, New Zealand","placeOfDeath":null,"father":"null","mother":"null","children":[],"links":[{"rel":"self","href":"http://family.danwallacenz.cloudfoundry.me/people/3","title":"Daniel Roy Wallace"},{"rel":"father","href":"http://family.danwallacenz.cloudfoundry.me/people/3/father","title":"Father"},{"rel":"mother","href":"http://family.danwallacenz.cloudfoundry.me/people/3/mother","title":"Mother"},{"rel":"children","href":"http://family.danwallacenz.cloudfoundry.me/people/3/children","title":"Children"}]}
		

#### Create his Mother	

		->curl -i -X POST -H "Content-Type: application/json" -H "Accept: application/json" -d '{"name": "Joan Margaret Carter", "sex" : "FEMALE", "dob": "02/14/1928", "placeOfBirth" : "Nelson, New Zealand"}' http://family.danwallacenz.cloudfoundry.me/people
		HTTP/1.1 201 Created
		Server: nginx
		Date: Sat, 03 Mar 2012 23:10:38 GMT
		Content-Type: application/json;charset=UTF-8
		Connection: keep-alive
		Keep-Alive: timeout=20
		Location: http://family.danwallacenz.cloudfoundry.me/people/6
		Content-Length: 632

		{"id":6,"name":"Joan Margaret Carter","version":0,"sex":"FEMALE","dob":"14/02/1928","dod":"unknown","placeOfBirth":"Nelson, New Zealand","placeOfDeath":null,"father":"null","mother":"null","children":[],"links":[{"rel":"self","href":"http://family.danwallacenz.cloudfoundry.me/people/6","title":"Joan Margaret Carter"},{"rel":"father","href":"http://family.danwallacenz.cloudfoundry.me/people/6/father","title":"Father"},{"rel":"mother","href":"http://family.danwallacenz.cloudfoundry.me/people/6/mother","title":"Mother"},{"rel":"children","href":"http://family.danwallacenz.cloudfoundry.me/people/6/children","title":"Children"}]}

#### Link Mother and Son

	->curl -i -X PUT -H "Content-Type: application/json" -H "Accept: application/json" -d '' http://family.danwallacenz.cloudfoundry.me/people/5/mother/6
	HTTP/1.1 200 OK
	Server: nginx
	Date: Sat, 03 Mar 2012 23:29:50 GMT
	Content-Type: application/json;charset=UTF-8
	Connection: keep-alive
	Keep-Alive: timeout=20
	Location: http://family.danwallacenz.cloudfoundry.me/people/5
	Content-Length: 1337

	{"id":5,"name":"Daniel Roy Wallace","version":1,"sex":"MALE","dob":"27/06/1957","dod":"unknown","placeOfBirth":"Te Awamutu, New Zealand","placeOfDeath":null,"father":"null","mother":{"id":6,"name":"Joan Margaret Carter","sex":"FEMALE","dob":"14/02/1928","dod":"unknown","placeOfBirth":"Nelson, New Zealand","placeOfDeath":null,"version":1,"links":[{"rel":"self","href":"http://family.danwallacenz.cloudfoundry.me/people/6","title":"Joan Margaret Carter"},{"rel":"father","href":"http://family.danwallacenz.cloudfoundry.me/people/6/father","title":"Father"},{"rel":"mother","href":"http://family.danwallacenz.cloudfoundry.me/people/6/mother","title":"Mother"},{"rel":"children","href":"http://family.danwallacenz.cloudfoundry.me/people/6/children","title":"Children"}]},"children":[],"links":[{"rel":"self","href":"http://family.danwallacenz.cloudfoundry.me/people/5","title":"Daniel Roy Wallace"},{"rel":"father","href":"http://family.danwallacenz.cloudfoundry.me/people/5/father","title":"Father"},{"rel":"mother","href":"http://family.danwallacenz.cloudfoundry.me/people/6","title":"Joan Margaret Carter"},{"rel":"children","href":"http://family.danwallacenz.cloudfoundry.me/people/5/children","title":"Children"}],"affectedParties":[{"id":6,"name":"Joan Margaret Carter","href":"http://family.danwallacenz.cloudfoundry.me/people/6"}]}
	
    
