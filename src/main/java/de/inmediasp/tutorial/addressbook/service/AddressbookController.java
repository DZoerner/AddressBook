package de.inmediasp.tutorial.addressbook.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import de.inmediasp.tutorial.addressbook.service.persistence.AddressDao;
import de.inmediasp.tutorial.addressbook.service.persistence.AddressRepository;
import de.inmediasp.tutorial.addressbook.type.Address;
import de.inmediasp.tutorial.addressbook.type.AddressList;

@RestController
public class AddressbookController {
	@Autowired
	private AddressRepository addressRepository;
	
	@Autowired
	NamedParameterJdbcTemplate jdbcTemplate;
	
	// PUT
	@RequestMapping(method = RequestMethod.PUT)
	public long create(@RequestBody @Valid Address address) {

		return addressRepository.saveAndFlush(address).getId();
	}

	// PUT all
	@RequestMapping(value="/all", method = RequestMethod.PUT)
	public String createAll(@RequestBody @Valid AddressList addresses) {

		for(Address address: addresses.getAddresses()) {
			addressRepository.saveAndFlush(address);
		}
		return "ok";
	}

	// POST
	@RequestMapping(method = RequestMethod.POST)
	public String update(@RequestBody @Valid Address address) {

		addressRepository.saveAndFlush(address);
		
		return "ok";
	}

	// DELETE
	@RequestMapping(method = RequestMethod.DELETE)
	public String delete(@RequestParam(value = "id") long id) {

		addressRepository.delete(id);
		
		return "ok";
	}

	// DELETE
	@RequestMapping(value="/all", method = RequestMethod.DELETE)
	public String deleteAll() {

		addressRepository.deleteAll();

		return "ok";
	}

	// GET single
	@RequestMapping(method = RequestMethod.GET, produces = { "application/xml", "text/xml" })
	@ResponseBody
	public Address get(@RequestParam(value = "id") long id) {

		return addressRepository.findOne(id);
	}

	// GET single
	@RequestMapping(method = RequestMethod.GET, produces = { "application/json", "text/json" })
    @CrossOrigin(origins = "http://localhost:8090")
	@ResponseBody
	public Address getJson(@RequestParam(value = "id") long id) {

		return addressRepository.findOne(id);
	}

	// GET all
	@RequestMapping(value="/all", method = RequestMethod.GET, produces = { "application/xml", "text/xml" })
    @CrossOrigin(origins = "http://localhost:8090")
	@ResponseBody
	public AddressList getAll() {
		List<Address> addresses= addressRepository.findAll();
		
		return new AddressList(addresses);
	}

	// GET all json
	@RequestMapping(value="/all", method = RequestMethod.GET, produces = { "application/json", "text/json" })
    @CrossOrigin(origins = "http://localhost:8090")
	@ResponseBody
	public AddressList getAllJson() {
		List<Address> addresses= addressRepository.findAll();
		
		return new AddressList(addresses);
	}

	// FIND filtered
	@RequestMapping(value="/search", method = RequestMethod.GET, produces = { "application/xml", "text/xml" })
	@ResponseBody
	public AddressList getFiltered(
			@RequestParam(value = "firstname", required=false) String firstname,
			@RequestParam(value = "lastname", required=false) String lastname,
			@RequestParam(value = "email", required=false) String email
			) {
		
		return new AddressDao().getFiltered(jdbcTemplate, firstname, lastname, email);
	}
}