package com.minidocdb;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DocumentStoreTest {
    @Test
    void helloWorks() {
        assertEquals("minidocdb", new DocumentStore().hello());
    }
}