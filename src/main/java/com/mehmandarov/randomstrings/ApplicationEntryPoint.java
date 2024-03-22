package com.mehmandarov.randomstrings;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

/**
 *
 */
@ApplicationPath("/api")
@ApplicationScoped
public class ApplicationEntryPoint extends Application {
}
