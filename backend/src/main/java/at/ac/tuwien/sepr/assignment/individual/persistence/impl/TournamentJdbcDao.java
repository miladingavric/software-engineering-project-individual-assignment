package at.ac.tuwien.sepr.assignment.individual.persistence.impl;

import at.ac.tuwien.sepr.assignment.individual.dto.HorseSelectionDto;
import at.ac.tuwien.sepr.assignment.individual.dto.TournamentCreateDto;
import at.ac.tuwien.sepr.assignment.individual.dto.TournamentDetailParticipantDto;
import at.ac.tuwien.sepr.assignment.individual.dto.TournamentSearchDto;
import at.ac.tuwien.sepr.assignment.individual.entity.Horse;
import at.ac.tuwien.sepr.assignment.individual.entity.Match;
import at.ac.tuwien.sepr.assignment.individual.entity.Participant;
import at.ac.tuwien.sepr.assignment.individual.entity.Tournament;
import at.ac.tuwien.sepr.assignment.individual.exception.FatalException;
import at.ac.tuwien.sepr.assignment.individual.exception.NotFoundException;
import at.ac.tuwien.sepr.assignment.individual.persistence.TournamentDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.lang.invoke.MethodHandles;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Repository
public class TournamentJdbcDao implements TournamentDao {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  private static final String TABLE_NAME = "tournament";
  private static final String TABLE_NAME_PARTICIPANTS = "tournamentparticipants";
  private static final String TABLE_NAME_HORSE = "horse";
  private static final String SQL_SELECT_SEARCH = "SELECT  "
      + "    t.id as \"id\", t.name as \"name\", t.start_date as \"start_date\""
      + "    , t.end_date as \"end_date\""
      + " FROM " + TABLE_NAME + " t"
      + " WHERE (:name IS NULL OR UPPER(t.name) LIKE UPPER('%'||:name||'%'))"
      + "  AND (:startDate IS NULL OR :startDate <= t.start_date)"
      + "  AND (:endDate IS NULL OR :endDate >= t.end_date)"
      + " ORDER BY t.start_date DESC ";
  private static final String SQL_SELECT_BY_ID = "SELECT * FROM " + TABLE_NAME + " WHERE id = ?";
  private static final String SQL_CREATE = "INSERT INTO "
      + TABLE_NAME
      + " (name, start_date, end_date)"
      + " VALUES (?, ?, ?)";
  private static final String SQL_CREATE_PARTICIPANTS = "INSERT INTO "
      + TABLE_NAME_PARTICIPANTS
      + " (tournamentId, horseId)"
      + " VALUES (?, ?)";
  private static final String SQL_SELECT_PARTICIPANTS_BY_TOURNAMENT_ID = "SELECT "
      + " h.id AS \"id\", h.name AS \"name\", h.date_of_birth AS \"date_of_birth\", "
      + "tp.entryNumber AS \"entryNumber\", tp.roundReached AS \"roundReached\""
      + " FROM " + TABLE_NAME_HORSE + " h "
      + " JOIN " + TABLE_NAME_PARTICIPANTS + " tp "
      + " ON tp.horseID = h.id "
      + " WHERE tp.tournamentID = ?";
  private static final String SQL_SELECT_PARTICIPANTS_IN_TOURNAMENT_ORDERED_BY_NAME = "SELECT "
      + " h.id AS \"id\", h.name AS \"name\", h.date_of_birth AS \"date_of_birth\", "
      + "tp.entryNumber AS \"entryNumber\", tp.roundReached AS \"roundReached\""
      + " FROM " + TABLE_NAME_HORSE + " h "
      + " JOIN " + TABLE_NAME_PARTICIPANTS + " tp "
      + " ON tp.horseID = h.id "
      + " WHERE tp.tournamentID = ?"
      + " ORDER BY tp.entryNumber ASC NULLS LAST;";
  private static final String SQL_SELECT_TOURNAMENT = "SELECT * FROM "
      + TABLE_NAME
      + " WHERE id = ?";
  private static final String SQL_MATCH_ROUND_COUNTER = "SELECT MAX(matchround) AS highest_round "
      + " FROM tournamentmatch "
      + " WHERE tournamentid = ?";
  private static final String SQL_GET_MATCHES = "SELECT *"
      + " FROM  tournamentmatch"
      + " WHERE tournamentid = ?";
  private static final String SQL_GET_MATCH_WINNER = "SELECT winnerHorseID"
      + " FROM  tournamentmatch"
      + " WHERE tournamentid = ? AND horse1id = ? AND horse2id = ?";
  private static final String SQL_GET_POINTS_IN_LAST_YEAR = "SELECT "
      + " SUM( "
      + " CASE "
      + " WHEN tp.roundReached = 4 THEN 5 "
      + " WHEN tp.roundReached = 3 THEN 3 "
      + " WHEN tp.roundReached = 2 THEN 1 "
      + " ELSE 0 "
      + " END"
      + " ) AS \"total_points\""
      + " FROM "
      + " tournamentparticipants tp "
      + " JOIN "
      + " tournament t ON tp.tournamentid = t.id "
      + " WHERE"
      + " tp.horseId = ? "
      + " AND t.start_date >= DATE_TRUNC('year', CURRENT_DATE - INTERVAL '1' YEAR)";
  private static final String SQL_UPDATE = "UPDATE tournamentparticipants"
      + " SET entryNumber = ?"
      + "  , roundReached = ?"
      + " WHERE horseId = ?"
      + " AND tournamentid = ?";

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

