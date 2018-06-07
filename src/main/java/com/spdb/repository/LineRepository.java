package com.spdb.repository;

import com.spdb.domain.LineInfoDTO;
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
public class LineRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String GET_WAYS_QUERY =
            "select s.order, s.stop, s.way from schedule_history s where s.line_name=? " +
                    "group by s.order, s.stop, s.way order by s.way, s.order";

    public List<LineInfoDTO> getScheduleInfo(String line) {
        return jdbcTemplate.query(GET_WAYS_QUERY,
                new Object[]{line}, getRowMapper());
    }

    private RowMapper<LineInfoDTO> getRowMapper() {
        return (resultSet, i) -> LineInfoDTO.builder()
                .order(resultSet.getLong("order"))
                .stop(resultSet.getString("stop"))
                .way(resultSet.getLong("way"))
                .build();
    }



}
