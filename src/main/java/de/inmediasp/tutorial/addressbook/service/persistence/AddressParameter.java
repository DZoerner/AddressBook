package de.inmediasp.tutorial.addressbook.service.persistence;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import de.inmediasp.tutorial.addressbook.type.Address;

public class AddressParameter {
	private Address address;
	
	public AddressParameter(Address address) {
		this.address= address;
	}
	
	public MapSqlParameterSource getSource(){
		MapSqlParameterSource ret= new MapSqlParameterSource();
		
		ret.addValue("firstname", address.getFirstname());
		ret.addValue("lastname", address.getLastname());
		ret.addValue("email", address.getEmail());
		
		return ret;
	}
}