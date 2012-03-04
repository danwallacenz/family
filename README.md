**This implements a service which exposes a RESTful JSON interface which manipulates genealogical data.
It is designed to be used as a service upon which different experimental Javascript clients can be evaluated.**

# Family API

--------------------------------------------------------------------------------------------------------------------
     
  
### Using curl
  
#### Create a new Person
		->curl -i -X POST -H "Content-Type: application/json" -H "Accept: application/json" 
				-d '{
   		"name":"Daniel Roy Wallace",
   		"sex":"MALE",
   		"dob":"06/27/1957",
   		"placeOfBirth":"Te Awamutu, New Zealand"
		}' 
				http://family.danwallacenz.cloudfoundry.me/people
		HTTP/1.1 201 Created
		Server: nginx
		Date: Sat, 03 Mar 2012 22:56:03 GMT
		Content-Type: application/json;charset=UTF-8
		Connection: keep-alive
		Keep-Alive: timeout=20
		__Location: http://family.danwallacenz.cloudfoundry.me/people/146__
		Content-Length: 640
		
		{
		   "id":146,
		   "name":"Daniel Roy Wallace",
		   __"version":0,__
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
		      __{
		         "rel":"self",
		         "href":"http://family.danwallacenz.cloudfoundry.me/people/146",
		         "title":"Daniel Roy Wallace"
		      }__,
		      {
		         "rel":"father",
		         "href":"http://family.danwallacenz.cloudfoundry.me/people/146/father",
		         "title":"Father"
		      },
		      {
		         "rel":"mother",
		         "href":"http://family.danwallacenz.cloudfoundry.me/people/146/mother",
		         "title":"Mother"
		      },
		      {
		         "rel":"children",
		         "href":"http://family.danwallacenz.cloudfoundry.me/people/146/children",
		         "title":"Children"
		      }
		   ]
		}
		

#### Create his mother
		->curl -i -X POST -H "Content-Type: application/json" -H "Accept: application/json" 
				-d '{"name": "Joan Margaret Carter", "sex" : "FEMALE", "dob": "02/14/1928", "placeOfBirth" : "Nelson, New Zealand"}' 
				http://family.danwallacenz.cloudfoundry.me/people
		HTTP/1.1 201 Created
		Server: nginx
		Date: Sat, 03 Mar 2012 22:57:04 GMT
		Content-Type: application/json;charset=UTF-8
		Connection: keep-alive
		Keep-Alive: timeout=20
		Location: http://family.danwallacenz.cloudfoundry.me/people/148
		Content-Length: 642
		
		{
		   "id":148,
		   "name":"Joan Margaret Carter",
		   "version":0,
		   "sex":"FEMALE",
		   "dob":"14/02/1928",
		   "dod":"unknown",
		   "placeOfBirth":"Nelson, New Zealand",
		   "placeOfDeath":null,
		   "father":"null",
		   "mother":"null",
		   "children":[
		
		   ],
		   "links":[
		      {
		         "rel":"self",
		         "href":"http://family.danwallacenz.cloudfoundry.me/people/148",
		         "title":"Joan Margaret Carter"
		      },
		      {
		         "rel":"father",
		         "href":"http://family.danwallacenz.cloudfoundry.me/people/148/father",
		         "title":"Father"
		      },
		      {
		         "rel":"mother",
		         "href":"http://family.danwallacenz.cloudfoundry.me/people/148/mother",
		         "title":"Mother"
		      },
		      {
		         "rel":"children",
		         "href":"http://family.danwallacenz.cloudfoundry.me/people/148/children",
		         "title":"Children"
		      }
		   ]
		}
		

#### Link mother and son (n.b., Ids will vary)

		->curl -i -X PUT -H "Content-Type: application/json" -H "Accept: application/json" 
			-d '' http://family.danwallacenz.cloudfoundry.me/people/146/mother/148
		HTTP/1.1 200 OK
		Server: nginx
		Date: Sat, 03 Mar 2012 22:58:29 GMT
		Content-Type: application/json;charset=UTF-8
		Connection: keep-alive
		Keep-Alive: timeout=20
		Location: http://family.danwallacenz.cloudfoundry.me/people/146
		Content-Length: 1361
		
		{
		   "id":146,
		   "name":"Daniel Roy Wallace",
		   __"version":1,__
		   "sex":"MALE",
		   "dob":"27/06/1957",
		   "dod":"unknown",
		   "placeOfBirth":"Te Awamutu, New Zealand",
		   "placeOfDeath":null,
		   "father":"null",
		   __"mother":{
		      "id":148,
		      "name":"Joan Margaret Carter",
		      "sex":"FEMALE",
		      "dob":"14/02/1928",
		      "dod":"unknown",
		      "placeOfBirth":"Nelson, New Zealand",
		      "placeOfDeath":null,
		      "version":1,
		      "links":[
		         {
		            "rel":"self",
		            "href":"http://family.danwallacenz.cloudfoundry.me/people/148",
		            "title":"Joan Margaret Carter"
		         },
		         {
		            "rel":"father",
		            "href":"http://family.danwallacenz.cloudfoundry.me/people/148/father",
		            "title":"Father"
		         },
		         {
		            "rel":"mother",
		            "href":"http://family.danwallacenz.cloudfoundry.me/people/148/mother",
		            "title":"Mother"
		         },
		         {
		            "rel":"children",
		            "href":"http://family.danwallacenz.cloudfoundry.me/people/148/children",
		            "title":"Children"
		         }
		      ]
		   },__
		   "children":[
		
		   ],
		   "links":[
		      {
		         "rel":"self",
		         "href":"http://family.danwallacenz.cloudfoundry.me/people/146",
		         "title":"Daniel Roy Wallace"
		      },
		      {
		         "rel":"father",
		         "href":"http://family.danwallacenz.cloudfoundry.me/people/146/father",
		         "title":"Father"
		      },
		      __{
		         "rel":"mother",
		         "href":"http://family.danwallacenz.cloudfoundry.me/people/148",
		         "title":"Joan Margaret Carter"
		      },__
		      {
		         "rel":"children",
		         "href":"http://family.danwallacenz.cloudfoundry.me/people/146/children",
		         "title":"Children"
		      }
		   ],
		   __"affectedParties":[
		      {
		         "id":148,
		         "name":"Joan Margaret Carter",
		         "href":"http://family.danwallacenz.cloudfoundry.me/people/148"
		      }
		   ]__
		}
		

