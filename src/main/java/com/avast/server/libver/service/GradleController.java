package com.avast.server.libver.service;

import com.avast.server.libver.model.gradle.DataRequest;
import com.avast.server.libver.model.gradle.GradleDepsDescriptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@Controller
public class GradleController {

    @Autowired
    Environment env;

    @Autowired
    GradleService gradleService;

    @RequestMapping(value = "/gradle/parse", method = RequestMethod.POST)
    @ResponseBody
    public GradleDepsDescriptor parseGradle(@RequestBody DataRequest dataRequest) {
        if (dataRequest == null || dataRequest.getData() == null) {
            throw new ParseInvalidException("No data to parse");
        }
        try {
            final GradleDepsDescriptor parse = gradleService.parse(dataRequest.getData());
            if (parse.getProjects().isEmpty()) {
                throw new ParseInvalidException("Empty project list");
            }
            return parse;
        } catch (ParseException e) {
            throw new ParseInvalidException(e.getMessage());
        }
    }

    @RequestMapping("/")
    public String gradleDeps(Model model) {
        return "gradle";
    }


}
