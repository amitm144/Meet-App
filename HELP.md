## Getting Started
### Instructions:
add and or change ACTIVE_PROFILE environment variable to choose the correct properties.
the various property files will determine the application behaviour:
1. production - HelperInitializer will be DISABLED and JPA will connect to the PRODUCTION db
    changes you will make WILL NOT be deleted from the database when you shut down the server.
2. staging - HelperInitializer will be ENABLED and JPA will connect to the STAGING db
    this setting is appropriate for testing in similar environment to production
    changes you will make WILL be deleted from the database when you shut down the server.
3. test - HelperInitializer will be DISABLED and JPA will connect to H2,
    this setting is appropriate for unit testing
    or if you wish to play with the app locally with no effect on any database
BY DEFAULT THE APPLICATION WILL START WITH STAGING PROPERTIES APPLIED

Similarly, environment variables are needed to connect to the DB, you may find their configurations, and additional information,
in our [project folder](https://drive.google.com/drive/folders/10RyL90qONrEUS2BbO4fNHSUQx6-8Dg6B?usp=share_link).

### Gradle Documentation
* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.7.6/gradle-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.7.6/gradle-plugin/reference/html/#build-image)


### Additional Documentation
[Class notebook](https://afekacollege.sharepoint.com/:o:/r/sites/Integrative/SiteAssets/Integrative%20SW%20Engineering%20Notebook?d=wb89662193ef146c99db9c202ab01e3bd&csf=1&web=1&e=jwZq1u)

