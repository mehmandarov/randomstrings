# Workshop: Java on Google Cloud: The Enterprise, The Serverless, and The Native

This workshop will guide you through deploying a sample Java application, `randomstrings`, to Google Cloud Platform (GCP) using Cloud Build.

### Target Audience: Developers familiar with Java and basic cloud concepts.

## Workshop Outline:

0. Intro - theory (30 min)

1. Setting Up The Local Environment (15 mins)

a. Clone the Repository:
Start by cloning the `randomstrings` repository from Github.
b. Code Review:
Briefly walk through the Java code.

2. Building and Running Locally (15 mins)

a. Local Build:
Build the application locally using Maven.

b. Running the Application:
Run the application locally to verify its functionality.

c. Building with Containers (Optional):
(For those who prefer containers) We'll show you how to build and run the application within a Docker container.
 
4. Deployment to GCP with Cloud Build (10 mins)

a. Cloud Build Configuration:
We'll walk you through the Cloud Build configuration file (cloudbuild.yaml) and explain its components. This includes environment variables and build steps.

b. Deploying to GCP:
We'll guide you through deploying the application to GCP using Cloud Build. This will involve triggering a build and monitoring the logs.

c. Example Substitutions:
We'll showcase examples of using substitutions for variables like environment (dev, test, prod) or specific configurations.

5.  Deploying Multiple Versions and Testing (50 mins)

a. Versioning Your Application:
Strategies for versioning your application code.

b. Deploying Different Versions:
Demonstrate how to deploy different versions of the application using Cloud Build triggers and substitutions.

c. Measuring response time, setting up the liveness probes, etc.