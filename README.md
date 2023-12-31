Method Activities for Spring Boot
========
[![Apache License 2](https://img.shields.io/badge/license-ASF2-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0.txt) 

[//]: # ([![Build Status]&#40;https://github.com/lukas-krecan/ShedLock/workflows/CI/badge.svg&#41;]&#40;https://github.com/lukas-krecan/ShedLock/actions&#41; )

[//]: # ([![Maven Central]&#40;https://maven-badges.herokuapp.com/maven-central/net.javacrumbs.shedlock/shedlock-parent/badge.svg&#41;]&#40;https://maven-badges.herokuapp.com/maven-central/net.javacrumbs.shedlock/shedlock-parent&#41;)

Method Activity is a library that enables you to generate activity data with your methods. It is based on Spring AOP and Spring Expression Language (SpEL). It integrates with Spring Boot and Spring Framework.
It is inspired by [Spring Method Security](https://docs.spring.io/spring-security/reference/servlet/authorization/method-security.html) and [Spring Cache](https://docs.spring.io/spring-framework/docs/current/reference/html/integration.html#cache).

+ [Versions](#versions)
+ [Components](#components)
+ [Usage](#usage)
+ [Release notes](#release-notes)

## Versions
You need to be using JDK >= 17 and up-to-date Spring boot parent dependency.

## Components
Method activity consists of three parts
* Core - The aspect oriented activity mechanism with spring expressions using Annotations
* Auto Configuration - Spring autoconfiguration to integration with your application, using Spring AOP.
* Activity Listener - Listener to listen to activity events - can be customized to your needs.

## Usage
To use method activities, you do the following
1) Enable and configure activities
2) Annotate your methods
3) Optionally, configure a custom activity listener


### Enable and configure Method Activity (Spring Boot)
First of all, we have to import the project

```xml
<dependency>
    <groupId>io.github.abdullaunais</groupId>
    <artifactId>method-activity-spring-boot-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

Now we just need the autoconfiguration annotation, in order to enable method activity. Use `@EnableMethodActivity` annotation

```java
@EnableMethodActivity
@SpringBootApplication
class MySpringApplication {
    ...
}
```

### Annotate your service methods

#### Template with Plain SpEL Expression Approach
```java
@PreActivity("Creating user with name #user.name")
@PostActivity("Created user with name #user.name. Took #executionTime ms to complete")
@ErrorActivity("Failed to create user with name #user.name with error: #exception.message")
public String createUser(User user) {
    ...
    return result;
}

// [PreActivity] Creating user with name John Doe
// [PostActivity] Created user with name John Doe. Took 100 ms to complete
// [ErrorActivity] Failed to create user with name John Doe with error: User already exists
```
&nbsp;
&nbsp;

#### Template with SpEL Expression in a config file Approach
```java
// UserService.java

@PreActivity("${activity.create-user.pre}")
@PostActivity("${activity.create-user.post}")
@ErrorActivity("${activity.create-user.error}")
public String createUser(User user) {
    ...
    return result;
}
```
```properties
#application.properties
activity.create-user.pre=Creating user with name #user.name
activity.create-user.post=Created user with name #user.name. Took #executionTime ms to complete
activity.create-user.error=Failed to create user with name #user.name with error: #exception.message
```
&nbsp;
&nbsp;
#### Using the arguments passed to the method in the expression
```java
@PreActivity("Searching for query: #query")
public List<Data> searchData(String query) { ... }

@PreActivity("Updating user with id: #id and name: #user.name")
public void updateUser(String id, User user) { ... }

@PreActivity("Updating user with id: #id and name: #user.name")
public void updateUser(String id, @ExpressionAlias("user") User updatedUser) { ... }
// user is the alias for updatedUser


// [PreActivity] Searching for query: spring boot
// [PreActivity] Updating user with id: 123 and name: John Doe
// [PreActivity] Updating user with id: 123 and name: John Doe
```
&nbsp;
&nbsp;
#### Making use of the authentication and return object in the expression
```java
@PreActivity("Getting user #authentication.principal.username")
@PostActivity("Successfully fetched user #authentication.principal.username with name: #returnObject.name")
public User getUser() {
    ...
    return user;
}

// [PreActivity] Getting user john.doe@domain.io
// [PostActivity] Successfully fetched user john.doe@domain with name: John Doe
```

&nbsp;
&nbsp;
#### Including the bean variables and methods in the application context

```java
@PreActivity("I am running java @systemProperties['java.version'] at @systemProperties['java.home']")
public String getSystemInfo() { ... }

@PreActivity("I am @environment.getProperty('spring.application.name')")
public String getAppInfo() { ... }

@PreActivity("Database driver is @dataSource.connection.metaData.driverName")
public String getDbInfo() { ... }

// [PreActivity] I am running java 17.0.6 at C:\Program Files\Java\jdk-17.0.6
// [PreActivity] I am demo-service
// [PreActivity] Database driver is com.mysql.jdbc.Driver
```
&nbsp;
&nbsp;

### Customizing the annotation
You can customize the behavior by overriding the default values of the annotation. The following example shows how to override the default values of the annotation.

#### Configure activity level, paramClass and entity
```java

@PreActivity(value = "Invoked #simpleClassName + '.' + #methodName + '(' + #id + ')'", level = ActivityLevel.TRACE)
public String getData(Integer id) { ... }

// [PreActivity] Invoked DataService.getData(123)



@PostActivity(value = "Successful fund transfer", entity = "transaction", entityId = "#transaction.id", paramClass = TransactionParams.class)
@ErrorActivity(value = "Failed fund transfer with reason: #exception.message", entity = "transaction", entityId = "#transaction.id", paramClass = TransactionParams.class)
public TransactionResponse doFundTransfer(Transaction transaction) { ... }

@Data
public class TransactionParams extends BaseActivityParams {
   @ParamExpression("#transaction.amount")
   private String amount;
   @ParamExpression("#transaction.currency")
   private String currency;
   @ParamExpression("#returnObj?.status")
   private String status;
}

// [PostActivity] Successful fund transfer
// Params: {amount=100, currency=USD, status=SUCCESS}

// [ErrorActivity] Failed fund transfer with reason: Insufficient funds
// Params: {amount=100, currency=USD, status=FAILED}
```

&nbsp;
&nbsp;
### Configure the Listener (Optional)
You can configure the listener to listen to activity events. The default listener logs the activity events to the console. You can configure your own listener by implementing `ActivityEventListener` interface and registering it as a bean.

```java

@Slf4j
@Component
public class MyCustomActivityListener implements ActivityEventListener {
   @Override
   public void onPreActivity(ParsedActivity<?> parsedActivity, ActivityAnnotationData annotationData) {
      log.info("[{}] {}", ActivityType.PreActivity, parsedActivity.getActivity());
      log.info("Params: {}", parsedActivity.getParams());
   }

   @Override
   public void onPostActivity(ParsedActivity<?> parsedActivity, ActivityAnnotationData annotationData) {
      log.info("[{}] {}", ActivityType.PostActivity, parsedActivity.getActivity());
      log.info("Params: {}", parsedActivity.getParams());
   }

   @Override
   public void onErrorActivity(ParsedActivity<?> parsedActivity, ActivityAnnotationData annotationData) {
      log.info("[{}] {}", ActivityType.ErrorActivity, parsedActivity.getActivity());
      log.info("Params: {}", parsedActivity.getParams());
   }

   // parsedActivity - The parsed activity. will contain the activity data and params
   // annotationData - The annotation data of the method. This will contain the activity and the params
}
```


## Kotlin Support
The library is tested with Kotlin and works fine. 


## Caveats
By default, there are some built in variables that are available to be used in the expression. These are:
* `#authentication` - The authentication context
* `#returnObject` - The return object of the method, only in PostActivity
* `#executionTime` - The execution time of the method in milliseconds, only in PostActivity
* `#exception` - The exception thrown by the method, only in ErrorActivity
* `#methodName` - The name of the method annotated
* `#simpleClassName` - The simple name of the class where the method is annotated
* `#fullClassName` - The full name of the class where the method is annotated
* `#packageName` - The package name of the class where the method is annotated

These variable names can be configured under spring configurations if you want to use them for some other purpose.

## Troubleshooting
Help, Method Activity does not do what it's supposed to do!

1. Upgrade to the newest version
2. Check the configuration, check the annotations.
3. Use Method Activity trace log. Method Activity logs interesting information on DEBUG level with logger name `io.github.abdullaunais.methodactivity`.
   It should help you to see what's going on.


# Release notes
## 1.0.0
* Initial Release

