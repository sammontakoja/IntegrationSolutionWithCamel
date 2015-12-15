# Module integrationtest

Part of cexml project.

This module is about verifying project objective.

Project objective is divided into 8 steps.

1. Receive HTTP POST request in address:port/camel/food
2. Read XML message from HTTP POST request
3. Transform XML message to Java object
4. Put some data from Java object to database
5. Transform Java object to two XML messages
6. Write first XML message to JMS queue
7. Put second XML message to HTTP response (who made the POST)
8. Read first XML message from JMS queue and write XML content to log file

So far vefified: