package com.mehmandarov.randomstrings.health;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Destroyed;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import org.crac.Context;
import org.crac.Core;
import org.crac.Resource;

@ApplicationScoped
public class StartupListener implements Resource {

    StartupListener(){
        Core.getGlobalContext().register(StartupListener.this);
    }

    public void init(@Observes
                     @Initialized(ApplicationScoped.class) Object init) {
        System.out.println("*** ****** ***StartupListener initialized");
        String timestamp = System.getenv("START_COMMAND_TIMESTAMP_MS");
        if (timestamp != null) {
            long started = Long.parseLong(timestamp);
            long elapsed = System.currentTimeMillis() - started;
            System.out.println("<======> STARTUP: Elapsed time: " + elapsed);
        }
    }

    public void destroy(@Observes
                        @Destroyed(ApplicationScoped.class) Object init) {
        System.out.println("*** ****** ***StartupListener destroyed");
    }

    @Override
    public void beforeCheckpoint(Context<? extends Resource> context) throws Exception {
        System.out.println("*** ****** ***StartupListener beforeCheckpoint");
    }

    @Override
    public void afterRestore(Context<? extends Resource> context) throws Exception {
        System.out.println("*** ******* ***StartupListener afterRestore");

        String timestamp = System.getenv("START_COMMAND_TIMESTAMP_MS");
        if (timestamp != null) {
            long started = Long.parseLong(timestamp);
            long elapsed = System.currentTimeMillis() - started;
            System.out.println("<======> RESTORE: Elapsed time: " + elapsed);
        }
    }
}