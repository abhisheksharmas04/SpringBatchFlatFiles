package com.spring.batch.flat.flies.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.spring.batch.flat.flies.models.ExamResult;

// Step 4 Develop Row Mapper

/*By developing row mapper either as seprate class or inner class or annonsms inner class
or lambda expression inner class we can convert each record coming to rs object
to given model class object*/

public class ExamResultRowMapper implements RowMapper<ExamResult> {

	@Override
	public ExamResult mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		return new ExamResult(rs.getInt(1),
				rs.getDate(2),rs.getDouble(3),rs.getInt(4));
	}

}
