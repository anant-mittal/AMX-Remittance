

## User and DB creation

* Start MongoDB without access control.
		
``` 
mongod --port 27017 --dbpath /data/db 
```

* Connect to the instance.

```
mongo --port 27017
```


*  Create the user administrator (in the admin authentication database).

```
use admin
db.createUser(
  {
    user: "myUserAdmin",
    pwd: "abc123",
    roles: [ { role: "userAdminAnyDatabase", db: "admin" } ]
  }
)
```

*  Re-start the MongoDB instance with access control.

```
mongod --auth --port 27017 --dbpath /data/db
```


*  Connect and authenticate as the user administrator.

```
mongo --port 27017 -u "myUserAdmin" -p "abc123" --authenticationDatabase "admin"
```

*  Create additional users as needed for your deployment (in the logger authentication database).

```
use logger
db.createUser(
  {
    user: "logger",
    pwd: "logger",
    roles: [ { role: "readWrite", db: "logger" },
             { role: "read", db: "reporting" } ]
  }
)
```


*  Connect and authenticate as logger.

```
mongo --port 27017 -u "logger" -p "logger" --authenticationDatabase "logger"
```


