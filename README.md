This repo implements a service which exposes a RESTful JSON interface which manipulates genealogical data.
It is designed to be used as a service upon which different experimental Javascript clients can be evaluated.

**Family API**

    URL - http://localhost:8080/family
     
    GET - $URL/person/{id} Return a JSON representation of a Person.
     Return status: 200 OK.
    
    POST - $URL/person/{id}  Create a Person given a JSON representation. 
     Return a URI for that Person.  
     Return status: 201 created.

    PUT - $URL/person/{id}  Update a Person given a JSON representation.  
     Return a URI for that Person. Update collaborators. Do not return their URIs. 
     Return status: 200 OK.

    DELETE - $URL/person/{id} Delete a Person and remove all dependencies. 
     Update collaborators. Do not return their URIs. 
     Return status: 200 OK.


    
    
