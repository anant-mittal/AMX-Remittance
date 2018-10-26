## Setup

### Assumptions
* project name in amx-jax repo : **ms-service**
* project name pom files : **ms-service**

###
* Create **deploy.ms-service.sh** , we can copy any existing module, **server-ui** for view based server and **ms-postman** pure rest based server, are good examples to copy
* go to build server portal ie jenkins create a new Item , here again we can copy any existing project and change the name of deployment script.
* go to deployment server, create **ms-service** folder in ~/jax folder
* run below command on server with admin/root permissions

```
sudo ln -s /home/devenvironment/jax/ms-service/ms-service-0.0.1-SNAPSHOT.jar /etc/init.d/ms-service
```
* optinally you can put application.properties in ~/jax/ms-service/ folder to override default properties
* start service in server, it is mendatory first time.

```
sudo /etc/init.d/ms-service start
```

* You can test your build from jenkin buid now. it will take some time, test it.
* DONE

