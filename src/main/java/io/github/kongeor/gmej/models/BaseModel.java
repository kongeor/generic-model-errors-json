package io.github.kongeor.gmej.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseModel {

    Map<String, List<String>> errors = new HashMap<>();
    public Map<String, List<String>> getErrors() { return errors; }
    public void setErrors(Map<String, List<String>> errors) { this.errors = errors; }

}
