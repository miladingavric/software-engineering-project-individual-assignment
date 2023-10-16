package at.ac.tuwien.sepr.assignment.individual.persistence.impl;

import at.ac.tuwien.sepr.assignment.individual.dto.HorseDetailDto;
import at.ac.tuwien.sepr.assignment.individual.dto.HorseSearchDto;
import at.ac.tuwien.sepr.assignment.individual.entity.Horse;
import at.ac.tuwien.sepr.assignment.individual.entity.Participant;
import at.ac.tuwien.sepr.assignment.individual.entity.Tournament;
import at.ac.tuwien.sepr.assignment.individual.exception.FatalException;
import at.ac.tuwien.sepr.assignment.individual.exception.NotFoundException;
import at.ac.tuwien.sepr.assignment.individual.persistence.ParticipantDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.lang.invoke.MethodHandles;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
@Repository
public class ParticipantJdbcDao implements ParticipantDao {
  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  private static final String TABLE_NAME = "tournamentparticipants";
  private static final String TABLE_NAME_PARTICIPANTS = "tournamentparticipants";
  private static final String TABLE_NAME_HORSE = "horse";

  private static final String SQL_SELECT_PARTICIPANTS_BY_TOURNAMENT_ID = "SELECT "
      + " h.id AS \"id\", h.name AS \"name\", h.date_of_birth AS \"date_of_birth\", "
      + "tp.entryNumber AS \"entryNumber\", tp.roundReached AS \"roundReached\""
      + " FROM " + TABLE_NAME_HORSE + " h "
      + " JOIN " + TABLE_NAME + " tp "
      + " ON tp.horseID = h.id "
      + " WHERE tp.tournamentID = ?"
      + " ORDER BY tp.entryNumber ASC NULLS LAST;";

  private final JdbcTemplate jdbcTemplate;
  private final NamedParameterJdbcTemplate jdbcNamed;

  public ParticipantJdbcDao(
      NamedParameterJdbcTemplate jdbcNamed,
      JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
    this.jdbcNamed = jdbcNamed;
  }
  @Override
  public Collection<Participant> search(HorseSearchDto searchParameters) {
    return null;
  }

  @Override
  public Participant create(HorseDetailDto horse) throws FatalException {
    return null;
  }

  @Override
  public Participant update(HorseDetailDto horse) throws NotFoundException {
    return null;
  }

  @Override
  public void delete(long id) throws NotFoundException {

  }

  @Override
  public Participant getById(long id) throws NotFoundException {
    return null;
  }

  @Override
  public Collection<Participant> getParticipantsInTournament(long id) throws NotFoundException {
    LOG.trace("getById({})", id);
    Collection<Participant> participants;

    participants = jdbcTemplate.query(SQL_SELECT_PARTICIPANTS_BY_TOURNAMENT_ID, this::mapRowParticipant, id);
    return participants;
  }

  private Participant mapRowParticipant(ResultSet result, int rownum) throws SQLException {
    return new Participant()
        .setId(result.getLong("id"))
        .setName(result.getString("name"))
        .setDateOfBirth(result.getDate("date_of_birth").toLocalDate())
        .setEntryNumber(result.getLong("entryNumber"))
        .setRoundReached(result.getLong("roundReached"))
        ;
  }
}
