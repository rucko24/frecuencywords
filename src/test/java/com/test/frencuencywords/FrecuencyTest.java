package com.test.frencuencywords;

import com.test.frencuencywords.service.FrecuencyWordsService;
import com.test.frencuencywords.util.Memory;
import lombok.extern.log4j.Log4j2;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Simple test for frecuency words
 */
@Log4j2
@SpringBootTest
public class FrecuencyTest {

    private static final String FORMAT_PRINTF_15 = "%-15s%s%n";
    private static final String WORD = "Word";
    private static final String HOLA = "hola";
    private static final String FRECUENCY = "Frecuency";

    private static final Path PATH_TEXT_FILE = Path.of("src/test/resources/textoSimple.txt");

    @Autowired
    private FrecuencyWordsService frecuencyWordsService;

    @Autowired
    private Memory memory;

    private final String WORDS = "Hola que tal, bienvenidos a BettaTech, Si os está gustando este vídeo, suscribiros y darle a la campanita para ver los nuevos videos que vaya subiendo!";

    @Test
    void testFrecuencyWordsParallel() {
        log.info("Run Frecuency Parallel");
        log.info(String.format(FORMAT_PRINTF_15,WORD,FRECUENCY));
        final Map<String,Long> mapWordsParallel = frecuencyWordsService.frecuencyWordsParallel(WORDS);
        Assertions.assertNotNull(mapWordsParallel);

        mapWordsParallel.forEach((word, frecuency) -> {
            log.info(String.format(FORMAT_PRINTF_15, word, frecuency));
        });

        final long result = mapWordsParallel.get(HOLA);

        final long expected = 1;
        MatcherAssert.assertThat(expected, Matchers.is(result));
        /*
         * total memory consumption
         */
        log.info(memory.getTotalMemory());
    }

    @Test
    void testFrecuencyWordsFromTextFile() {
        log.info("Run Frecuency Parallel");
        try (final InputStream inputStream = Files.newInputStream(PATH_TEXT_FILE);
             final Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
             final BufferedReader br = new BufferedReader(reader)) {

            final StringBuilder sb = new StringBuilder();
            //read lines with Stream api
            sb.append(br.lines().collect(Collectors.joining()));

            final Map<String, Long> mapWordsParallel = frecuencyWordsService.frecuencyWords(sb.toString());
            log.info(String.format(FORMAT_PRINTF_15,WORD,FRECUENCY));
            mapWordsParallel.forEach((word, frecuency) -> {
                log.info(String.format(FORMAT_PRINTF_15, word, frecuency));
            });

            final long result = mapWordsParallel.get(HOLA);
            final long expected = 1;

            MatcherAssert.assertThat(expected, Matchers.is(result));

        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
        /*
         * total memory consumption
         */
        log.info(memory.getTotalMemory());
    }

    @Test
    void frecuencyFromPath() {
        log.info("Run Frecuency Parallel");
        final Map<String,Long> mapWordsParallel = frecuencyWordsService.frecuencyWordsFromFile(PATH_TEXT_FILE);
        log.info(String.format(FORMAT_PRINTF_15, WORD,FRECUENCY));
        mapWordsParallel.forEach((word, frecuency) -> {
            log.info(String.format(FORMAT_PRINTF_15, word, frecuency));
        });

        final long result = mapWordsParallel.get(HOLA);
        final long expected = 1;

        MatcherAssert.assertThat(expected, Matchers.is(result));
        /*
         * total memory consumption
         */
        log.info(memory.getTotalMemory());
    }

} 