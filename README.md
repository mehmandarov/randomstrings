# RandomStrings – Using Jakarta EE + MicroProfile

## Development: Building and Running the application locally

| Runtime      | Build                                      | Run                                                | Address                       |
|--------------|--------------------------------------------|----------------------------------------------------|-------------------------------|
| Open Liberty | ```mvn -f pom-liberty.xml clean package``` | ```java -jar target/randomstrings.jar```           | http://localhost:9080/api/rnd |
| Quarkus      | ```mvn -f pom-quarkus.xml clean package``` | ```java -jar target/quarkus-app/quarkus-run.jar``` | http://localhost:8080/api/rnd |
| Helidon      | ```mvn -f pom-helidon.xml clean package``` | ```java -jar target/randomstrings.jar```           | http://localhost:8080/api/rnd |

Note: You can run your application in `dev mode` that enables live code reloading:

```shell script
./mvnw -f pom-quarkus.xml quarkus:dev
```

## Building Container Images on GCP and Deploy to Cloud Run

### Create Artifact Registry Repository
https://cloud.google.com/artifact-registry/docs/repositories/create-repos#docker

### Build Images
```shell script
gcloud builds submit --substitutions=_APP_RUNTIME="quarkus",_APP_RUNTIME_FLAVOUR="jvm"

gcloud builds submit --substitutions=_APP_RUNTIME="helidon",_APP_RUNTIME_FLAVOUR="jvm"

gcloud builds submit --substitutions=_APP_RUNTIME="liberty",_APP_RUNTIME_FLAVOUR="jvm"
```

## Application Set-up and Links
### Port Configuration
Cloud Run uses port 8080 by default. All runtimes are configured to expose that port. These configurations are done in: 

Quarkus: defaults to 8080
OpenLiberty: set-up in [server.xml](src/main/liberty/config/server.xml)
Helidon: set-up in [microprofile-config.properties](src/main/resources/META-INF/microprofile-config.properties)

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

## Deploying the Application

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