package io._10a.tkdemo;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.SetJoin;
import javax.transaction.Transactional;
import org.hibernate.transform.ResultTransformer;

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

	@SuppressWarnings("unchecked")
	public List<GreetingDTO> getGreetingWithTransformer() {

		// SELECT g.id, g.language, g.greeting, gt.name FROM Greeting g LEFT JOIN FETCH g.greeter gt

		return entityManager.createNamedQuery("Greeting.forTransformer")
				.unwrap(org.hibernate.query.Query.class)
				.setResultTransformer(new ResultTransformer() {

					final Map<Long, GreetingDTO> myDTOs = new LinkedHashMap<>();

					@Override public Object transformTuple(Object[] objects, String[] strings) {

						Long greetingId = (Long)objects[0];
						myDTOs.putIfAbsent(greetingId, new GreetingDTO(
								(String) objects[1],
								(String) objects[2],
								new LinkedHashSet<>()
						));

						if (objects[3] instanceof String) {
							myDTOs.get(greetingId).greeters().add(new GreeterDTO((String) objects[3]));
						}

						return myDTOs.get(greetingId);
					}

					@Override public List transformList(List list) {
						return new ArrayList(myDTOs.values());
					}
				}).getResultList();

	}

}
