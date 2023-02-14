package com.example.jablonski.pwc.model;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.LinkedList;
import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CountryNode {
    private CountryNode parent;
    private CountryNode parentFromStart;
    private CountryNode parentFromEnd;
    private boolean visited;
    private String name;
    private String code;
    private List<CountryNode> borders;


    public CountryNode(CountryNode parent, boolean visited, String name, String code, List<CountryNode> borders) {
        this.parent = parent;
        this.visited = visited;
        this.name = name;
        this.code = code;
        this.borders = borders;
    }

    public static CountryNode of(String name, String code) {
        return new CountryNode(null, false, name, code, new LinkedList<>());
    }

    public void addBorder(CountryNode border) {
        if (!borders.contains(border)) {
            borders.add(border);
        }
    }

    public void setParentFromStart(CountryNode parentFromStart){
        this.parentFromStart = parentFromStart;
    }

    public void setParentFromEnd(CountryNode parentFromEnd){
        this.parentFromEnd = parentFromEnd;
    }
}
