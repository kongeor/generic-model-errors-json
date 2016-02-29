package io.github.kongeor.gmej.models;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Pet extends BaseModel {

    private String type;
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    List<Skill> skills;
    public List<Skill> getSkills() { return skills; }
    public void setSkills(List<Skill> skills) { this.skills = skills; }
}
