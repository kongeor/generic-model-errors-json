package io.github.kongeor.gmej.models;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Person extends BaseModel {

    private String name;
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    private Address address;
    public Address getAddress() { return address; }
    public void setAddress(Address address) { this.address = address; }

    private List<Pet> pets;
    public List<Pet> getPets() { return pets; }
    public void setPets(List<Pet> pets) { this.pets = pets; }

}
