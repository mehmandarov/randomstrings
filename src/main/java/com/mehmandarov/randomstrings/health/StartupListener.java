package com.mehmandarov.randomstrings.health;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Destroyed;
import jakarta.enterprise.event.Startup;
import jakarta.enterprise.event.Observes;
import org.crac.Context;
import org.crac.Core;
import org.crac.Resource;
import java.time.Instant;

@ApplicationScoped
public class StartupListener implements Resource {

    StartupListener(){
        Core.getGlobalContext().register(StartupListener.this);
    }

    private long fibonacciNumber = 0;

    public void init(@Observes
                     Startup event) {
        System.out.println("*** ****** ***StartupListener initialized");
        String timestamp = System.getenv("START_COMMAND_TIMESTAMP_MS");
        if (timestamp != null) {
            long started = Long.parseLong(timestamp);
            double elapsed = Instant.now().toEpochMilli() - started;

            fibonacciNumber = fibonacci(42);
            System.out.println("<======> STARTUP: Elapsed time (s): " + elapsed/1_000);

            System.out.println("<======> STARTUP: Fibonacci number: " + fibonacciNumber);
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
            double elapsed = Instant.now().toEpochMilli() - started;
            System.out.println("<======> RESTORE: Elapsed time (s): " + elapsed/1_000);

            System.out.println("<======> RESTORE: Fibonacci number: " + fibonacciNumber);
        }
    }

    private static long fibonacci(int n) {
        if (n <= 1) {
            return n;
        }
        return fibonacci(n - 1) + fibonacci(n - 2);
    }

}