package hib.test.cn;

import java.util.HashSet;
import java.util.Set;

public class Person {

	private Long id;
	private int age;
	private String firstname;
	private String lastname;

	Person() {
	}

	public Long getId() {
		return id;
	}

	public int getAge() {
		return age;
	}

	public String getFirstname() {
		return firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	private Set events = new HashSet();

	public Set getEvents() {
		return events;
	}

	public void setEvents(Set events) {
		this.events = events;
	}

	// Accessor methods for all properties, private setter for 'id'

}
