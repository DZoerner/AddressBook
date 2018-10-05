package de.inmediasp.tutorial.addressbook.service.persistence;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

public class IdParameter {
	private int id;
	
	public IdParameter(int id) {
		this.id= id;
	}
	
	public MapSqlParameterSource getSource(){
		MapSqlParameterSource ret= new MapSqlParameterSource();
		
		ret.addValue("id", id);
		
		return ret;
	}
}