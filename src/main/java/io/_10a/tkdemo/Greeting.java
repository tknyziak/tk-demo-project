package io._10a.tkdemo;

import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
		name = "Greeting.findByLanguage", query = "SELECT g FROM Greeting g where g.language = :lang"
)
@NamedQuery(
		name = "Greeting.forTransformer", query = "SELECT g.id, g.language, g.greeting, gt.name FROM Greeting g LEFT JOIN g.greeters gt"
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

	@OneToMany
	@JoinColumn(name = "GREETING_ID", referencedColumnName = "GREETING_ID")
	private Set<Greeter> greeters;


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
