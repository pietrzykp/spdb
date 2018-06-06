package com.spdb.repository;

import com.spdb.domain.ScheduleDTO;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class PredictionRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String GET_DELAYS_QUERY = "select stop, stopid from schedule_history limit 2";

    public List<ScheduleDTO> getTest() {
        return jdbcTemplate.query(GET_DELAYS_QUERY, new Object[]{}, getRowMapper());
    }

    private RowMapper<ScheduleDTO> getRowMapper() {
        return (resultSet, i) -> ScheduleDTO.builder()
                .stop(resultSet.getString("stop"))
                .stopid(resultSet.getLong("stopid"))
                .build();
    }
}
