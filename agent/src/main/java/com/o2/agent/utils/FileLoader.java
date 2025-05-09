package com.o2.agent.utils;

import lombok.NoArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

@Component
@NoArgsConstructor
public class FileLoader {
    public String load(String fileName) {
        ClassPathResource resource = new ClassPathResource(fileName);
        try (InputStream inputStream = resource.getInputStream()) {
            Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8);
            return scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
        } catch (IOException e) {
            throw new RuntimeException("Error loading file: " + fileName, e);
        }
    }
}
