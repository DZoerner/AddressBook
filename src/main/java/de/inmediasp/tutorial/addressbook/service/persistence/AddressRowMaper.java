package de.inmediasp.tutorial.addressbook.service.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import de.inmediasp.tutorial.addressbook.type.Address;

public class AddressRowMaper implements RowMapper<Address>{

	@Override
	public Address mapRow(ResultSet rs, int rowNum) throws SQLException {
		Address ret= new Address();

		ret.setFirstname(rs.getString("firstname"));
		ret.setLastname(rs.getString("lastname"));
		ret.setEmail(rs.getString("email"));

		return ret;
	}

}
