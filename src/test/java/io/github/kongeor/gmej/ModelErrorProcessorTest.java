package io.github.kongeor.gmej;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.kongeor.gmej.models.Address;
import io.github.kongeor.gmej.models.Person;
import io.github.kongeor.gmej.models.Pet;
import io.github.kongeor.gmej.models.Skill;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

public class ModelErrorProcessorTest {

    Person simplePerson = new Person();

    ObjectMapper mapper = new ObjectMapper();

    ModelErrorProcessor processor = new ModelErrorProcessor();

    @Before
    public void setup() {
	simplePerson.setName("Foo");
	Address address = new Address();
	address.setAddress("Bar 12");
	simplePerson.setAddress(address);
    }


    @Test
    public void invalid_base_error() throws IOException {
	String errorJson = "{\"errors\": { \"base\": [\"Name is invalid\"]}}";
	Object errors = mapper.readValue(errorJson, Object.class);
	processor.process(simplePerson, (Map<String, Object>) errors);
	String out = mapper.writeValueAsString(simplePerson);
	assertThat(out).isEqualTo("{\"errors\":{\"base\":[\"Name is invalid\"]},\"name\":\"Foo\",\"address\":{\"address\":\"Bar 12\"}}");
    }

    @Test
    public void invalid_nested_field() throws IOException {
	String errorJson = "{\"errors\": { \"address\": [\"Address is invalid\"]}}";
	Object errors = mapper.readValue(errorJson, Object.class);
	processor.process(simplePerson, (Map<String, Object>) errors);
	String out = mapper.writeValueAsString(simplePerson);
	assertThat(out).isEqualTo("{\"name\":\"Foo\",\"address\":{\"errors\":{\"address\":[\"Address is invalid\"]},\"address\":\"Bar 12\"}}");
    }

    @Test
    public void invalid_list_errors() throws IOException {
	Pet cat = new Pet();
	cat.setType("dog");
	Pet dog = new Pet();
	dog.setType("dog");

	simplePerson.setPets(Arrays.asList(cat, dog));

	String errorJson = "{\"errors\": { \"pets[0]\": [\"Cat person\"]}}";
	Object errors = mapper.readValue(errorJson, Object.class);
	processor.process(simplePerson, (Map<String, Object>) errors);
	String out = mapper.writeValueAsString(simplePerson);
	assertThat(out).isEqualTo("{\"name\":\"Foo\",\"address\":{\"address\":\"Bar 12\"},\"pets\":[{\"errors\":{\"base\":[\"Cat person\"]},\"type\":\"dog\"},{\"type\":\"dog\"}]}");
    }

    @Test
    public void invalid_nested_list_errors() throws IOException {
	Pet dog = new Pet();
	dog.setType("dog");

	Skill rollover = new Skill();
	rollover.setSkill("Rollover");

	Skill invalid = new Skill();
	invalid .setSkill("Make a sandwich");

	dog.setSkills(Arrays.asList(rollover, invalid));

	simplePerson.setPets(Collections.singletonList(dog));

	String errorJson = "{\"errors\": { \"pets[0]\": { \"skills[1]\": [\"Dogs can't make sandwiches\"]}}}";
	Object errors = mapper.readValue(errorJson, Object.class);
	processor.process(simplePerson, (Map<String, Object>) errors);
	String out = mapper.writeValueAsString(simplePerson);
	assertThat(out).isEqualTo("{\"name\":\"Foo\",\"address\":{\"address\":\"Bar 12\"},\"pets\":[{\"type\":\"dog\",\"skills\":[{\"skill\":\"Rollover\"},{\"errors\":{\"base\":[\"Dogs can't make sandwiches\"]},\"skill\":\"Make a sandwich\"}]}]}");
    }
}