    tournaments = jdbcTemplate.query(SQL_SELECT_TOURNAMENT, this::mapRow, id);

    if (tournaments.isEmpty()) {
      throw new NotFoundException("No horse with ID %d found".formatted(id));
    }
    if (tournaments.size() > 1) {
      // This should never happen!!
      throw new FatalException("Too many horses with ID %d found".formatted(id));
    }

    return tournaments.get(0);
  }

  @Override
  public Tournament getStandings(long id) throws NotFoundException {
    LOG.trace("getStandings({})", id);
    List<Participant> participants;
    participants = jdbcTemplate.query(SQL_SELECT_PARTICIPANTS_BY_TOURNAMENT_ID, this::mapRowParticipant, id);
    Participant[] horseParticipants = new Participant[participants.size()];
    int increment = 0;
    for (Participant participant : participants) {
      horseParticipants[increment] = participant;
      increment++;
    }
    Long newId = getById(id).getId();
    String name = getById(id).getName();
    return new Tournament()
        .setId(newId)
        .setName(name)
        .setParticipants(horseParticipants);
  }

  @Override
  public Participant[] generateFirstRound(long id) throws NotFoundException {
    LOG.trace("getFirstRound({})", id);
    List<Participant> participants;
    participants = jdbcTemplate.query(SQL_SELECT_PARTICIPANTS_IN_TOURNAMENT_ORDERED_BY_NAME, this::mapRowParticipant, id);
    HashMap<Participant, Long> horseAndPoints = new HashMap<>();
    for (Participant participant : participants) {
      horseAndPoints.put(participant, getPoints(participant.getId()));
    }
    LinkedHashMap<Participant, Long> sortedMap = horseAndPoints.entrySet()
        .stream()
        .sorted(Map.Entry.<Participant, Long>comparingByValue()
            .reversed()
            .thenComparing(Map.Entry.<Participant, Long>comparingByKey()
                .reversed()))
        .collect(Collectors.toMap(
            Map.Entry::getKey,
            Map.Entry::getValue,
            (e1, e2) -> e1,
            LinkedHashMap::new
        ));
    Participant[] horseParticipants = sortedMap.keySet().toArray(new Participant[0]);
    return horseParticipants;
  }

  @Override
  public Tournament create(TournamentCreateDto tournament) throws FatalException {
    LOG.trace("update({})", tournament);
    final KeyHolder keyHolderTournaments = new GeneratedKeyHolder();
    if (keyHolderTournaments == null) {
      throw new FatalException("ID was not created");
    }

    try {
      jdbcTemplate.update(connection -> {
        PreparedStatement ps = connection.prepareStatement(SQL_CREATE,
            Statement.RETURN_GENERATED_KEYS);

        ps.setString(1, tournament.name());
        ps.setString(2, tournament.startDate().format(DateTimeFormatter.ISO_DATE));
        ps.setString(3, tournament.endDate().format(DateTimeFormatter.ISO_DATE));
        return ps;

      }, keyHolderTournaments);
    } catch (DataAccessException e) {
      new FatalException(e);
    }
    Long tournamentKey = keyHolderTournaments.getKey().longValue();
    Object[] participants = Arrays.stream(tournament.participants()).toArray();
    ArrayList<Participant> horses = new ArrayList<>();
    for (Object horse : participants) {
      HorseSelectionDto temp = (HorseSelectionDto) horse;
      horses.add(
          new Participant()
              .setId(temp.id())
              .setName(temp.name())
              .setDateOfBirth(temp.dateOfBirth())
      );
      try {
        jdbcTemplate.update(connection -> {
          PreparedStatement ps = connection.prepareStatement(SQL_CREATE_PARTICIPANTS);

          ps.setLong(1, tournamentKey);
          ps.setLong(2, temp.id());
          return ps;
        });
      } catch (DataAccessException e) {
        new FatalException(e);
      }
    }

    Participant[] returnHorses = new Participant[horses.size()];
    int increment = 0;
    for (Participant horse : horses) {
      returnHorses[increment] = horse;
      increment++;
    }

    return new Tournament()
        .setId(keyHolderTournaments.getKey().longValue())
        .setName(tournament.name())
        .setStartDate(tournament.startDate())
        .setEndDate(tournament.endDate())
        .setParticipants(returnHorses);
  }

  @Override
  public Tournament update(TournamentDetailParticipantDto[] participants, Long tournamentID) throws FatalException, NotFoundException {
    int updated;
    for (TournamentDetailParticipantDto participant : participants) {
      updated = jdbcTemplate.update(SQL_UPDATE,
          participant.entryNumber(),
          participant.roundReached(),
          participant.horseId(),
          tournamentID);
      if (updated == 0) {
        throw new NotFoundException("Could not update horse with ID " + participant.horseId() + ", because it does not exist");
      }
    }
    return null;
  }

  @Override
  public long getRounds(long id) throws NotFoundException {
    List<Long> rounds = jdbcTemplate.query(SQL_MATCH_ROUND_COUNTER, this::mapRowCounter, id);
    return rounds.get(0);
  }

  private long getPoints(long id) throws NotFoundException {
    List<Long> rounds = jdbcTemplate.query(SQL_GET_POINTS_IN_LAST_YEAR, this::mapPointCounter, id);
    return rounds.get(0);
  }

  @Override
  public List<Match> getMatches(long id) throws NotFoundException {
    List<Match> matches = jdbcTemplate.query(SQL_GET_MATCHES, this::mapAllMatches, id);
    return matches;
  }

  private Tournament mapRow(ResultSet result, int rownum) throws SQLException {
    return new Tournament()
        .setId(result.getLong("id"))
        .setName(result.getString("name"))
        .setStartDate(result.getDate("start_date").toLocalDate())
        .setEndDate(result.getDate("end_date").toLocalDate())
        ;
  }

  private Horse mapRowHorse(ResultSet result, int rownum) throws SQLException {
    return new Horse()
        .setId(result.getLong("id"))
        .setName(result.getString("name"))
        .setDateOfBirth(result.getDate("date_of_birth").toLocalDate())
        ;
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

  private long mapRowCounter(ResultSet result, int rownum) throws SQLException {
    return result.getLong("highest_round");
  }

  private long mapPointCounter(ResultSet result, int rownum) throws SQLException {
    return result.getLong("total_points");
  }

  private Match mapAllMatches(ResultSet result, int rownum) throws SQLException {
    return new Match()
        .setId(result.getLong("matchID"))
        .setTournamentID(result.getLong("tournamentID"))
        .setMatchRound(result.getLong("matchRound"))
        .setHorseID1(result.getLong("horse1ID"))
        .setHorseID2(result.getLong("horse2ID"))
        .setWinnerHorseID(result.getLong("winnerHorseID"))
        ;

  }

}
