package io._10a.tkdemo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "GREETINGS")
@NamedQuery(
		name = "Greetings.findAll", query = "SELECT g FROM Greeting g"
)
@NamedQuery(
		name = "Greeting.findAllLanguages", query = "SELECT g.language FROM Greeting g"
)
@NamedQuery(
		name = "Greeting.findByLanguage", query = "SELECT NEW io._10a.tkdemo.GreetingDTO(g.language, g.greeting) FROM Greeting g where g.language = :lang"
)
public class Greeting {

	@Id
	@Column(name = "GREETING_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "LANG")
	private String language;

	@Column(name = "GREETING")
	private String greeting;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getGreeting() {
		return greeting;
	}

	public void setGreeting(String greeting) {
		this.greeting = greeting;
	}

	@Override public String toString() {
		return "Greeting{" + "id=" + id + ", language='" + language + '\'' + ", greeting='" + greeting + '\'' + '}';
	}
}
