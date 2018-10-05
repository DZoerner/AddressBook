package de.inmediasp.tutorial.addressbook.service;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import de.inmediasp.tutorial.addressbook.service.persistence.AddressDao;
import de.inmediasp.tutorial.addressbook.type.Address;
import de.inmediasp.tutorial.addressbook.type.AddressList;

@RestController
public class AddressbookController {
	@Autowired
	NamedParameterJdbcTemplate jdbcTemplate;

	
	// PUT
	@RequestMapping(method = RequestMethod.PUT)
	public int create(@RequestBody @Valid Address address) {

		return new AddressDao().insert(jdbcTemplate, address);
	}

	// PUT all
	@RequestMapping(value="/all", method = RequestMethod.PUT)
	public String createAll(@RequestBody @Valid AddressList addresses) {

		return new AddressDao().intertAll(jdbcTemplate, addresses);
	}

	// POST
	@RequestMapping(method = RequestMethod.POST)
	public String update(
			@RequestParam(value = "id") int id,
			@RequestBody @Valid Address address
			) {

		return new AddressDao().update(jdbcTemplate, id, address);
	}

	// DELETE
	@RequestMapping(method = RequestMethod.DELETE)
	public String delete(@RequestParam(value = "id") int id) {

		return new AddressDao().delete(jdbcTemplate, id);
	}

	// DELETE
	@RequestMapping(value="/all", method = RequestMethod.DELETE)
	public String deleteAll() {

		return new AddressDao().deleteAll(jdbcTemplate);
	}

	// GET single
	@RequestMapping(method = RequestMethod.GET, produces = { "application/xml", "text/xml" })
	@ResponseBody
	public Address get(@RequestParam(value = "id") int id) {

		return new AddressDao().get(jdbcTemplate, id);
	}

	// GET all
	@RequestMapping(value="/all", method = RequestMethod.GET, produces = { "application/xml", "text/xml" })
	@ResponseBody
	public AddressList getAll() {
		return new AddressDao().getAll(jdbcTemplate);
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