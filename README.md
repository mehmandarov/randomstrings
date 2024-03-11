# MicroProfile – RandomStrings

## Introduction

### Run with Open Liberty
This is a demo application built with MicroProfile and currently running setup to run on OpenLiberty. It will return a combination of a random adjective and a noun.

The generation of the executable jar file can be performed by issuing the following command:

    mvn clean package

This will create an executable jar file `randomstrings.jar within the _target_ maven folder. This can be started by executing the following command:

    java -jar target/randomstrings.jar

To launch the test page, open your browser at the following URL

    http://localhost:9080/index.html

See the [website][1] for more information on MicroProfile.

### Run with Quarkus

#### Running Quarkus in Dev Mode
You can run your application in dev mode that enables live coding using:
```shell script
./mvnw -f pom_quarkus.xml quarkus:dev
```

#### Running Quarkus
Build:
```shell script
mvn -f pom_quarkus.xml clean package
```

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.


### Config
Configuration of your application parameters ([specification][2]).

The example class `RandomStringsSupplier` shows you how to configure and define default values for variables.

### Health
The health status can be used to determine if the 'computing node' needs to be discarded/restarted or not ([specification][3]).

The class `ServiceHealthCheck` contains an example of a custom check which can be integrated to health status checks of the instance.  The index page contains a link to the status data.

### Metrics
The Metrics exports _Telemetric_ data in a uniform way of system and custom resources ([specification][4]).

The class `RandomStringsController` contains an example how you can measure the execution time of a request. The index page also contains a link to the metric page (with all metric info).


### Open API

Exposes the information about your endpoints in the format of the OpenAPI v3 specification ([docs][5]).

The index page contains a link to the OpenAPI information of the available endpoints for this project.


### Deploy to Google Cloud Run
Make sure to have a file named _exactly_ Dockerfile in the root folder of the project. 
This is the one Google Cloud Build will be using. 

```shell script
gcloud builds submit --tag gcr.io/{your project name}/randomstrings
gcloud run deploy --image gcr.io/{your project name}/randomstrings --platform managed
```


### Quarkus Native
Build with `mvn verify -f pom_quarkus.xml -Pnative -Dquarkus.native.container-build=true`

You need to have GraalVM installed to build locally.

```shell script
sudo xattr -r -d com.apple.quarantine /Library/Java/JavaVirtualMachines/<graalvm-version>
```

Ensure that your path or java_home includes GraalVM, as specified in the readme.

## Authors
- [Rustam Mehmandarov][6]
- [Mads Opheim][7]


[1]: https://microprofile.io/
[2]: https://microprofile.io/project/eclipse/microprofile-config
[3]: https://microprofile.io/project/eclipse/microprofile-health
[4]: https://microprofile.io/project/eclipse/microprofile-metrics
[5]: https://microprofile.io/project/eclipse/microprofile-open-api
[6]: https://github.com/mehmandarov
[7]: https://github.com/madsop