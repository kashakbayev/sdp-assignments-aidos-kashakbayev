package model;

import visitor.DocumentVisitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Table implements DocumentElement {

    private final List<String> headers;
    private final List<List<String>> rows;

    public Table(List<String> headers, List<List<String>> rows) {
        this.headers = Collections.unmodifiableList(new ArrayList<>(headers));
        this.rows = Collections.unmodifiableList(new ArrayList<>(rows));
    }

    public List<String> getHeaders() {
        return headers;
    }

    public List<List<String>> getRows() {
        return rows;
    }

    @Override
    public void accept(DocumentVisitor visitor) {
        visitor.visit(this);
    }
}