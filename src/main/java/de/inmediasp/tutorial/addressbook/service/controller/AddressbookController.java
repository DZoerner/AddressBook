package de.inmediasp.tutorial.addressbook.service.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
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
@RequestMapping("addresses")
public class AddressbookController {
	@Autowired
	private AddressRepository addressRepository;
	
	@Autowired
	NamedParameterJdbcTemplate jdbcTemplate;
	
	// create single
	@RequestMapping(method = RequestMethod.POST, headers="type=address")
	public long create(@RequestBody @Valid Address address) {

		return addressRepository.saveAndFlush(address).getId();
	}

	// create all
	@RequestMapping(method = RequestMethod.POST, headers="type=addresslist")
	public String createAll(@RequestBody @Valid AddressList addresses) {

		for(Address address: addresses.getAddresses()) {
			addressRepository.saveAndFlush(address);
		}
		return "ok";
	}

	// update
	@RequestMapping(method = RequestMethod.PUT)
	public String update(@RequestBody @Valid Address address) {

		addressRepository.saveAndFlush(address);
		
		return "ok";
	}

	// delete
	@RequestMapping(value="{id}", method = RequestMethod.DELETE)
	public String delete(@PathVariable(value = "id") long id) {
		if(!addressRepository.exists(id)) {
			throw new ResourceNotFoundException(id);
		}
		
		addressRepository.delete(id);
		
		return "ok";
	}

	// delete all
	@RequestMapping(method = RequestMethod.DELETE)
	public String deleteAll() {

		addressRepository.deleteAll();

		return "ok";
	}

	// get single xml
	@RequestMapping(value="{id}", method = RequestMethod.GET, produces = { "application/xml", "text/xml" })
	@ResponseBody
	public Address get(@PathVariable(value = "id") long id) {

		return addressRepository.findOne(id);
	}

	// get single json
	@RequestMapping(value="{id}", method = RequestMethod.GET, produces = { "application/json", "text/json" })
    @CrossOrigin(origins = "http://localhost:8090")
	@ResponseBody
	public Address getJson(@PathVariable(value = "id") long id) {

		return addressRepository.findOne(id);
	}

	// get all json
	@RequestMapping(method = RequestMethod.GET, produces = { "application/xml", "text/xml" })
    @CrossOrigin(origins = "http://localhost:8090")
	@ResponseBody
	public AddressList getAll() {
		List<Address> addresses= addressRepository.findAll();
		
		return new AddressList(addresses);
	}

	// get all json
	@RequestMapping(method = RequestMethod.GET, produces = { "application/json", "text/json" })
    @CrossOrigin(origins = "http://localhost:8090")
	@ResponseBody
	public AddressList getAllJson() {
		List<Address> addresses= addressRepository.findAll();
		
		return new AddressList(addresses);
	}

	// find filtered
	@RequestMapping(value="/", method = RequestMethod.GET, produces = { "application/xml", "text/xml" })
	@ResponseBody
	public AddressList getFiltered(
			@RequestParam(value = "firstname", required=false) String firstname,
			@RequestParam(value = "lastname",  required=false) String lastname,
			@RequestParam(value = "email",     required=false) String email
			) {
		
		return new AddressDao().getFiltered(jdbcTemplate, firstname, lastname, email);
	}
}