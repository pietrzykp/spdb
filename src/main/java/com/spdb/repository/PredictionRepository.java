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

    private static final String GET_DELAYS_QUERY =
            "SELECT sched.way,\n" +
            "    sched.order as order,\n" +
            "    AVG(COALESCE(sched.delay, 0)) AS avg_delay,\n" +
            "    sched.scheduled - DATE_TRUNC('day', sched.scheduled) AS scheduled_departure,\n" +
            "    AVG(COALESCE(sched.realdeparture, sched.scheduled) - DATE_TRUNC('day', COALESCE(sched.realdeparture, sched.scheduled))) as avg_real_depature,\n" +
            "    line_name,\n" +
            "    scheduleed.course_loid AS course_id,\n" +
            "    scheduleed.daytype AS day_type,\n" +
            "    sched.stopid AS stop_id\n" +
            "FROM schedule_history AS sched\n" +
            "JOIN dcourse_schedule AS scheduleed\n" +
            "    ON scheduleed.dcourse_loid = sched.course\n" +
            "WHERE sched.scheduled - DATE_TRUNC('day', sched.scheduled) \n" +
            "BETWEEN \n" +
            "    to_timestamp(?, 'YYYY-DD-MM HH24:MI:SS') - date_trunc('day', to_timestamp(?, 'YYYY-DD-MM HH24:MI:SS'))\n" +
            "AND \n" +
            "    to_timestamp(?, 'YYYY-DD-MM HH24:MI:SS') - date_trunc('day', to_timestamp(?, 'YYYY-DD-MM HH24:MI:SS'))\n" +
            "AND scheduleed.daytype = ?\n" +
            "AND line_name = ?\n" +
            "AND way = ?\n" +
            "GROUP BY sched.way, course_id, sched.order, scheduled_departure, line_name, day_type, stop_id\n" +
            "ORDER BY way,line_name, course_id, scheduled_departure, sched.order,day_type";

    public List<ScheduleDTO> getScheduleInfo(String hourFrom, String hourTo, Long day, String lineName, Long way) {
        return jdbcTemplate.query(GET_DELAYS_QUERY,
                new Object[]{hourFrom, hourFrom, hourTo, hourTo, day, lineName, way}, getRowMapper());
    }

    private RowMapper<ScheduleDTO> getRowMapper() {
        return (resultSet, i) -> ScheduleDTO.builder()
                .way(resultSet.getLong("way"))
                .order(resultSet.getLong("order"))
                .avgDelay(resultSet.getDouble("avg_delay"))
                .scheduleDeparture(resultSet.getString("scheduled_departure"))
                .avgRealDeparture(resultSet.getString("avg_real_depature"))
                .lineName(resultSet.getString("line_name"))
                .courseId(resultSet.getLong("course_id"))
                .dayType(resultSet.getLong("day_type"))
                .stopId(resultSet.getLong("stop_id"))
                .build();
    }
}
