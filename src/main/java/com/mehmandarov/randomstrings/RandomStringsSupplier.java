package com.mehmandarov.randomstrings;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.crac.Context;
import org.crac.Core;
import org.crac.Resource;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;


@ApplicationScoped
public class RandomStringsSupplier implements Resource {

    RandomStringsSupplier(){
        Core.getGlobalContext().register(RandomStringsSupplier.this);
    }

    @Inject
    @ConfigProperty(name = "adjectivesFileName", defaultValue = "/adjectives.txt")
    String adjectivesFileName;

    @Inject
    @ConfigProperty(name = "nounsFileName", defaultValue = "/nouns.txt")
    String nounsFileName;

    @Inject
    @ConfigProperty(name ="readFromFile", defaultValue = "true")
    Boolean readFromFile;

    public String[] generateRandomStringsPair() {
        List<String> adjectives = readFromFile ? readFile(adjectivesFileName) : readFromEnum(Adjectives.values());
        List<String> nouns = readFromFile ? readFile(nounsFileName) : readFromEnum(Nouns.values());

        return  new String[] {getRandomElement(adjectives), getRandomElement(nouns)};
    }

    private List<String> readFromEnum(Enum<?>[] values) {
        return Arrays.stream(values).map(Enum::name).map(str -> str.replaceAll("_", "-")).collect(Collectors.toList());
    }

    private List<String> readFile(String fileName) {
        List<String> result = new ArrayList<>();

        try {
            InputStream is = getClass().getResourceAsStream(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            String line;
            while ((line = reader.readLine()) != null) {
                result.add(line);
            }

        } catch (IOException e) {
            result.add(e.getMessage());
        }
        return result;
    }

    private String getRandomElement(List<String> list)
    {
        Random rand = new Random();
        return list.get(rand.nextInt(list.size()));
    }

    @Override
    public void beforeCheckpoint(Context<? extends Resource> context) throws Exception {
        System.out.println("*** Random Strings Supplier before checkpoint: Closing File Handles");
    }

    @Override
    public void afterRestore(Context<? extends Resource> context) throws Exception {
        System.out.println("*** Random Strings Supplier after restore: Re-creating File Handles");
    }

    // Forces eager initialization on application startup.
    // Needed for the constructor to be run at startup, which is needed for initializing CRaC related things in this file.
    public void onStartup(@Observes Object event) {}

}
