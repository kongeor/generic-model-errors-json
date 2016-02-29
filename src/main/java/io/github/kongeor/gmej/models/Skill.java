package io.github.kongeor.gmej.models;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Skill extends BaseModel {

    private String skill;
    public String getSkill() { return skill; }
    public void setSkill(String skill) { this.skill = skill; }
}
