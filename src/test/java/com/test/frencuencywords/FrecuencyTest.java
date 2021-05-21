package com.test.frencuencywords;

import com.test.frencuencywords.service.FrecuencyWordsService;
import com.test.frencuencywords.util.Memory;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

/**
 * Simple test for frecuency words
 */
@Log4j2
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {FrecuencyWordsService.class,Memory.class})
@DisplayName("Frecuency words, reading text, sync, async from String or File")
class FrecuencyTest {

    private static final String FORMAT_PRINTF_15 = "%-15s%s";
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
    @DisplayName("reading text from String text with blockingIO")
    void testFrecuencyWordsNull() {
        log.info("Run Frecuency");
        log.info(String.format(FORMAT_PRINTF_15,WORD,FRECUENCY));
        final Map<String,Long> mapWordsSync = frecuencyWordsService.frecuencyWords(WORDS);
        Assertions.assertNotNull(mapWordsSync);

        mapWordsSync.forEach((word, frecuency) -> {
            log.info(String.format(FORMAT_PRINTF_15, word, frecuency));
        });

        final long actual = mapWordsSync.get(HOLA);

        final long expected = 1;
        assertThat(actual).isEqualTo(expected);
        /*
         * total memory consumption
         */
        log.info(memory.getTotalMemory());
    }

    @Test
    @DisplayName("reading text from String text parallel")
    void testFrecuencyWordsParallel() {
        log.info("Run Frecuency Parallel");
        log.info(String.format(FORMAT_PRINTF_15,WORD,FRECUENCY));
        final Map<String,Long> mapWordsParallel = frecuencyWordsService.frecuencyWordsParallel(WORDS);
        Assertions.assertNotNull(mapWordsParallel);

        mapWordsParallel.forEach((word, frecuency) -> {
            log.info(String.format(FORMAT_PRINTF_15, word, frecuency));
        });

        final long actual = mapWordsParallel.get(HOLA);

        final long expected = 1;
        assertThat(actual).isEqualTo(expected);
        /*
         * total memory consumption
         */
        log.info(memory.getTotalMemory());
    }

    @Test
    @DisplayName("reading text from input with 'BufferedReader' ")
    void testFrecuencyWordsFromTextFile() {
        if(Files.exists(PATH_TEXT_FILE)) {
            log.info("Run Frecuency Parallel");
            try (final InputStream inputStream = this.getClass().getResourceAsStream("/textoSimple.txt");
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

                assertThat(result).isEqualTo(expected);

            } catch (IOException ex) {
                log.error(ex.getMessage());
            }
            /*
             * total memory consumption
             */
            log.info(memory.getTotalMemory());
        } else {
            throw new RuntimeException("File not found!");
        }

    }

    @Test
    @DisplayName("reading text from input with 'Files.lines' ")
    void frecuencyFromPath() {
        log.info("Run Frecuency Parallel");
        final Map<String,Long> mapWordsParallel = frecuencyWordsService.frecuencyWordsFromFile(PATH_TEXT_FILE);
        log.info(String.format(FORMAT_PRINTF_15, WORD,FRECUENCY));
        mapWordsParallel.forEach((word, frecuency) -> {
            log.info(String.format(FORMAT_PRINTF_15, word, frecuency));
        });

        final long result = mapWordsParallel.get(HOLA);
        final long expected = 1;

        assertThat(result).isEqualTo(expected);
        /*
         * total memory consumption
         */
        log.info(memory.getTotalMemory());
    }

} 