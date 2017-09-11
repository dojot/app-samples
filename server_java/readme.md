# server-java

>Java server project for IoT with CPqD ​IoT Plataform

## Setup

Make sure to configure the application.properties that contains the info about the platform.
```propeties
...

platform.url=https://your-platform-url

fiware-service=platform-user
fiware-servicepath=/

security.username=platform-user
security.passwd=platform-password

```

``` bash
# install dependencies and run serve at localhost:3001
./gradlew bootRun

# build project
./gradlew assemble

```
References:
* Server created with [Spring Boot](http://projects.spring.io/spring-boot/)
* Websocket created with [Netty-Socketio](https://github.com/mrniko/netty-socketio)