#### The mother has a child now

		->curl -i -H "Accept: application/json" http://family.danwallacenz.cloudfoundry.me/people/148
		HTTP/1.1 200 OK
		Server: nginx
		Date: Sat, 03 Mar 2012 23:51:01 GMT
		Content-Type: application/json;charset=utf-8
		Connection: keep-alive
		Keep-Alive: timeout=20
		Content-Length: 1272
		
		{
		   "id":148,
		   "name":"Joan Margaret Carter",
		   "version":1,
		   "sex":"FEMALE",
		   "dob":"14/02/1928",
		   "dod":"unknown",
		   "placeOfBirth":"Nelson, New Zealand",
		   "placeOfDeath":null,
		   "father":"null",
		   "mother":"null",
		   __"children":[
		      {
		         "id":146,
		         "version":1,
		         "name":"Daniel Roy Wallace",
		         "sex":"MALE",
		         "dob":"27/06/1957",
		         "dod":"unknown",
		         "placeOfBirth":"Te Awamutu, New Zealand",
		         "placeOfDeath":null,
		         "father":"null",
		         "mother":148,
		         "links":[
		            {
		               "rel":"self",
		               "href":"http://family.danwallacenz.cloudfoundry.me/people/146",
		               "title":"Daniel Roy Wallace"
		            },
		            {
		               "rel":"father",
		               "href":"http://family.danwallacenz.cloudfoundry.me/people/146/father",
		               "title":"Father"
		            },
		            {
		               "rel":"mother",
		               "href":"http://family.danwallacenz.cloudfoundry.me/people/148",
		               "title":"Joan Margaret Carter"
		            },
		            {
		               "rel":"children",
		               "href":"http://family.danwallacenz.cloudfoundry.me/people/146/children",
		               "title":"Children"
		            }
		         ]
		      }
		   ],__
		   "links":[
		      {
		         "rel":"self",
		         "href":"http://family.danwallacenz.cloudfoundry.me/people/148",
		         "title":"Joan Margaret Carter"
		      },
		      {
		         "rel":"father",
		         "href":"http://family.danwallacenz.cloudfoundry.me/people/148/father",
		         "title":"Father"
		      },
		      {
		         "rel":"mother",
		         "href":"http://family.danwallacenz.cloudfoundry.me/people/148/mother",
		         "title":"Mother"
		      },
		      {
		         "rel":"children",
		         "href":"http://family.danwallacenz.cloudfoundry.me/people/148/children",
		         "title":"Children"
		      }
		   ]
		}
		

#### Searching
		->curl -i -H "Accept: application/json"  "http://family.danwallacenz.cloudfoundry.me/people?name=__an__&find=ByNameLike"
		HTTP/1.1 200 OK
		Server: nginx
		Date: Sat, 03 Mar 2012 23:05:28 GMT
		Content-Type: application/json;charset=utf-8
		Connection: keep-alive
		Keep-Alive: timeout=20
		Content-Length: 631
		
		{
		   "searchResults":[
		      {
		         "id":146,
		         "name":"Daniel Roy Wallace",
		         "sex":"MALE",
		         "dob":"27/06/1957",
		         "dod":"unknown",
		         "placeOfBirth":"Te Awamutu, New Zealand",
		         "placeOfDeath":null,
		         "href":"http://family.danwallacenz.cloudfoundry.me/146"
		      },
		      {
		         "id":148,
		         "name":"Joan Margaret Carter",
		         "sex":"FEMALE",
		         "dob":"14/02/1928",
		         "dod":"unknown",
		         "placeOfBirth":"Nelson, New Zealand",
		         "placeOfDeath":null,
		         "href":"http://family.danwallacenz.cloudfoundry.me/148"
		      }
		   ]
		}
		  