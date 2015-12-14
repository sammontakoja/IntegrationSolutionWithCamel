# cexml

Camel eating XML

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

# Data

**Input**

Step 1: HTTP POST payload xml must be valid against camelfood.xsd.

Like CamelFoodExample.xml
```
<camelfood>
   <name>dry grain</name>
   <amount>82723023</amount>
</camelfood>
```

**Output**

HTTP response payload xml must be valid against processed.xsd

Step 7 happy case, like ResponceOkExample.xml
```
<responce>
  <processed>true</processed>
  <time>2007-10-26T09:36:28</time>
  <message>OK</message>
</responce>
```

Steps 1-7 fail case, like ResponceFailExample.xml
```
<responce>
  <processed>false</processed>
  <time>2007-10-26T09:36:28</time>
  <message>Something bad happened when doing this and that</message>
</responce>
```

# Stack

__Language__
Java 7 (yeah I know, it's 2015 but client wants this one)

__Integration and DI frameworks__
Apache Camel + Spring boot
(A recent tobacco industry survey showed that 90% of all men still prefer women to camels but heck we know better)

__Build tool__
Apache Maven
