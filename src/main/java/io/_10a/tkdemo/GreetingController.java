package io._10a.tkdemo;

import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@RequestScoped
public class GreetingController {

	@Inject EntityManager entityManager;

	@Inject GreetingMapper greetingMapper;

	public List<String> getAllLanguages() {
		return entityManager.createNamedQuery("Greeting.findAllLanguages", String.class).getResultList();
	}

	public GreetingDTO getGreetingForLang(String lang) {
		return entityManager.createNamedQuery("Greeting.findByLanguage", GreetingDTO.class)
				.setParameter("lang", lang).getResultList().stream().findFirst().orElse(null);
	}

	@Transactional
	public boolean createNewGreeting(GreetingDTO greetingDTO) {
		Greeting greeting = greetingMapper.fromGreetingDTO(greetingDTO);

		entityManager.persist(greeting);
		return true;
	}

	@Transactional
	public Greeting updateGreeting(Long greetingId, GreetingDTO greetingDTO) {
		Greeting greeting = entityManager.find(Greeting.class, greetingId);
		if (greeting == null) {
			return null;
		} else {
			greeting.setLanguage(greetingDTO.lang());
			greeting.setGreeting(greetingDTO.greeting());
			return entityManager.merge(greeting);
		}
	}

	@Transactional
	public boolean deleteGreeting(Long greetingId) {
		final Greeting reference = entityManager.getReference(Greeting.class, greetingId);
		if (reference != null) {
			entityManager.remove(reference);
			return true;
		}
		return false;
	}
}
