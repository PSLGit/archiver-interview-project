# File archiver project
## Overview of the current data processing engine

We have a service mesh of many microservices responsible for data exchange between third party systems and us. An existing data consumer application routinely receives JSON data from various external sources. As data is received, a sequential counter is added to the JSON data. For example, consider the following incoming data work load:

```
{
    "key" : "value"
}
```

The existing data processor tags the data by adding a unique sequential id:
```
{
    "id" : 123123,
    "data" : {
        "key" : "value"
    }
}
```

This helps keep track of the order in which the data is received. Once tagged, the data is saved into a file and placed on a shared mount on the file system that is accessible by other applications. The files placed onto the file system will then be picked up by downstream data processors. Currently, there is only one downstream data processor which simply takes the file from the file system, processes the data and deletes the file after completion.

## New file monitor application (File archiver)

For better monitoring of files being processed, we have built a small file monitor application (the code is in this repository) that looks at the files on the mounting location and stores the file name in the database. We plan to use this to create reports to give us information about files being processed.

To ensure downstream systems can use this information, we will be adding a new attribute in the JSON data to indicate that this file has been archived.
```
{
    "id" : 123123,
    "data" : {
        "key" : "value"
    },
    "archived" : "date-time-value"
}
```

Future data processors may use that information to decide on whether a file should be processed.

## Docker Compose

We have added a Docker Compose configuration with MySQL and phpMyAdmin to make it easier to run the Spring Boot application locally.