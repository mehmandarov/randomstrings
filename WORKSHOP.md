# Workshop: Java on Google Cloud: The Enterprise, The Serverless, and The Native

This workshop will guide you through deploying a sample Java application, `randomstrings`, to Google Cloud Platform (GCP) using Cloud Build.

### Target Audience: Developers familiar with Java and basic cloud concepts.

## Workshop Outline:

### 0. Intro - theory (30 min)

### 1. Setting Up The Local Environment (15 mins)

#### a. Clone the Repository:
Start by cloning the `randomstrings` repository from Github: https://github.com/mehmandarov/randomstrings.

#### b. Code Review:
Briefly walk through the Java code.

### 2. Building and Running Locally (15 mins)

#### a. Local Build:
Build the application locally using Maven. 

If you are using Cloud Shell, Java and Maven will be installed for you. 
You will still need to switch Java version from 17 to 21. See _"Tips and Tricks"_ section below for a how-to. 

#### b. Running the Application:
Run the application locally to verify its functionality.

Follow the instructions in the `README` file [here][1], _section "Local Build and Run"_.

#### c. Building with Containers (Optional):
\[For those who prefer containers\] 
We'll show you how to build and run the application within a Docker container.

### 4. Deployment to GCP with Cloud Build (20 mins)

#### a. Cloud Build Configuration:
We'll walk you through the Cloud Build configuration file (`cloudbuild.yaml`) and explain its components. This includes environment variables and build steps.

#### b. Deploying to GCP:
We'll guide you through deploying the application to GCP using Cloud Build. This will involve triggering a build and monitoring the logs.

Follow the instructions in the `README` file [here][2], _section "Building Container Images and Cloud Deployment"_.

#### c. Example Substitutions:
We'll showcase examples of using substitutions for variables like environment (dev, test, prod) or specific configurations.
Examples of how to work with variables and substitutions in `cloudbuild.yaml`.

### 5.  Deploying Multiple Versions and Testing (50 mins)

#### a. Versioning Your Application:
Discussion point: Strategies for versioning your application code.

#### b. Deploying Different Versions:
Demonstrate how to deploy different versions of the application using Cloud Build triggers and substitutions.

Have a look at:
- how to work with variables and substitutions in `cloudbuild.yaml` _(again!)_
- how to build with multiple Dockerfiles and multistage builds: `src/main/docker`

#### c. Measuring response time, setting up the liveness probes, etc.

Measure the response times in the command line:

```bash
# Base URL is either localhost:<port>, or Cloud Run URL for your deployment:
time curl <BASE_URL>/api/rnd
```

---
## Tips & Tricks

Check Java version in Cloud Console:

```bash
java --version
```

Change Java version in Cloud Console:

```bash
sudo update-java-alternatives -s java-1.21.0-openjdk-amd64 && export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64
```


[1]: https://github.com/mehmandarov/randomstrings?tab=readme-ov-file#local-build-and-run
[2]: https://github.com/mehmandarov/randomstrings?tab=readme-ov-file#building-container-images-and-cloud-deployment-cloud-build-and-cloud-run
