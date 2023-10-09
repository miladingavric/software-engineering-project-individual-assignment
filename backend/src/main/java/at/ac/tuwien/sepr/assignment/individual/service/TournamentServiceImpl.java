package at.ac.tuwien.sepr.assignment.individual.service;


import at.ac.tuwien.sepr.assignment.individual.dto.TournamentCreateDto;
import at.ac.tuwien.sepr.assignment.individual.dto.TournamentDetailDto;
import at.ac.tuwien.sepr.assignment.individual.dto.TournamentDetailParticipantDto;
import at.ac.tuwien.sepr.assignment.individual.dto.TournamentListDto;
import at.ac.tuwien.sepr.assignment.individual.dto.TournamentSearchDto;
import at.ac.tuwien.sepr.assignment.individual.dto.TournamentStandingsDto;
import at.ac.tuwien.sepr.assignment.individual.dto.TournamentStandingsTreeDto;
import at.ac.tuwien.sepr.assignment.individual.entity.Horse;
import at.ac.tuwien.sepr.assignment.individual.entity.Match;
import at.ac.tuwien.sepr.assignment.individual.entity.Tournament;
import at.ac.tuwien.sepr.assignment.individual.exception.ConflictException;
import at.ac.tuwien.sepr.assignment.individual.exception.NotFoundException;
import at.ac.tuwien.sepr.assignment.individual.exception.ValidationException;
import at.ac.tuwien.sepr.assignment.individual.mapper.HorseMapper;
import at.ac.tuwien.sepr.assignment.individual.mapper.TournamentMapper;
import at.ac.tuwien.sepr.assignment.individual.persistence.HorseDao;
import at.ac.tuwien.sepr.assignment.individual.persistence.TournamentDao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
public class TournamentServiceImpl implements TournamentService {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  private final TournamentDao dao;
  private final TournamentMapper mapper;
  private final HorseDao horseDao;

  public TournamentServiceImpl(TournamentDao dao, TournamentMapper mapper, HorseMapper horseMapper, HorseDao horseDao) {
    this.dao = dao;
    this.mapper = mapper;
    this.horseDao = horseDao;
  }
  @Override
  public Stream<TournamentListDto> search(TournamentSearchDto searchParameters) {
    var tournaments = dao.search(searchParameters);
    return tournaments.stream().map(tournament -> mapper.entityToListDto(tournament));
  }

  @Override
  public TournamentDetailDto getById(long id) throws NotFoundException {
    LOG.trace("details({})", id);
    Tournament tournament = dao.getById(id);
    return mapper.entityToDetailDto(tournament);
  }

  @Override
  public TournamentStandingsDto getStandings(long id) throws NotFoundException {
    LOG.trace("details({})", id);
    Tournament tournament = dao.getStandings(id);
    List<Match> matches = dao.getMatches(id);
    for (Match match : matches) {
      System.out.println(horseDao.getById(match.getHorseID1()).getName()
                        + " VS "
                        + horseDao.getById(match.getHorseID2()).getName()
                        + " ---- "
                        + horseDao.getById(match.getWinnerHorseID()).getName() + "  WON!");
    }
    Horse[] participants = tournament.getParticipants();
    TournamentDetailParticipantDto Wendy = new TournamentDetailParticipantDto(participants[0].getId(), participants[0].getName(), participants[0].getDateOfBirth(), 1, 3);
    TournamentDetailParticipantDto Hugo = new TournamentDetailParticipantDto(participants[1].getId(), participants[1].getName(), participants[1].getDateOfBirth(), 2, 3);
    TournamentDetailParticipantDto Bella = new TournamentDetailParticipantDto(participants[2].getId(), participants[2].getName(), participants[2].getDateOfBirth(), 3, 2);
    TournamentDetailParticipantDto Thunder = new TournamentDetailParticipantDto(participants[3].getId(), participants[3].getName(), participants[3].getDateOfBirth(), 4, 2);
    TournamentDetailParticipantDto Luna = new TournamentDetailParticipantDto(participants[4].getId(), participants[4].getName(), participants[0].getDateOfBirth(), 5, 1);
    TournamentDetailParticipantDto Apollo = new TournamentDetailParticipantDto(participants[5].getId(), participants[5].getName(), participants[5].getDateOfBirth(), 6, 1);
    TournamentDetailParticipantDto Sophie = new TournamentDetailParticipantDto(participants[6].getId(), participants[6].getName(), participants[6].getDateOfBirth(), 7, 1);
    TournamentDetailParticipantDto Max = new TournamentDetailParticipantDto(participants[7].getId(), participants[7].getName(), participants[7].getDateOfBirth(), 8, 1);
    //___________________________________________________________________//
    //-------------------------------------------------------------------//
    TournamentStandingsTreeDto tree = new TournamentStandingsTreeDto();
    tree.insert(Thunder);
    tree.insert(Thunder);
    tree.insert(Bella);
    tree.insert(Luna);
    tree.insert(Thunder);
    tree.insert(Bella);
    tree.insert(Hugo);
    tree.insert(Apollo);
    tree.insert(Sophie);
    tree.insert(Wendy);
    tree.insert(Max);
    long roundReached = dao.getRounds(id);

    return mapper.entityToStandingsDto(tournament, tree, 1, roundReached);
  }

  @Override
  public TournamentDetailDto create(TournamentCreateDto tournament) throws ValidationException, ConflictException {
    LOG.trace("details({})", tournament);
    Tournament createdTournament = dao.create(tournament);
    return mapper.entityToDetailDto(createdTournament);
  }


  public TournamentStandingsTreeDto createTree (List<Match> matches, Tournament tournament) {
    Horse[] participants = tournament.getParticipants();
    TournamentDetailParticipantDto Wendy = new TournamentDetailParticipantDto(participants[0].getId(), participants[0].getName(), participants[0].getDateOfBirth(), 1, 3);
    TournamentDetailParticipantDto Hugo = new TournamentDetailParticipantDto(participants[1].getId(), participants[1].getName(), participants[1].getDateOfBirth(), 2, 3);
    TournamentDetailParticipantDto Bella = new TournamentDetailParticipantDto(participants[2].getId(), participants[2].getName(), participants[2].getDateOfBirth(), 3, 3);
    TournamentDetailParticipantDto Thunder = new TournamentDetailParticipantDto(participants[3].getId(), participants[3].getName(), participants[3].getDateOfBirth(), 4, 3);
    TournamentDetailParticipantDto Luna = new TournamentDetailParticipantDto(participants[4].getId(), participants[4].getName(), participants[0].getDateOfBirth(), 5, 3);
    TournamentDetailParticipantDto Apollo = new TournamentDetailParticipantDto(participants[5].getId(), participants[5].getName(), participants[5].getDateOfBirth(), 6, 3);
    TournamentDetailParticipantDto Sophie = new TournamentDetailParticipantDto(participants[6].getId(), participants[6].getName(), participants[6].getDateOfBirth(), 7, 3);
    TournamentDetailParticipantDto Max = new TournamentDetailParticipantDto(participants[7].getId(), participants[7].getName(), participants[7].getDateOfBirth(), 8, 3);
    //___________________________________________________________________//
    //-------------------------------------------------------------------//
    TournamentStandingsTreeDto tree = new TournamentStandingsTreeDto();
    tree.insert(Thunder);
    tree.insert(Thunder);
    tree.insert(Bella);
    tree.insert(Luna);
    tree.insert(Thunder);
    tree.insert(Bella);
    tree.insert(Hugo);
    tree.insert(Apollo);
    tree.insert(Sophie);
    tree.insert(Wendy);
    tree.insert(Max);


   return tree;
  }
}
