package com.mehmandarov.randomstrings;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;

@ApplicationScoped
public class RandomStringsSupplier {

    @Inject
    @ConfigProperty(name = "adjectivesFileName", defaultValue = "/adjectives.txt")
    String adjectivesFileName;

    @Inject
    @ConfigProperty(name = "nounsFileName", defaultValue = "/nouns.txt")
    String nounsFileName;


    public String[] generateRandomStringsPair() {
        ArrayList<String> adjectives = readFile(adjectivesFileName);
        ArrayList<String> nouns = readFile(nounsFileName);

        return  new String[] {getRandomElement(adjectives), getRandomElement(nouns)};
    }


    private ArrayList<String> readFile(String fileName) {
        ArrayList<String> result = new ArrayList<>();

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

    private String getRandomElement(ArrayList<String> list)
    {
        Random rand = new Random();
        return list.get(rand.nextInt(list.size()));
    }
}
