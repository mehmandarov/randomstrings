# RandomStrings – Using Jakarta EE + MicroProfile
A demo project built with Jakarta EE and MicroProfile – a service that returns random combination of an adjective and a noun as a JSON list.

## Local Build and Run
### Building and Running the Application Locally

The code can be deployed to several runtimes. This is done to illustrate the switching runtimes with minor changes to the code and to observe the performance.

| Runtime          | Build                                      | Run                                                | Address                       |
|------------------|--------------------------------------------|----------------------------------------------------|-------------------------------|
| **Open Liberty** | ```mvn -f pom-liberty.xml clean package``` | ```java -jar target/randomstrings.jar```           | http://localhost:8080/api/rnd |
| **Quarkus**      | ```mvn -f pom-quarkus.xml clean package``` | ```java -jar target/quarkus-app/quarkus-run.jar``` | http://localhost:8080/api/rnd |
| **Helidon**      | ```mvn -f pom-helidon.xml clean package``` | ```java -jar target/randomstrings.jar```           | http://localhost:8080/api/rnd |

**_Note:_** You can run your Quarkus application in `dev mode` that enables live code reloading:

```shell script
./mvnw -f pom-quarkus.xml quarkus:dev
```

### Building and Running the Application Locally in a Container
All containers are multistage build containers and can be found in a [folder structure][10] for each runtime and are marked with `jvm` and `native`, depending on flavour you want to build. The same containers are used to create images for the Cloud deployments as well.

An example of building and running using `docker`:

```shell script
# BUILD container
docker build -t randomstrings-quarkus:regular -f src/main/docker/quarkus/Dockerfile.jvm .

# RUN container
docker run -d --privileged --rm -i --name=randomstrings-quarkus -p 8080:8080 randomstrings-quarkus:regular

# DELETE container
docker rm -f randomstrings-quarkus   
```

## Building Container Images and Cloud Deployment: Cloud Build and Cloud Run

<details>
<summary> Expand for more details. Full info on how to build, deploy, and run the app on Google Cloud. </summary>

### Preparations

#### 1. Activate necessary APIs for your Google Cloud project

APIs to enable: `Cloud Build`, `Artifact Registry`, `Cloud Run`.

```bash
gcloud services enable cloudbuild.googleapis.com
gcloud services enable artifactregistry.googleapis.com
gcloud services enable run.googleapis.com
```

#### 2. Create Artifact Registry Repository

Values that will be used later in the scripts:
* Repository name: `rndstrs`
* Region: `europe-north1`

```bash
gcloud artifacts repositories create rndstrs \
--repository-format=docker \
--location=europe-north1 \
--description="Randomstrings Workshop Artifact Repository" \
--disable-vulnerability-scanning
```
More info https://cloud.google.com/artifact-registry/docs/repositories/create-repos#docker


### Build, Add, Deploy 

Build images using Cloud Build, add to the registry, and deploy to Cloud Run.

| Runtime               | Build & Deploy to Cloud Run                                                                     |
|-----------------------|-------------------------------------------------------------------------------------------------|
| **Quarkus – JVM**     | ```gcloud builds submit --substitutions=_APP_RUNTIME="quarkus",_APP_RUNTIME_FLAVOUR="jvm"```    |
| **Quarkus – Native**  | ```gcloud builds submit --substitutions=_APP_RUNTIME="quarkus",_APP_RUNTIME_FLAVOUR="native"``` |
| **OpenLiberty – JVM** | ```gcloud builds submit --substitutions=_APP_RUNTIME="liberty",_APP_RUNTIME_FLAVOUR="jvm"```    |
| **Helidon – JVM**     | ```gcloud builds submit --substitutions=_APP_RUNTIME="helidon",_APP_RUNTIME_FLAVOUR="jvm"```    |
| **Helidon – Native**  | ```gcloud builds submit --substitutions=_APP_RUNTIME="helidon",_APP_RUNTIME_FLAVOUR="native"``` |

You can see all deployed services using:

```bash
gcloud run services list
```

_**NOTE:** The default setup is to require authentication for the deployed applications.
You can turn off authentication for the applications by adding `--allow-unauthenticated` in
`gcloud run deploy` command in `cloudbild.yaml`, or use this command to modify the permissions
after the deploy:_

