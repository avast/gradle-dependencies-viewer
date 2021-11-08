package com.avast.server.libver.service;


import com.avast.server.libver.model.gradle.GradleDepsDescriptor;
import com.avast.server.libver.service.impl.GradleServiceImpl;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

/**
 * @author Vitasek L.
 */
public class GradleServiceTest {
    @Test
    public void parseGradle() throws Exception {
        final URI uri = GradleServiceTest.class.getResource("/dep.txt").toURI();
        final String data = Files.lines(Paths.get(uri)).collect(Collectors.joining("\n"));
        final GradleDepsDescriptor gradleDesc = new GradleServiceImpl().parse(data);
        System.out.println("gradleDesc = " + gradleDesc);
    }

    @Test
    public void parseGradle2() throws Exception {
        final URI uri = GradleServiceTest.class.getResource("/dep2.txt").toURI();
        final String data = Files.lines(Paths.get(uri)).collect(Collectors.joining("\n"));
        final GradleDepsDescriptor gradleDesc = new GradleServiceImpl().parse(data);
        System.out.println("gradleDesc = " + gradleDesc);
    }
}
