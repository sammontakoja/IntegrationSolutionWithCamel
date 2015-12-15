# cexml

This project is about testing how Camel and Spring boot can handle messaging based on XSD, REST and JMS-queues.

# Objective

1. Receive HTTP POST request in address:port/camel/food
2. Read XML message from HTTP POST request
3. Transform XML message to Java object
4. Put some data from Java object to database
5. Transform Java object to two XML messages
6. Write first XML message to JMS queue
7. Put second XML message to HTTP response (who made the POST)
8. Read first XML message from JMS queue and write XML content to log file

Steps 1-7 are must be done inside one transaction.
When writing to JMS queue in step 7 fails then database action in step 5 is rolled back.

From the HTTP request point of view steps 1-7 are done synchronous and step 8 must be done asynchronously.

If something bad happens between steps 1 and 7 then http 200 is returned with xml payload saying input xml is not processed.

When steps 1-7 are done without errors then http 200 is returned with xml payload saying input xml is processed.

# Stack

__Language__
Java 7 (yeah I know, it's 2015 but client wants this one)

__Integration and DI frameworks__
Apache Camel + Spring boot
(A recent tobacco industry survey showed that 90% of all men still prefer women to camels but heck we know better)

__Build tool__
Apache Maven

# Implementation

├── camel
│   ├── pom.xml
│   ├── README.md
│   └── src
│       └── main
│           ├── java
│           │   └── tk
│           │       └── sammontakoja
│           │           └── cexml
│           │               ├── Application.java
│           │               └── RouterInitializer.java
│           └── resources
│               ├── application.properties
│               └── banner.txt
├── pom.xml
├── README.md
└── xsd2jaxb
    ├── messages
    │   ├── camelfood.xsd
    │   ├── processed.xsd
    ├── pom.xml
    └── README.md
10 directories, 15 files

Module __xsd2jaxb__ hold XSD files and convert them to JAXB classes.
Module __camel__ build war-packet which contains all applicaton logic.
Module __integrationtest__ verify project objective if fullfilled.