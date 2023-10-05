package at.ac.tuwien.sepr.assignment.individual.persistence.impl;

import at.ac.tuwien.sepr.assignment.individual.dto.HorseDetailDto;
import at.ac.tuwien.sepr.assignment.individual.dto.HorseSearchDto;
import at.ac.tuwien.sepr.assignment.individual.entity.Horse;
import at.ac.tuwien.sepr.assignment.individual.exception.FatalException;
import at.ac.tuwien.sepr.assignment.individual.exception.NotFoundException;
import at.ac.tuwien.sepr.assignment.individual.persistence.HorseDao;
import at.ac.tuwien.sepr.assignment.individual.type.Sex;
import java.lang.invoke.MethodHandles;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class HorseJdbcDao implements HorseDao {
  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private static final String TABLE_NAME = "horse";
  private static final String SQL_SELECT_BY_ID = "SELECT * FROM " + TABLE_NAME + " WHERE id = ?";
  private static final String SQL_SELECT_SEARCH = "SELECT  "
          + "    h.id as \"id\", h.name as \"name\", h.sex as \"sex\", h.date_of_birth as \"date_of_birth\""
          + "    , h.height as \"height\", h.weight as \"weight\", h.breed_id as \"breed_id\""
          + " FROM " + TABLE_NAME + " h LEFT JOIN breed b ON (h.breed_id = b.id)"
          + " WHERE (:name IS NULL OR UPPER(h.name) LIKE UPPER('%'||:name||'%'))"
          + "  AND (:sex IS NULL OR :sex = sex)"
          + "  AND (:bornEarliest IS NULL OR :bornEarliest <= h.date_of_birth)"
          + "  AND (:bornLatest IS NULL OR :bornLatest >= h.date_of_birth)"
          + "  AND (:breed IS NULL OR UPPER(b.name) LIKE UPPER('%'||:breed||'%'))";

  private static final String SQL_LIMIT_CLAUSE = " LIMIT :limit";

  //CRUD
  private static final  String SQL_CREATE = "INSERT INTO "
          + TABLE_NAME
          + " (name, sex, date_of_birth, height, weight, breed_id)"
          + " VALUES (?, ?, ?, ?, ?, ?)";
  private static final String SQL_UPDATE = "UPDATE " + TABLE_NAME
      + " SET name = ?"
      + "  , sex = ?"
      + "  , date_of_birth = ?"
      + "  , height = ?"
      + "  , weight = ?"
      + "  , breed_id = ?"
      + " WHERE id = ?";
  private static final String SQL_DELETE = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";

  private final JdbcTemplate jdbcTemplate;
  private final NamedParameterJdbcTemplate jdbcNamed;


  public HorseJdbcDao(
      NamedParameterJdbcTemplate jdbcNamed,
      JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
    this.jdbcNamed = jdbcNamed;
  }

  @Override
  public Horse getById(long id) throws NotFoundException {
    LOG.trace("getById({})", id);
    List<Horse> horses;
    horses = jdbcTemplate.query(SQL_SELECT_BY_ID, this::mapRow, id);

    if (horses.isEmpty()) {
      throw new NotFoundException("No horse with ID %d found".formatted(id));
    }
    if (horses.size() > 1) {
      // This should never happen!!
      throw new FatalException("Too many horses with ID %d found".formatted(id));
    }

    return horses.get(0);
  }

  @Override
  public Collection<Horse> search(HorseSearchDto searchParameters) {
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
  public Horse create(HorseDetailDto horse) throws FatalException {
    LOG.trace("update({})", horse);
    final KeyHolder keyHolder = new GeneratedKeyHolder();
    if (keyHolder == null) {
      throw new FatalException("ID was not created");
    }

    try {
      jdbcTemplate.update(connection -> {
        PreparedStatement ps = connection.prepareStatement(SQL_CREATE,
                Statement.RETURN_GENERATED_KEYS);

        ps.setString(1, horse.name());
        ps.setString(2, horse.sex().toString());
        ps.setString(3, horse.dateOfBirth().format(DateTimeFormatter.ISO_DATE));
        ps.setFloat(4, horse.height());
        ps.setFloat(5, horse.weight());
        if (horse.breed() != null) {
          ps.setLong(6, horse.breed().id());
        } else {
          ps.setNull(6, -1);
        }

        return ps;

      }, keyHolder);
    } catch (DataAccessException e) {
      new FatalException(e);
    }
    if (horse.breed() != null) {
      return new Horse()
              .setId(keyHolder.getKey().longValue())
              .setName(horse.name())
              .setSex(horse.sex())
              .setDateOfBirth(horse.dateOfBirth())
              .setHeight(horse.height())
              .setWeight(horse.weight())
              .setBreedId(horse.breed().id());
    } else {
      return new Horse()
              .setId(keyHolder.getKey().longValue())
              .setName(horse.name())
              .setSex(horse.sex())
              .setDateOfBirth(horse.dateOfBirth())
              .setHeight(horse.height())
              .setWeight(horse.weight())
              .setBreedId(null);
    }
  }


  @Override
  public Horse update(HorseDetailDto horse) throws NotFoundException {
    LOG.trace("update({})", horse);
    int updated;
    if (horse.breed() != null) {
      updated = jdbcTemplate.update(SQL_UPDATE,
              horse.name(),
              horse.sex().toString(),
              horse.dateOfBirth(),
              horse.height(),
              horse.weight(),
              horse.breed().id(),
              horse.id());
    } else {
      updated = jdbcTemplate.update(SQL_UPDATE,
              horse.name(),
              horse.sex().toString(),
              horse.dateOfBirth(),
              horse.height(),
              horse.weight(),
              null,
              horse.id());
    }
    if (updated == 0) {
      throw new NotFoundException("Could not update horse with ID " + horse.id() + ", because it does not exist");
    }


    if (horse.breed() != null) {
      return new Horse()
              .setId(horse.id())
              .setName(horse.name())
              .setSex(horse.sex())
              .setDateOfBirth(horse.dateOfBirth())
              .setHeight(horse.height())
              .setWeight(horse.weight())
              .setBreedId(horse.breed().id());
    } else {
      return new Horse()
              .setId(horse.id())
              .setName(horse.name())
              .setSex(horse.sex())
              .setDateOfBirth(horse.dateOfBirth())
              .setHeight(horse.height())
              .setWeight(horse.weight())
              .setBreedId(null);
    }
  }

  @Override
  public void delete(long id) throws NotFoundException {
    LOG.trace("delete horse with ID {{}}", id);

    try {
      getById(id);
    } catch (NotFoundException e) {
      throw new NotFoundException("Deleting a Horse that doesn't exist");
    }

    try {
      jdbcTemplate.update(con -> {
        PreparedStatement ps = con.prepareStatement(SQL_DELETE);
        ps.setLong(1, id);
        return ps;
      });
    } catch (DataAccessException e) {
      throw new FatalException("Could not Access database");
    }
  }


  private Horse mapRow(ResultSet result, int rownum) throws SQLException {
    return new Horse()
        .setId(result.getLong("id"))
        .setName(result.getString("name"))
        .setSex(Sex.valueOf(result.getString("sex")))
        .setDateOfBirth(result.getDate("date_of_birth").toLocalDate())
        .setHeight(result.getFloat("height"))
        .setWeight(result.getFloat("weight"))
        .setBreedId(result.getLong("breed_id"))
        ;
  }
}
