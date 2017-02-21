package com.avast.server.libver.service.impl;

import com.avast.server.libver.model.gradle.GradleDepsDescriptor;
import com.avast.server.libver.service.GradleService;
import org.springframework.stereotype.Service;

import java.text.ParseException;

/**
 * @author Vitasek L.
 */
@Service
public class GradleServiceImpl implements GradleService {


    @Override
    public GradleDepsDescriptor parse(String data) throws ParseException {
        return new GradleParser(data).parse();
    }
}