```bash
# You might be asked to choose a region when running this command.
# Use the same region as the one you used for the deployment, here it is: europe-north1
gcloud run services add-iam-policy-binding [SERVICE_NAME] \
    --member="allUsers" \
    --role="roles/run.invoker"
```

### Creating a CRaC Enabled Application

This is an example of how application using CRaC can be set-up. See shell scripts in [crac][14] folder for more details.

```bash
# 1. build the image
./src/scripts/crac/checkpoint.sh

# 2. tag the image
export PROJECT_ID=$(gcloud config list --format 'value(core.project)')
echo   $PROJECT_ID

docker tag randomstrings-crac:checkpoint europe-north1-docker.pkg.dev/${PROJECT_ID}/rndstrs/randomstrings-crac

# 3. push the image to GCR
docker push europe-north1-docker.pkg.dev/${PROJECT_ID}/rndstrs/randomstrings-crac

# 4. deploy the image to Cloud Run
gcloud run deploy randomstrings-quarkus-crac  \
--image=europe-north1-docker.pkg.dev/${PROJECT_ID}/rndstrs/randomstrings-crac \
--execution-environment=gen2  \
--allow-unauthenticated \
--region=europe-north1 \
--args="--cap-add CHECKPOINT_RESTORE --cap-add SETPCAP -XX:+UnlockExperimentalVMOptions"
```
</details>

## Application Setup and Links
### Port Configuration
Cloud Run uses port 8080 by default. All runtimes are configured to expose that port. These configurations are done in: 

| Runtime         | Port Config                                  |
|-----------------|----------------------------------------------|
| **Quarkus**     | defaults to 8080                             |
| **OpenLiberty** | setup in [server.xml][8]                     |
| **Helidon**     | setup in [microprofile-config.properties][9] |


### Config
Lets you provide configuration parameters for your application ([specification][2]).

The example class `RandomStringsSupplier` shows you how to configure and define default values for variables.

### Health
Available at: `<URL>/health`.

The health status can be used to determine if the 'computing node' needs to be discarded/restarted or not ([specification][3]). These probes (liveness, readiness, startup) are available at separate URLs and can easily be integrated with the Cloud's health checks as they return [HTTP codes (and human-readable)][11] responses.

The class [ServiceHealthCheck][12] contains an example of a custom check which can be integrated to health status checks of the instance.

The [index page][13] contains a link to the status data.


### Metrics
Available at: `<URL>/metrics`.

The Metrics exports _Telemetric_ data in a uniform way of system and custom resources ([specification][4]).

The class `RandomStringsController` contains an example how you can measure the execution time of a request. 
The [index page][13] also contains a link to the metric page.

### Open API
Available at: `<URL>/openapi`.

Exposes the information about your endpoints in the format of the OpenAPI v3 specification ([docs][5]).

The [index page][13] contains a link to the OpenAPI information of the available endpoints for this project.

### CRaC

This is how an application using CRaC can be set-up. See helper shell scripts in [crac][14] folder 
for more details.

* `checkpoint.sh` – a script for building the container image with a checkpoint for a CRaC enabled application.
* `restore.sh` – a script for starting the application by restoring it from the snapshot
* `start-up-no-crac.sh` – a script for starting the application containers without CRaC, can be used to have a base for the benchmarks.

Run scripts from the root folder of this project, e.g.:

```bash
./src/scripts/crac/checkpoint.sh

./src/scripts/crac/restore.sh

./src/scripts/crac/start-up-no-crac.sh
```


## Contributors
- [Rustam Mehmandarov][6]
- [Mads Opheim][7]


[1]: https://microprofile.io/
[2]: https://microprofile.io/project/eclipse/microprofile-config
[3]: https://microprofile.io/project/eclipse/microprofile-health
[4]: https://microprofile.io/project/eclipse/microprofile-metrics
[5]: https://microprofile.io/project/eclipse/microprofile-open-api
[6]: https://github.com/mehmandarov
[7]: https://github.com/madsop
[8]: src/main/liberty/config/server.xml
[9]: src/main/resources/META-INF/microprofile-config.properties
[10]: src/main/docker
[11]: https://github.com/eclipse/microprofile-health/blob/main/spec/src/main/asciidoc/protocol-wireformat.asciidoc#appendix-a-rest-interfaces-specifications
[12]: src/main/java/com/mehmandarov/randomstrings/health/ServiceHealthCheck.java
[13]: src/main/resources/META-INF/resources/index.html
[14]: src/scripts/crac