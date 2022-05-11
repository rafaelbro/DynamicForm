# Objective
Implement a dynamic formulary submittion service that should be able to:
- Receive a JSON containing the formulary fields, and their format. This will enforce the format of submissions.
- Receive submissions to the formulary structure previously created.
- Query for submissions of specific formulary for dates superior to 3 years.

# Requirements
- Java 17
- Maven 3.8.5

# How to run
The server will start at port 8080, on localhost.
```sh
mvn clean install
mvn spring-boot:run
```

# Architecture
The program is divided in two main ways. The form creation and form submission process. On a API call for the creation of a form the `DynamicFormCreationController.java` class is called that simply calls the `DynamicFormCreationService.java` method **formularyCreator**.

This method maps the forms to a integer that will be the ID refereced to as the formulary Id and returns this ID if the creation is successful. The current supported datatypes for the forms are: **integer**, **boolean**, **location**, **date**, **string**. The following payload is an example of a formulary containing all the data types being posted to the **/api/forms/definitions** endpoint:
```json
{
    "amount": "integer",
    "date": "date",
    "residency": "location",
    "name": "string",
    "isActive": "boolean"
}
```
Requests made that are not in the described format will be returned with a HTTP reponse of 400 and a description of the error.

The formulary submittion process is similar to the formulary creation. It calls the `DynamicFormSubmissionController.java` on a API call that simply redirects the payload to the  `DynamicFormSubmissionService.java` method **submitFormulary**.
This class contains a three level map in which the first level is the formulary Id, the second level is the submission Id and the third is the values of the submitted formulary. This means that, for example, formulary Id 0 can have a submission Id 0 and formulary Id 1 can also have a submission Id 0, however since this is mapped we can retrieve exactly the necessary formulary when needed. 
After processing the submission, if valid, the server saves this map state to a file a returns a payload containing the form id and submission id to the user. The following payload is an example of a request to the submission endpoint **/api/forms/0/entries** (where 0 in the URL is the form_id of the submission).
Using as reference the previous JSON we would have:
```json
{
    "amount": "10000",
    "date": "29/05/2012",
    "residency": "(200,300)",
    "name": "A random company LTDA",
    "isActive": "T"
}
```
A few restrictions regarding the fields are:
1. Dates cannot be superior to current date (it wouldn't make sense to have a date in the future)
2. Integers are limited by the range of -2147483648 to 2147483647
3. Locations are a tuple of two integers, which means they share the same domain, and have to necessarily have the format "(integer, integer)"
4. Booleans have a domain of (T, True) for true and (F, False) for false

We have a service that manages the data between sessions by using files. The program has a component called `StartUpLoader.java` that listens to an event triggered by the Spring framework indicating that the application is ready. When that happens we load to memory the values of the Maps for the Form Creation and Form Submission. It does that by calling `DynamicFormFileService.java` that is the service responsible for reading and writing to and from the files that maintain the Forms and Submissions. Currently those files are maintained in the root directory with the names **FormFormat.txt** and *Submissions.txt**.

For the queries related to the submissions we have the **/api/forms/{form_id}/entries** endpoint. Calling a get to this endpoint triggers the `DynamicFormSubmissionController.java` that calls the `DynamicFormSubmissionService.java` method **queryFoundationDate**. Currently, since we do not receive any arguments, we query for a predefined field in submissions called **date**, so if the formulary does not have a date field it will not be able to query, returning an error.


# Improvements
Here we discuss the improvements that could be made to the application:
1. The file saving process rewrites the whole file everytime, which is computationally intensive. Ideally we would have a Database to manage that.
2. Querying is all done in memory, for a large set of data that would become exponetially intensive, a DB layer to take care of those queries would be ideal
3. Implement a multilevel JSON structure, currently the application only supports forms with one JSON level.
4. Implement dynamic queries would be ideal to reduce manual development of queries. Ideally add arguments to this call to allow more customization of searches.
5. Reducing data in-memory usage.

# Discussion
Here some trade-offs of the application and definitions:
1. Should we standardize the input data? For consistency the received data is currently saved as is. But for processing reasons it might be better to standardized received data.
2. Should data be case sensitive? Currently values are being treated as case insensitive (ex: we could receive a T or t for true in boolean) but JSON keys are not. Should they all be case insensitive or sensitive?