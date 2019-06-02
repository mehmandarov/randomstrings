# MicroProfile – RandomStrings

## Introduction

This is a demo application built with MicroProfile and currently running setup to run on OpenLiberty. It will return a combination of a random adjective and a noun.

The generation of the executable jar file can be performed by issuing the following command

    mvn clean package

This will create an executable jar file **randomstrings.jar** within the _target_ maven folder. This can be started by executing the following command

    java -jar target/randomstrings.jar

To launch the test page, open your browser at the following URL

    http://localhost:8181/randomstrings/index.html

See the [website][1] for more information on MicroProfile.



### Config

Configuration of your application parameters ([specification][2].

The example class **RandomStringsSupplier** shows you how to configure and define default values for variables.



### Health

The health status can be used to determine if the 'computing node' needs to be discarded/restarted or not ([specification][3]).

The class **ServiceHealthCheck** contains an example of a custom check which can be integrated to health status checks of the instance.  The index page contains a link to the status data.



### Metrics

The Metrics exports _Telemetric_ data in a uniform way of system and custom resources ([specification][4]).

The class **RandomStringsController** contains an example how you can measure the execution time of a request. The index page also contains a link to the metric page (with all metric info).




### Open API

Exposes the information about your endpoints in the format of the OpenAPI v3 specification ([docs][5]).

The index page contains a link to the OpenAPI information of the available endpoints for this project.



## Author
* [Rustam Mehmandarov][6]



[1]: https://microprofile.io/
[2]: https://microprofile.io/project/eclipse/microprofile-config
[3]: https://microprofile.io/project/eclipse/microprofile-health
[4]: https://microprofile.io/project/eclipse/microprofile-metrics
[5]: https://microprofile.io/project/eclipse/microprofile-open-api
[6]: https://github.com/mehmandarov
