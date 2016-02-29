package io.github.kongeor.gmej.models;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Address extends BaseModel {

    private String address;
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}
