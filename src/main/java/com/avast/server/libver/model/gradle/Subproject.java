package com.avast.server.libver.model.gradle;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Vitasek L.
 */
public class Subproject {
    private Map<String, List<Node>> sourceSet = new LinkedHashMap<>();

    public Map<String, List<Node>> getSourceSet() {
        return sourceSet;
    }

    public void setSourceSet(Map<String, List<Node>> sourceSet) {
        this.sourceSet = sourceSet;
    }
}
