package at.ac.tuwien.sepr.assignment.individual.service;


import at.ac.tuwien.sepr.assignment.individual.dto.TournamentCreateDto;
import at.ac.tuwien.sepr.assignment.individual.dto.TournamentDetailDto;
import at.ac.tuwien.sepr.assignment.individual.dto.TournamentDetailParticipantDto;
import at.ac.tuwien.sepr.assignment.individual.dto.TournamentListDto;
import at.ac.tuwien.sepr.assignment.individual.dto.TournamentSearchDto;
import at.ac.tuwien.sepr.assignment.individual.dto.TournamentStandingsDto;
import at.ac.tuwien.sepr.assignment.individual.dto.TournamentStandingsTreeDto;
import at.ac.tuwien.sepr.assignment.individual.entity.Horse;
import at.ac.tuwien.sepr.assignment.individual.entity.Participant;
import at.ac.tuwien.sepr.assignment.individual.entity.Tournament;
import at.ac.tuwien.sepr.assignment.individual.exception.ConflictException;
import at.ac.tuwien.sepr.assignment.individual.exception.NotFoundException;
import at.ac.tuwien.sepr.assignment.individual.exception.ValidationException;
import at.ac.tuwien.sepr.assignment.individual.mapper.ParticipantMapper;
import at.ac.tuwien.sepr.assignment.individual.mapper.TournamentMapper;
import at.ac.tuwien.sepr.assignment.individual.persistence.HorseDao;
import at.ac.tuwien.sepr.assignment.individual.persistence.ParticipantDao;
import at.ac.tuwien.sepr.assignment.individual.persistence.TournamentDao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class TournamentServiceImpl implements TournamentService {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  private final TournamentDao dao;
  private final TournamentMapper mapper;
  private final ParticipantDao participantDao;
  private final ParticipantMapper participantMapper;
  private final HorseDao horseDao;

  public TournamentServiceImpl(TournamentDao dao,
                               TournamentMapper mapper,
                               HorseDao horseDao,
                               ParticipantDao participantDao,
                               ParticipantMapper participantMapper) {
    this.dao = dao;
    this.mapper = mapper;
    this.participantDao = participantDao;
    this.participantMapper = participantMapper;
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
    Collection<Participant> participantsNew = participantDao.getParticipantsInTournament(id);
    Tournament tournament = dao.getStandings(id);
    TournamentDetailParticipantDto[] participantsDtos = new TournamentDetailParticipantDto[8];
    int i = 0;
    for (Participant participant : participantsNew) {
      participantsDtos[i] = participantMapper.entityToTournamentDetailParticipantDto(participant);
      i++;
    }
    TournamentStandingsTreeDto tree = startCreatingTournamentTree(participantsDtos);
    return mapper.entityToStandingsDto(tournament, tree);
  }

  @Override
  public TournamentStandingsDto generateFirstRound(long id) throws NotFoundException {
    TournamentDetailParticipantDto[] participantsDtos = new TournamentDetailParticipantDto[8];
    Tournament tournament = dao.getStandings(id);
    Participant[] participants = dao.generateFirstRound(id);
    int i = 0;
    for (Participant participant : participants) {
      participantsDtos[i] = participantMapper.entityToTournamentDetailParticipantDtoPlusRoundReached(participant);
      i++;
    }
    TournamentStandingsTreeDto tree = startCreatingTournamentTree(participantsDtos);
    return mapper.entityToStandingsDto(tournament, tree);
  }

  @Override
  public TournamentDetailDto create(TournamentCreateDto tournament) throws ValidationException, ConflictException, NotFoundException {
    LOG.trace("details({})", tournament);
    Tournament createdTournament = dao.create(tournament);
    return mapper.entityToDetailDto(createdTournament);
  }

  @Override
  public TournamentStandingsDto update(TournamentStandingsDto standings) throws ValidationException, ConflictException, NotFoundException {
    System.out.println("id: " + standings.id()
                     + "\nname: " + standings.name()
                     + "\ntree: " + standings.tree()
                     + "\nparticipants: " + standings.participants());
    ArrayList extractedParticipants;
    extractedParticipants = extractParticipants(standings.tree());
    HashMap<Object,Long> newMapper = new HashMap<>();
    for (Object participant : extractedParticipants) {
      Long points = 0L;
      if (newMapper.get(participant) != null) {
        points = newMapper.get(participant);
      }
      newMapper.put(participant,points+1);
      System.out.println(participant);
    }
    TournamentDetailParticipantDto[] participantsToUpdate = new TournamentDetailParticipantDto[8];
    int i = 0;
    for (Object key : newMapper.keySet()) {
      TournamentDetailParticipantDto participant = (TournamentDetailParticipantDto) key;
      participantsToUpdate[i] = new TournamentDetailParticipantDto(
          participant.horseId(),
          participant.name(),
          participant.dateOfBirth(),
          participant.entryNumber(),
          newMapper.get(key));
      System.out.println(participant + ", Value: " + newMapper.get(key));
      i++;
    }
    dao.update(participantsToUpdate, standings.id());
    return new TournamentStandingsDto(standings.id(),standings.name(),participantsToUpdate, standings.tree());
  }


  /**Creating tree section*/
  private TournamentStandingsTreeDto createTournamentTree(TournamentDetailParticipantDto[] arr, int low, int high) {
    if (low == high) {
      if (arr[low] == null || arr[low].roundReached() == 0) {
        return new TournamentStandingsTreeDto();
      }
      return new TournamentStandingsTreeDto(arr[low]);
    }

    int mid = (low + high) / 2;

    TournamentStandingsTreeDto branches0 = createTournamentTree(arr, low, mid);
    TournamentStandingsTreeDto branches1 = createTournamentTree(arr, mid + 1, high);

    // If both children are null, return null
    if (branches0 == null && branches1 == null) {
      return new TournamentStandingsTreeDto(null);
    }

    // If one child is null, return the other child
    if (branches0 == null) {
      return branches1;
    }

    if (branches1 == null) {
      return branches0;
    }


    // If both children are not null, return a new node with the winner
    TournamentDetailParticipantDto winner = getWinner(branches0.thisParticipant(), branches1.thisParticipant());
    TournamentStandingsTreeDto node = new TournamentStandingsTreeDto(winner);
    node.branches()[0] = branches0;
    node.branches()[1] = branches1;

    return node;
  }

  // Function to start creating the tournament tree
  public TournamentStandingsTreeDto startCreatingTournamentTree(TournamentDetailParticipantDto[] arr) {
    TournamentStandingsTreeDto root = createTournamentTree(arr, 0, arr.length - 1);
    return root;
  }

  TournamentDetailParticipantDto getWinner(TournamentDetailParticipantDto participant1, TournamentDetailParticipantDto participant2) {
    // If both participants have reached the same round, the winner is not yet decided
    if (participant1 == null) {
      return null;
    }
    // If participant2 is null, return participant1
    if (participant2 == null) {
      return null;
    }
    if (participant1.roundReached() == participant2.roundReached()) {
      return null;
    }

    // Otherwise, the participant who has reached a higher round is the winner
    return participant1.roundReached() > participant2.roundReached() ? participant1 : participant2;
  }

  /** Extract from list*/
  public static ArrayList<TournamentDetailParticipantDto> extractParticipants(TournamentStandingsTreeDto node) {
    ArrayList<TournamentDetailParticipantDto> participantsList = new ArrayList<>();

    if (node != null) {
      TournamentDetailParticipantDto thisParticipant = node.thisParticipant();

      if (thisParticipant != null) {
        participantsList.add(thisParticipant);
      }

      TournamentStandingsTreeDto[] branches = node.branches();
      if (branches != null) {
        for (TournamentStandingsTreeDto branch : branches) {
          participantsList.addAll(extractParticipants(branch));
        }
      }
    }
    return participantsList;
  }

}
