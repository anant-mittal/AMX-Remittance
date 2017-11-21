## JAVA 
1. Download JAVA 8 [link](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
1. Set Java Env Path
	* Mac [reference](https://www.mkyong.com/java/how-to-set-java_home-environment-variable-on-mac-os-x/) or [ref2](http://www.sajeconsultants.com/how-to-set-java_home-on-mac-os-x/)
	
	```
		$ nano ~/.profile
		
		//Add this line and exit 
		export JAVA_HOME=$(/usr/libexec/java_home)
		
		$ source ~/.profile
		
		//test it
		$ echo $JAVA_HOME
		
	```

## MAVEN
1. Download Maven [link](https://maven.apache.org/download.cgi)
2. Extract files and rename folder to apache-maven and put it in your Projects folder
    ```
     /Users/lalittanwar/Projects/apache-maven
    ```
3. Add the *bin* directory of the created directory apache-maven to the *PATH* environment variable [reference](https://maven.apache.org/install.html)
    *  Mac
    ```
        $ nano ~/.profile
        
        //Add this line
        export PATH=/Users/lalittanwar/Projects/apache-maven/bin:$PATH
        
        $ source ~/.profile
        
        //Test it
        $ mvn -v
    ```
4. Run setup.sh first before importing project
    ```
    $ bash .setup.sh
    ```

## BUILD
http://www.codetab.org/apache-maven-tutorial/maven-multi-module-project/
