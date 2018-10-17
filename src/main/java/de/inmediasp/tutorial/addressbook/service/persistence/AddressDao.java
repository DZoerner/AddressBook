package de.inmediasp.tutorial.addressbook.service.persistence;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import de.inmediasp.tutorial.addressbook.type.AddressList;

public class AddressDao {
	final static String select_filtered_sql = "SELECT * from Address WHERE 1=1 ";

	public AddressList getFiltered(NamedParameterJdbcTemplate jdbcTemplate, 
			String firstname,
			String lastname,
			String email
			) {
		
		if(firstname != null && firstname.length() == 0) {
			firstname= null;
		}
		if(lastname != null && lastname.length() == 0) {
			lastname= null;
		}
		if(email != null && email.length() == 0) {
			email= null;
		}

		String sql= select_filtered_sql;
		MapSqlParameterSource parameter= new MapSqlParameterSource();
		
		if(firstname != null) {
			sql+= " AND firstname=:firstname";
			parameter.addValue("firstname", firstname);
		}
		if(lastname != null) {
			sql+= " AND lastname=:lastname";
			parameter.addValue("lastname", lastname);
		}
		if(email != null) {
			sql+= " AND email=:email";
			parameter.addValue("email", email);
		}
		
		AddressList ret= new AddressList(jdbcTemplate.query(sql, parameter, new AddressRowMaper()));
		
		return ret;
	}
}