package io._10a.tkdemo;

import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.SetJoin;
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

	public GreetingDTO getGreetingForLangWithCriteria(String lang) {

		// SELECT NEW io._10a.tkdemo.GreetingDTO(g.language, g.greeting)
		// FROM Greeting g
		// where g.language = :lang

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<GreetingDTO> query = builder.createQuery(GreetingDTO.class);

		Root<Greeting> root = query.from(Greeting.class);
		final SetJoin<Greeting, Greeter> greeterJoin = root.join(Greeting_.greeters);

		query.select(
				builder.construct(GreetingDTO.class, root.get(Greeting_.language), root.get(Greeting_.greeting))
		).where(
				builder.equal(root.get(Greeting_.language), builder.parameter(String.class, "lang"))
		);

		// ....

		return entityManager.createQuery(query).
				setParameter("lang", lang).
				getResultList().stream().findFirst().orElse(null);


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
