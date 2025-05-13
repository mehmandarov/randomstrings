# 0. some housekeeping
export PROJECT_ID=$(gcloud config list --format 'value(core.project)')
echo   $PROJECT_ID

# 1. build the image
source checkpoint.sh

# 2. tag the image
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

