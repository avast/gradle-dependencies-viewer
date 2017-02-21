package com.avast.server.libver.model.gradle;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Vitasek L.
 */
public class GradleDepsDescriptor {
    Map<String, Subproject> projects = new LinkedHashMap<>();

    public Map<String, Subproject> getProjects() {
        return projects;
    }

    public void setProjects(Map<String, Subproject> projects) {
        this.projects = projects;
    }
}
