package at.ac.tuwien.sepr.assignment.individual.service;

import at.ac.tuwien.sepr.assignment.individual.dto.HorseSearchDto;
import at.ac.tuwien.sepr.assignment.individual.dto.TournamentDetailDto;
import at.ac.tuwien.sepr.assignment.individual.dto.TournamentListDto;
import at.ac.tuwien.sepr.assignment.individual.dto.TournamentSearchDto;
import at.ac.tuwien.sepr.assignment.individual.entity.Horse;
import at.ac.tuwien.sepr.assignment.individual.entity.Tournament;
import at.ac.tuwien.sepr.assignment.individual.exception.NotFoundException;
import at.ac.tuwien.sepr.assignment.individual.mapper.HorseMapper;
import at.ac.tuwien.sepr.assignment.individual.mapper.TournamentMapper;
import at.ac.tuwien.sepr.assignment.individual.persistence.HorseDao;
import at.ac.tuwien.sepr.assignment.individual.persistence.TournamentDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class TournamentServiceImpl implements TournamentService{

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final TournamentDao dao;
    private final TournamentMapper mapper;

    public TournamentServiceImpl(TournamentDao dao, TournamentMapper mapper) {
        this.dao = dao;
        this.mapper = mapper;
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
}
