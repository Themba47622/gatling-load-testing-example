# Load & Performance Testing | Gatling

## Description
Gatling load & performance testing example

## Installation
```
mvn install
```

**Usage**\
Option 1: Execute tests using the *Engine* or *MyGatlingRunner* class

Option 2: Execute tests using the command line
```
mvn gatling:test -Dgatling.simulationClass=templates.GatlingTemplate -DUSERS=10 -DRAMP_DURATION=5 -DDURATION=30
```

**Reporting**
An HTML report for the test execution can be found in the below folder location:
- ../target/gatling/example...-.../index.html

## Test API 
```
https://test-api.k6.io
```
