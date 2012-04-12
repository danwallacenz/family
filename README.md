
#*familypeople*

--------------------------------------------------------------------------------------------------------------------

**This is a service which manipulates genealogical data.**

It exposes a RESTful JSON interface which is designed to be used as a service upon which different experimental clients can be evaluated.

Some **sample apps** can be viewed <a href="http://familypeople.cloudfoundry.com/home-cloudfoundry.html">here</a>.

I'm going to concentrate on Javascript, HTML5 and CSS3 clients; my friend Ovi is building an Android front-end.

--------------------------------------------------------------------------------------------------------------------
## Family API




  
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
		Location: http://family.danwallacenz.cloudfoundry.me/people/146
		Content-Length: 640
		
		{
		   "id":146,
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

##### Notes:
	
 1. __"id":146,__		
"name":"Daniel Roy Wallace",
__"version":0,__
"sex":"MALE",

    ___id__ is assigned by JPA, __version__ is for optimistic locking._

 2. Keep-Alive: timeout=20
__Location: http://family.danwallacenz.cloudfoundry.me/people/146__
Content-Length: 640

    _HTTP Header __Location__ contains the URL of the newly created Person._

 3. __"links"__:[
  {
 __"rel":"self",__
 __"href":"http://family.danwallacenz.cloudfoundry.me/people/146",__
 __"title":"Daniel Roy Wallace"__
  },
  {
 "rel":__"father"__,
 "href":"http://family.danwallacenz.cloudfoundry.me/people/146/father",
 "title":"Father"
  },
  {
 "rel":__"mother"__,
 "href":"http://family.danwallacenz.cloudfoundry.me/people/146/mother",
 "title":"Mother"
  },
  {
 "rel":__"children"__,
 "href":"http://family.danwallacenz.cloudfoundry.me/people/146/children",
 "title":"Children"
  }
]

    ___"links"__  attempts to follow recommended RESTful HATEOAS principles._

-------------------------------------------------------------------------
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
		
-------------------------------------------------------------------------
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
		   "version":1,
		   "sex":"MALE",
		   "dob":"27/06/1957",
		   "dod":"unknown",
		   "placeOfBirth":"Te Awamutu, New Zealand",
		   "placeOfDeath":null,
		   "father":"null",
		   "mother":{
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
		   },
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
		   ],
		   "affectedParties":[
		      {
		         "id":148,
		         "name":"Joan Margaret Carter",
		         "href":"http://family.danwallacenz.cloudfoundry.me/people/148"
		      }
		   ]
		}
		
##### Notes:

1."__affectedParties__":[
  {
   "id":148,
   "name":"Joan Margaret Carter",
   "href":"http://family.danwallacenz.cloudfoundry.me/people/148"
  }
]

2."links":[
      {
...
      },
      {
         "rel":"mother",
         __"href":"http://family.danwallacenz.cloudfoundry.me/people/148",
         "title":"Joan Margaret Carter"__
      },
...
   ],

3.
...
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
   "version":1,__
...

-------------------------------------------------------------------------
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
		   "children":[
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
		
##### Notes:
1.
..
 "mother":"null",
 __"children":[
 {
   "id":146,
   "version":1,
   "name":"Daniel Roy Wallace",
   "sex":"MALE",
   "dob":"27/06/1957",__
...

-------------------------------------------------------------------------
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
	
-------------------------------------------------------------------------
# Notes to myself	
  		
### Accessing MySQL in CloudFoundry VMs

1. vmc target http://api.familypeople.cloudfoundry.com
2. vmc target http://api.danwallacenz.cloudfoundry.me

		->vmc target http://api.danwallacenz.cloudfoundry.me
		Successfully targeted to [http://api.danwallacenz.cloudfoundry.me]
		
		->vmc login
		Attempting login to [http://api.danwallacenz.cloudfoundry.me]
		Email: daniel.wallace.nz@gmail.com
		Password: ********
		Successfully logged into [http://api.danwallacenz.cloudfoundry.me]
		
		->vmc apps

		+-------------+----+---------+----------------------------------------------+-------------+
		| Application | #  | Health  | URLS                                         | Services    |
		+-------------+----+---------+----------------------------------------------+-------------+
		| caldecott   | 1  | RUNNING | caldecott-7ee9e.danwallacenz.cloudfoundry.me | mysql-c9b0f |
		| family      | 1  | RUNNING | family.danwallacenz.cloudfoundry.me          | mysql-c9b0f |
		+-------------+----+---------+----------------------------------------------+-------------+
		
		->vmc tunnel mysql-c9b0f
		Password: ********
		Getting tunnel connection info: OK

		Service connection info: 
  		username : uZItJ1E8jJsZh
  		password : p905HD4CXuJ9L
  		name     : db9f50d98499e466baa3240bca1099f67

		Starting tunnel to mysql-c9b0f on port 10000.
		1: none
		2: mysqldump
		3: mysql
		
		mysql>
