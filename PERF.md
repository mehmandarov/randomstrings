# Google Cloud Run: Same codebase, different runtimes.
## Cold Start Response Times and Image Sizes
This is an example of the response times from command line calls `time curl <cloudrun_url>/api/rnd` to various deployments. Response times did vary slightly, but stayed roughly within that range.
_Disclaimer: YMMV. Take it with a pinch of salt!_

| Runtime              | Built Container Image Size | Google Cloud Run - Cold Start Response Times |
|----------------------|----------------------------|----------------------------------------------|
| **Open Liberty**     | 262.2 MB                   | 20.877s                                      |
| **Quarkus (JVM)**    | 197.2 MB                   | 5.371s                                       |
| **Quarkus (Native)** | 63.7 MB                    | 1.394s                                       |
| **Helidon (JVM)**    | 177.4 MB                   | 6.333s                                       |



