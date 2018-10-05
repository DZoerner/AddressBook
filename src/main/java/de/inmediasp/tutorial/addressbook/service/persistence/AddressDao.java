package de.inmediasp.tutorial.addressbook.service.persistence;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import de.inmediasp.tutorial.addressbook.type.Address;
import de.inmediasp.tutorial.addressbook.type.AddressList;

public class AddressDao {
	final static String insert_sql = "INSERT INTO Address (firstname, lastname, email) VALUES (:firstname, :lastname, :email)";

	public int insert(NamedParameterJdbcTemplate jdbcTemplate, Address address) {

		final KeyHolder holder = new GeneratedKeyHolder();

		jdbcTemplate.update(insert_sql, new AddressParameter(address).getSource(), holder);

		return holder.getKey().intValue();
	}

	public String intertAll(NamedParameterJdbcTemplate jdbcTemplate, AddressList addresses) {

		final KeyHolder holder = new GeneratedKeyHolder();
		
		for(Address address: addresses.getAddresses()) {
			jdbcTemplate.update(insert_sql, new AddressParameter(address).getSource(), holder);
		}
		
		return "ok";
	}

	final static String update_sql = "UPDATE Address SET firstname= :firstname, lastname= :lastname, email= :email WHERE id=:id ";

	public String update(NamedParameterJdbcTemplate jdbcTemplate, int id,Address address) {

		MapSqlParameterSource parameter= new AddressParameter(address).getSource();
		
		parameter.addValue("id", id);
		
		jdbcTemplate.update(update_sql, parameter);

		return "ok";
	}

	final static String delete_sql = "DELETE FROM Address WHERE id=:id ";

	public String delete(NamedParameterJdbcTemplate jdbcTemplate, int id) {

		jdbcTemplate.update(delete_sql, new IdParameter(id).getSource());

		return "ok";
	}

	final static String deleteAll_sql = "DELETE FROM Address";

	public String deleteAll(NamedParameterJdbcTemplate jdbcTemplate) {

		jdbcTemplate.update(deleteAll_sql, new MapSqlParameterSource());

		return "ok";
	}

	final static String select_sql = "SELECT * from Address WHERE id=:id";

	public Address get(NamedParameterJdbcTemplate jdbcTemplate, int id) {

		return jdbcTemplate.queryForObject(select_sql, new IdParameter(id).getSource(), new AddressRowMaper());
	}

	final static String select_all_sql = "SELECT * from Address";

	public AddressList getAll(NamedParameterJdbcTemplate jdbcTemplate) {

		AddressList ret= new AddressList(jdbcTemplate.query(select_all_sql, new AddressRowMaper()));
		
		return ret;
	}

	final static String select_filtered_sql = "SELECT * from Address WHERE 1=1 ";

	public AddressList getFiltered(NamedParameterJdbcTemplate jdbcTemplate, 
			String firstname,
			String lastname,
			String email
			) {

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