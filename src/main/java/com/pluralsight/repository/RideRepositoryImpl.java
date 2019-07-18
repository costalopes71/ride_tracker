package com.pluralsight.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.pluralsight.model.Ride;
import com.pluralsight.repository.util.RideRowMapper;

@Repository("rideRepository")
public class RideRepositoryImpl implements RideRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public List<Ride> getRides() {
		
		List<Ride> rides = jdbcTemplate.query("SELECT * FROM ride", new RideRowMapper());
		
		return rides;
	}

	@Override
	public Ride createRide(Ride ride) {
		
		jdbcTemplate.update("INSERT INTO ride (name, duration) VALUES (?, ?)", ride.getName(), ride.getDuration());
		
		/* Outro metodo que existe no SpringJDBC eh o SimpleJdbcInsert, posso fazer essa configuracao
		 uma vez e usar para todos os metodos, eh mais verbosa mas facilita para obter a chave gerada 
		 automaticamente
		 
		SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate);
		
		List<String> columns = new ArrayList<>();
		columns.add("name");
		columns.add("duration");
		
		insert.setTableName("ride");
		insert.setColumnNames(columns);
		
		Map<String, Object> data = new HashMap<>();
		data.put("name", ride.getName());
		data.put("duration", ride.getDuration());
		
		insert.setGeneratedKeyName("id");
		
		Number key = insert.executeAndReturnKey(data);
		
		System.out.println(key);
		*/
		
		return null;
	}
	
}
