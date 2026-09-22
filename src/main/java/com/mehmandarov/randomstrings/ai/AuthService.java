package com.mehmandarov.randomstrings.ai;

import com.google.auth.oauth2.GoogleCredentials;
import jakarta.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.util.Collections;

@ApplicationScoped
public class AuthService {

    private static final String CLOUD_PLATFORM_SCOPE = "https://www.googleapis.com/auth/cloud-platform";
    private GoogleCredentials credentials;

    public String getAccessToken() throws IOException {
        if (credentials == null) {
            credentials = GoogleCredentials.getApplicationDefault()
                    .createScoped(Collections.singletonList(CLOUD_PLATFORM_SCOPE));
        }
        credentials.refreshIfExpired();
        return "Bearer " + credentials.getAccessToken().getTokenValue();
    }
}
