package at.ac.tuwien.sepr.assignment.individual.persistence.impl;

import at.ac.tuwien.sepr.assignment.individual.dto.TournamentSearchDto;
import at.ac.tuwien.sepr.assignment.individual.entity.Horse;
import at.ac.tuwien.sepr.assignment.individual.entity.Tournament;
import at.ac.tuwien.sepr.assignment.individual.exception.FatalException;
import at.ac.tuwien.sepr.assignment.individual.exception.NotFoundException;
import at.ac.tuwien.sepr.assignment.individual.persistence.TournamentDao;
import at.ac.tuwien.sepr.assignment.individual.type.Sex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.lang.invoke.MethodHandles;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collection;
import java.util.List;

@Repository
public class TournamentJdbcDao implements TournamentDao {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String TABLE_NAME = "tournament";
    private static final String SQL_SELECT_SEARCH = "SELECT  "
            + "    t.id as \"id\", t.name as \"name\", t.start_date as \"start_date\""
            + "    , t.end_date as \"end_date\""
            + " FROM " + TABLE_NAME + " t"
            + " WHERE (:name IS NULL OR UPPER(t.name) LIKE UPPER('%'||:name||'%'))"
            + "  AND (:startDate IS NULL OR :startDate <= t.start_date)"
            + "  AND (:endDate IS NULL OR :endDate >= t.end_date)"
            + " ORDER BY t.start_date DESC ";
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM " + TABLE_NAME + " WHERE id = ?";

    private static final String SQL_LIMIT_CLAUSE = " LIMIT :limit";
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate jdbcNamed;


    public TournamentJdbcDao(
            NamedParameterJdbcTemplate jdbcNamed,
            JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcNamed = jdbcNamed;
    }
    @Override
    public Collection<Tournament> search(TournamentSearchDto searchParameters) {
        LOG.trace("search({})", searchParameters);
        var query = SQL_SELECT_SEARCH;
        if (searchParameters.limit() != null) {
            query += SQL_LIMIT_CLAUSE;
        }
        var params = new BeanPropertySqlParameterSource(searchParameters);
        params.registerSqlType("sex", Types.VARCHAR);

        return jdbcNamed.query(query, params, this::mapRow);
    }

    @Override
    public Tournament getById(long id) throws NotFoundException {
        LOG.trace("getById({})", id);
        List<Tournament> tournaments;
        tournaments = jdbcTemplate.query(SQL_SELECT_BY_ID, this::mapRow, id);

        if (tournaments.isEmpty()) {
            throw new NotFoundException("No horse with ID %d found".formatted(id));
        }
        if (tournaments.size() > 1) {
            // This should never happen!!
            throw new FatalException("Too many horses with ID %d found".formatted(id));
        }

        return tournaments.get(0);
    }
    private Tournament mapRow(ResultSet result, int rownum) throws SQLException {
        return new Tournament()
                .setId(result.getLong("id"))
                .setName(result.getString("name"))
                .setStartDate(result.getDate("start_date").toLocalDate())
                .setEndDate(result.getDate("end_date").toLocalDate())
                ;
    }
}
