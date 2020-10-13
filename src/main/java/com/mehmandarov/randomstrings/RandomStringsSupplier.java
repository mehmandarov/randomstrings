package com.mehmandarov.randomstrings;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@ApplicationScoped
public class RandomStringsSupplier {

    @Inject
    @ConfigProperty(name = "adjectivesFileName", defaultValue = "/adjectives.txt")
    String adjectivesFileName;

    @Inject
    @ConfigProperty(name = "nounsFileName", defaultValue = "/nouns.txt")
    String nounsFileName;

    @Inject
    @ConfigProperty(name ="readFromFile", defaultValue = "true")
    boolean readFromFile;

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

        } catch (FileNotFoundException e) {
            result.add(e.getMessage());
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
}
