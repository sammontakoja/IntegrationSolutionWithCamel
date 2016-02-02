# cexml

Some (corporate) people tend to buy monolithic Java application servers like WebLogic mainly because they think there's no alternative way. 

I wrote this small example to prove them wrong.

With Spring boot you can easilly combine smaller libraries into one application.

As a result this example application offer same kind services as bigger application servers have:

1) HTTP server

2) JMS Client

3) JMS Broker

4) Transaction management

# Stack
Java 7

Spring boot (for building the app)

Apache Camel (for creating HTTP and JMS routing what several ESB tools offers)

ActiveMQ (for being JMS client and broker)

Atomikos (for transactions management)

# Build
mvn clean install -DskipTests

# Run
java -jar camel/target/camel-0.1-SNAPSHOT.war
