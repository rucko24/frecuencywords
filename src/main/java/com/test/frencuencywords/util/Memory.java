package com.test.frencuencywords.util;

import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class Memory {

    private static final Runtime RUNTIME = Runtime.getRuntime();

    public String getTotalMemory() {
        return String.format("Total Memory %dMB",
                (RUNTIME.totalMemory() - RUNTIME.freeMemory()) / ( 1024 * 1024 ));
    }
}
