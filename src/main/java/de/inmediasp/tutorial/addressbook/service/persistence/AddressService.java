package de.inmediasp.tutorial.addressbook.service.persistence;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import de.inmediasp.tutorial.addressbook.type.Address;

@Repository
@Transactional
public class AddressService {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	public long insert(Address address) {
		entityManager.persist(address);
		return address.getId();
	}

	public Address find(long id) {
		return entityManager.find(Address.class, id);
	}
	
	@SuppressWarnings("unchecked")
	public List<Address> findAll() {
		Query query = entityManager.createNamedQuery(
				"query_find_all_addresses", Address.class);
		return query.getResultList();
	}
}