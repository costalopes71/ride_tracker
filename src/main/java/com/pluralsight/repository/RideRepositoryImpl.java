package com.pluralsight.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
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
		
		//
		// metodo mais facil de insert que nao necessita retornar o objeto do banco (com a chave primaria auto increment setada)
		//
		// jdbcTemplate.update("INSERT INTO ride (name, duration) VALUES (?, ?)", ride.getName(), ride.getDuration());
		
		//
		// metodo de insert usando annonimous class (usado para retornar a chave gerada pelo bd):
		//
		
		// objeto que retorna a chave criada pelo banco de dados 
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		jdbcTemplate.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
			
				PreparedStatement ps = con.prepareStatement(
						"INSERT INTO ride (name, duration) VALUES (?, ?)",
						new String[] {"id"});
				ps.setString(1, ride.getName());
				ps.setInt(2, ride.getDuration());
				
				return ps;
			}
			
		}, keyHolder);
		
		Number id = keyHolder.getKey();
		
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
		
		return getRide(id.intValue());
	}

	@Override
	public Ride getRide(Integer id) {

		Ride ride = jdbcTemplate.queryForObject("SELECT * FROM ride WHERE id = ?", new RideRowMapper(), id);
		
		return ride;
	}

	@Override
	public Ride updateRide(Ride ride) {
		
		jdbcTemplate.update(
				"UPDATE ride SET name = ?, duration = ? WHERE id = ?",
				ride.getName(),
				ride.getDuration(),
				ride.getId());
		
		return ride;
	}

	@Override
	public void updateRides(List<Object[]> pairs) {
		jdbcTemplate.batchUpdate("UPDATE ride SET ride_date = ? WHERE id = ?", pairs);
	}
	
}
