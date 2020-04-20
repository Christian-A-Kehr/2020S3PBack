*This project is meant as start code for projects and exercises given in Flow-1+2 at http://cphbusiness.dk in the Study Program "AP degree in Computer Science"*

*Projects which are expected to use this start-code are projects that require all, or most of the following technologies:*
 - *JPA and REST*
- *Testing, including database test*
- *Testing, including tests of REST-API's*
- *CI and CONTINUOUS DELIVERY*

### Preconditions
*In order to use this code, you should have a local developer setup + a "matching" droplet on Digital Ocean as described in the 3. semester guidelines* 
# Getting Started

## Initial renames:

- Clone the project from GitHub (remember to remove the .git folder)
- Create appropriate databases on local vagrant as well as on droplet server
- Rename the folder manually
- Open project in NetBeans and rename the project and artifactId (right click -> rename)

- Change database name in the (Files tab or use explorer) 'travis.yml' file (line 43)
- Change target server in 'Project Files/pom.xml' (line 18) Remember to use https
- Change project name in 'Web Pages/META-INF/context.xml' (line 2)
- Change database name in 'Source Packages/rest/RenameMeResource.java' (line 19)
- Change database name in 'Test Packages/facades/FacadeExampleTest.java' (line 32)
- Change database name in 'Other Sources/src/main/resources/default package/config.properties' (line 17 + line 21)


## Populating the databases with Users:

- To populate droplet database with dummy users, tempoarily change the data in 'Other Sources/src/main/resources/default package/config.properties'
to point at the droplet (line 11-14)

db.server=DOMAIN_NAME (ex. cakehr.dk/e-scape.dk)

db.port=3306

db.user=DB_USER

db.password=DB_PASSWORD

- Then run the setup file 'Source Packages/utils/SetupTestUsers.java' then change back to localhost

db.server=localhost

db.port=3307

db.user=dev

db.password=ax2

- Optionally: run the file again to populate the local vagrant database with users too


## Deploying:

- Create a new GitHub repository and publish the project
- Setup Travis to ensure clean deployment (remember to set env variables: 'REMOTE_USER' : 'script_user' and 'REMOTE_PW' : 'PW_FOR_script_user')
- ssh into your droplet and open this file with nano: 'sudo nano /opt/tomcat/bin/setenv.sh'

export DEPLOYED="DEV_ON_DIGITAL_OCEAN"

export USER="DB_USER"

export PW="DB_PASSWORD"

export CONNECTION_STR="jdbc:mysql://localhost:3306/DATABASE_NAME"

- And lastly use 'mvn clean test -Dremote.user=script_user -Dremote.password=PW_FOR_script_user tomcat7:deploy' to deploy
- Now clone the frontEnd application and follow the instructions to set this up "https://github.com/Christian-A-Kehr/2020CA3FrontEnd"


## Notes:

- For now all test classes and login classes are dependant on the RenameMe entities
- All current DTO classes as well as the JokeResource.java class are just examples meant to be used as a template for remote api fetching.
- DemoResource.java is used to confirm the existence of users in the database.
- The RenameMeResource.java, RenameMe.java and FacadeExample.java (as well as tests for these) are templates that can be changed or copied.
- Role.java and User.java are currently sufficient for a basic user/admin system. Will need modification only if required.
- The errorhandling folder and security folder doesn't require editing for new projects.


# After project completion:

- Remove 'Source Packages/security/SharedSecret.java' (line 22-24)
- Remove 'Source Packages/utils/setupTestUsers.java' or add to the .gitignore file

For more info: https://github.com/dat3startcode/rest-jpa-devops-startcode/blob/master/README_proof_of_concept.md