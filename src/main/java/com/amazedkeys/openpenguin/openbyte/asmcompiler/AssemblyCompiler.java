package com.amazedkeys.openpenguin.openbyte.asmcompiler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AssemblyCompiler {
    // Attributes
    private String source;
    private String working_code;
    private HashMap<String, Integer> branch_labels;
    private List<String> lines;
    private List<List<String>> tokens;

    // Stage markers
    private boolean has_cleaned_comments, has_split_lines, has_tokenized, has_parsed, has_interpreted, has_made_object_file, has_made_binary,
                    has_resolved_labels = false;


    // Constructor
    public AssemblyCompiler() {
        this.branch_labels = new HashMap<>();
        this.lines = new ArrayList<>();
        this.tokens = new ArrayList<List<String>>();
    }

    // Public Methods
    public void load(String program) {
        this.source = program;
        this.working_code = program;
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
        this.working_code = this.source;
    }

    // Internal Methods
    protected void clean_comments() {
        this.working_code = this.working_code.replaceAll(";.+\n", "\n");
        this.working_code = this.working_code.replaceAll("\\s*\n", "\n");
        //System.out.println("---CODE WITHOUT COMMENTS---");
        //System.out.println(this.working_code);
        //System.out.println("---CODE WITHOUT COMMENTS---");
        this.has_cleaned_comments = true;
    }

    protected void split_lines() {
        if (!this.has_cleaned_comments)
            this.clean_comments();

        Pattern pattern = Pattern.compile("(.+)\\n");
        Matcher matcher = pattern.matcher(this.working_code);
        while (matcher.find()) {
            String l = matcher.group();
            l = l.substring(0, l.length() - 1);
            //System.out.println("[!] " + l);
            this.lines.add(l);
        }

        //this.lines = this.working_code.split("(.+)\n");
        this.has_split_lines = true;
    }

    protected void tokenize() {
        System.out.println("Tokenize!");
        if (!this.has_split_lines)
            this.split_lines();
        for (String line : this.lines) {
            ArrayList<String> t = new ArrayList<>();

            System.out.println("Line \"" + line + "\"!");

            if (line.endsWith(":")) {
                // label declaration
                String label_name = line.substring(0, line.length() - 1);
                System.out.println("Found label named \"" + label_name + "\"!");
                t.add("LABEL_DECLARE");
                t.add(label_name);
            } else {
                line = line.replaceAll("(.+) (.+),? (.+)", "$1 $2 $3");
                t.addAll(Arrays.asList(line.split(" ")));
            }

            this.tokens.add(t);
        }
        this.has_tokenized = true;
    }

    private String concatStringList(List<String> in, String merger) {
        StringBuilder builder = new StringBuilder();
        for (String s : in)
            builder.append(s).append(merger);
        return builder.toString();
    }

    private String concatStringList(List<String> in) {
        return concatStringList(in, " ");
    }


    protected void parse() {
        if (!this.has_tokenized)
            this.tokenize();
        AssemblyLookupTable alt = new AssemblyLookupTable();
        for (List<String> instr : this.tokens) {
            String line = concatStringList(instr);
            String opcode = alt.identify_opcode(line);

        }
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
