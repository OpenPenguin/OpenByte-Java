package com.amazedkeys.openpenguin.openbyte.asmcompiler;

import java.util.HashMap;

public class AssemblyCompiler {
    // Attributes
    private String source;
    private HashMap<String, Integer> branch_labels;
    private String[] lines;

    // Stage markers
    private boolean has_cleaned_comments, has_split_lines, has_parsed, has_interpreted, has_made_object_file, has_made_binary,
                    has_resolved_labels = false;


    // Constructor
    public AssemblyCompiler() {

    }

    // Public Methods
    public void load(String program) {

    }

    public void start() {

    }

    public void getObjectFile() {
        if (!this.has_made_object_file)
            this.make_object_file();
        //TODO return object file
    }

    public void getBinary() {
        if (!this.has_made_binary)
            this.make_binary();
        //TODO return binary
    }

    public void clean() {
        this.has_cleaned_comments = false;
        this.has_split_lines = false;
        this.has_parsed = false;
        this.has_interpreted = false;
        this.has_made_object_file = false;
        this.has_made_binary = false;
    }

    // Internal Methods
    protected void clean_comments() {

    }

    protected void split_lines() {
        if (!this.has_cleaned_comments)
            this.clean_comments();
    }

    protected void parse() {
        if (!this.has_split_lines)
            this.split_lines();
    }

    protected void interpret() {
        if (!this.has_parsed)
            this.parse();
    }

    protected void resolve_labels() {

    }

    // Internal Methods to Build End Products
    protected void make_object_file() {
        if (!this.has_interpreted)
            this.interpret();
    }

    protected void make_binary() {
        if (!this.has_interpreted)
            this.interpret();
        if (!this.has_resolved_labels)
            this.resolve_labels();
    }

}
