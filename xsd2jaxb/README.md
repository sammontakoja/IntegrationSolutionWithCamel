# Module xsd2jaxb

Part of cexml project.

All input and output messages in cexml project are XML documents constructed with Schema.
This module contains schema files and transform them to jaxb classes.

# Schemas

HTTP interface "address:port/camel/food"

__Input__ 

camelfood.xsd

Example input
```
<camelfood>
   <name>dry grain</name>
   <amount>82723023</amount>
</camelfood>
```

__Output__

processed.xsd

Example output when input processed
```
<responce>
  <processed>true</processed>
  <time>2007-10-26T09:36:28</time>
  <message>OK</message>
</responce>
```

Example output when input processing failed
```
<responce>
  <processed>false</processed>
  <time>2007-10-26T09:36:28</time>
  <message>Something bad happened when doing this and that</message>
</responce>
```