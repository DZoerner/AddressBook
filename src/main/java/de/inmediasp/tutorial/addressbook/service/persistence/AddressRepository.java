package de.inmediasp.tutorial.addressbook.service.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import de.inmediasp.tutorial.addressbook.type.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {
}