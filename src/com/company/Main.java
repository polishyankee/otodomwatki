package com.company;

import java.io.*;
import java.lang.reflect.Executable;
import java.net.URL;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) throws Exception {

        ExecutorService executorService = Executors.newFixedThreadPool(30);

        long start = System.currentTimeMillis();

        URL otodom = new URL("https://www.otodom.pl/sprzedaz/mieszkanie/sopot/");
        BufferedReader in = new BufferedReader(
                new InputStreamReader(otodom.openStream()));

        String inputLine;
        StringBuilder stringBuilder = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            stringBuilder.append(inputLine);
            stringBuilder.append(System.lineSeparator());
        }
        in.close();
        String content = stringBuilder.toString();
        Set<String> strings = new TreeSet<>();

        for (int i = 0; i < content.length(); i++) {
            i = content.indexOf("https://www.otodom.pl/oferta/", i);
            if (i < 0)
                break;
            String substring = content.substring(i);
            String link = substring.split(".html")[0];
            strings.add(link);
        }

        for (int i = 0; i < strings.size(); i++) {
            int finalI = i;
            executorService.submit(() -> {
                        try {
                            readWebsite(strings.toArray()[finalI].toString(), finalI + ".html");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
            );
        }

        executorService.shutdown();

        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }

    public static void readWebsite(String link, String fileName) throws Exception {

        URL otodom = new URL(link);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(otodom.openStream()));

        String inputLine;
        StringBuilder stringBuilder = new StringBuilder();
        while ((inputLine = in.readLine()) != null){
            stringBuilder.append(inputLine);
            stringBuilder.append(System.lineSeparator());
        }
        in.close();

        BufferedWriter bw = new BufferedWriter(new FileWriter(fileName,false));
        bw.write(stringBuilder.toString());
        bw.close();
    }




}
