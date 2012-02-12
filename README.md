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
  




    
    
