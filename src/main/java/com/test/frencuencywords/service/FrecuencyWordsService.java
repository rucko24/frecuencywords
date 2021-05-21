package com.test.frencuencywords.service;

import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 */
@Service
public class FrecuencyWordsService {

    private static final String REG_EXP = "[\\.\\,\\!]";
    private static final String SPLIT_ESPACE_CARACTER = " ";
    
    /**
     *
     * Process with parallelStream()
     *
     * @param words to count
     * @return Map<String,Long> with key(word), and value(frecuency)
     */
    public Map<String,Long> frecuencyWordsParallel(@NonNull final String words) {
        final String[] splitWords = words.split(SPLIT_ESPACE_CARACTER);
        return Arrays.stream(splitWords)
                .parallel()
                .filter(this::filterEmptyAndNull)
                .map(this::normalize)           
                .collect(Collectors.groupingBy(Function.identity(),Collectors.counting()));
    }

    /**
     *
     * Process with stream()
     *
     * @param words to count
     * @return Map<String,Long> with key(word), and value(frecuency)
     */
    public Map<String,Long> frecuencyWords(@NonNull final String words) {
        final String[] splitWords = words.split(SPLIT_ESPACE_CARACTER);
        return Arrays.stream(splitWords)
                .filter(this::filterEmptyAndNull)
                .map(this::normalize)
                .collect(Collectors.groupingBy(Function.identity(),Collectors.counting()));
    }

    /**
     *
     * @param input path
     * @return Map<String,Long> with key(word), and value(frecuency)
     */
    public Map<String,Long> frecuencyWordsFromFile(final Path input) {
        try (final Stream<String> lines = Files.lines(input)) {

            return Arrays.stream(lines.collect(Collectors.joining())
                    .split(SPLIT_ESPACE_CARACTER))
                    .filter(this::filterEmptyAndNull)
                    .map(this::normalize)
                    .collect(Collectors.groupingBy(Function.identity(),Collectors.counting()));

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private boolean filterEmptyAndNull(final String word) {
        return Objects.nonNull(word) && !"".equals(word);
    }

    private String normalize(final String word) {
        return word.toLowerCase().replaceAll(REG_EXP, "");
    }

}