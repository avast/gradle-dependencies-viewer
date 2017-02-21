package com.avast.server.libver.service;

import com.avast.server.libver.model.gradle.GradleDepsDescriptor;

import java.text.ParseException;

/**
 * @author Vitasek L.
 */
public interface GradleService {

    GradleDepsDescriptor parse(String data) throws ParseException;
}
