package com.avast.server.libver.service.impl;

import com.avast.server.libver.model.gradle.GradleDepsDescriptor;
import com.avast.server.libver.model.gradle.Node;
import com.avast.server.libver.model.gradle.Subproject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Vitasek L.
 */
public class GradleParser {
    private final static Logger logger = LoggerFactory.getLogger(GradleParser.class);

    private final static Pattern TREE_LINE = Pattern.compile("\\\\|\\+---");
    private String data;

    private final static String PROJECT_TITLE = "------------------------------------------------------------\\n(.*?)\\n------------------------------------------------------------";
    private final static Pattern PROJECT_PATTERN = Pattern.compile(PROJECT_TITLE);


    public GradleParser(String data) {
        this.data = data.replace("\r\n", "\n") + "\n\n";
    }

    public GradleDepsDescriptor parse() throws ParseException {
        int projectIndex = 0;
        final GradleDepsDescriptor gradleDepsDescriptor = new GradleDepsDescriptor();
        final Matcher projectMatcher = PROJECT_PATTERN.matcher(data);
        while (projectMatcher.find(projectIndex)) {
            projectIndex = projectMatcher.start();
            String projectName = projectMatcher.group(1);
            if (projectName.startsWith("Project :")) {
                projectName = projectName.substring(9);
            }
            final Subproject subproject = new Subproject();
            gradleDepsDescriptor.getProjects().put(projectName, subproject);
            final Matcher projectMatcher2 = PROJECT_PATTERN.matcher(data);
            final boolean foundAnotherProject = projectMatcher2.find(projectMatcher.end());
            final String projectDepsData;
            if (foundAnotherProject) {
                projectDepsData = data.substring(projectIndex, projectMatcher2.start());
            } else {
                projectDepsData = data.substring(projectIndex);
            }
            fillSourceSets(subproject, projectDepsData);
            projectIndex = projectMatcher.end();
        }
        return gradleDepsDescriptor;
    }

    private void fillSourceSets(Subproject subproject, String projectDepsData) throws ParseException {
        final Matcher matcher = TREE_LINE.matcher(projectDepsData);
        int sourceIndex = 0;
        while (matcher.find(sourceIndex)) {
            final int treeLineStart = matcher.start();
            final int titleIndex = projectDepsData.lastIndexOf("\n\n", treeLineStart);
            if (titleIndex == -1) {
                throw new ParseException("Cannot find source set description", sourceIndex);
            }
            String sourceSet = projectDepsData.substring(titleIndex + 2, projectDepsData.indexOf('\n', titleIndex + 3));
            if (sourceSet.contains(" - ")) {
                sourceSet = sourceSet.substring(0, sourceSet.lastIndexOf(" - "));
            }

            final Node root = new Node("root", -1);

            try (SourceSetParser sourceSetParser = new SourceSetParser(projectDepsData.substring(treeLineStart))) {
                sourceSetParser.walkTree(root);
            } catch (Exception e) {
                logger.error("Failed to parse source set", e);
                throw new ParseException("Cannot parse source set: " + e.getMessage(), 0);
            }
            if (root.getChildren() != null) {
                subproject.getSourceSet().put(sourceSet, root.getChildren());
            }
            sourceIndex = projectDepsData.indexOf("\n\n", treeLineStart);
        }

    }

    private class SourceSetParser implements AutoCloseable{
        private Scanner scanner;

        SourceSetParser(String data) {
            this.scanner = new Scanner(data);
        }

        String walkTree(Node currentNode, String currentLine) {
            Node lastNode = currentNode;
            while (currentLine != null) {
                final int foundPlus = currentLine.indexOf("---");//well, not 100% optimized when going back in tree
                int lineLevel = 0;
                if (foundPlus >= 0) {
                    lineLevel = (foundPlus - 1) / 5;
                } else assert false;
                if (currentNode.level + 1 > lineLevel) {
                    return currentLine;
                }
                final Node newNode = new Node(currentLine.substring(foundPlus + 4).trim(), lineLevel);
                if (currentNode.level + 1 == lineLevel) { //on same level
                    currentNode.addChild(newNode);
                    lastNode = newNode;
                    currentLine = getLine();
                } else {
                    if (currentNode.level + 1 < lineLevel) { // on next level
                        lastNode.addChild(newNode);
                        currentLine = walkTree(newNode, getLine());
                    }
                }
            }
            return null;
        }

        private String getLine() {
            if (scanner.hasNextLine()) {
                final String line = scanner.nextLine();
                if (StringUtils.isEmpty(line.trim())) {
                    return null;
                }
                return line;
            }
            return null;
        }

        @Override
        public void close() throws Exception {
            if (scanner != null) {
                scanner.close();
            }
        }

        void walkTree(Node root) {
            walkTree(root, getLine());
        }
    }


}
