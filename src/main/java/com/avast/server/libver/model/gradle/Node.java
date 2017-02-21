package com.avast.server.libver.model.gradle;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vitasek L.
 */
public class Node {
    public String text;

    @JsonIgnore
    public int level;
    private List<Node> children;


    public Node() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public List<Node> getChildren() {
        return children;
    }

    public void setChildren(List<Node> children) {
        this.children = children;
    }

    public Node(String text, int level) {
        this.text = text;
        this.level = level;
    }

    public void addChild(Node childNode) {
        if (children == null) {
            children = new ArrayList<>();
        }
        children.add(childNode);
    }

    @Override
    public String toString() {
        return "{" +
                text + '\'' +
                ", level=" + level +
                '}';
    }
}