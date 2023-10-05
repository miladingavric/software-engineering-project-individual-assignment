package at.ac.tuwien.sepr.assignment.individual.rest;
import at.ac.tuwien.sepr.assignment.individual.dto.*;
import at.ac.tuwien.sepr.assignment.individual.entity.Tournament;
import at.ac.tuwien.sepr.assignment.individual.exception.ConflictException;
import at.ac.tuwien.sepr.assignment.individual.exception.NotFoundException;
import at.ac.tuwien.sepr.assignment.individual.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import at.ac.tuwien.sepr.assignment.individual.service.TournamentService;

import java.lang.invoke.MethodHandles;
import java.util.stream.Stream;
@RestController
@RequestMapping(path = TournamentsEndpoint.BASE_PATH)
public class TournamentsEndpoint {


    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    static final String BASE_PATH = "/tournaments";

    private final TournamentService service;

    public TournamentsEndpoint(TournamentService service) {
        this.service = service;
    }

    @GetMapping
    public Stream<TournamentListDto> searchTournaments(TournamentSearchDto searchParameters) {
        LOG.info("GET " + BASE_PATH);
        LOG.debug("request parameters: {}", searchParameters);
        return service.search(searchParameters);
    }

    @GetMapping("{id}")
    public TournamentDetailDto getById(@PathVariable long id) {
        LOG.info("GET " + BASE_PATH + "/{}", id);
        try {
            return service.getById(id);
        } catch (NotFoundException e) {
            HttpStatus status = HttpStatus.NOT_FOUND;
            logClientError(status, "Tourmanet to get details of not found", e);
            throw new ResponseStatusException(status, e.getMessage(), e);
        }
    }
/*
    @PostMapping()
    public HorseDetailDto create(@RequestBody HorseDetailDto toCreate) throws ValidationException, ConflictException {
        LOG.info("POST " + BASE_PATH + "/{}", toCreate);
        LOG.debug("Body of request:\n{}", toCreate);
        return service.create(toCreate);
    }

    @GetMapping
    public Stream<HorseListDto> searchHorses(HorseSearchDto searchParameters) {
        LOG.info("GET " + BASE_PATH);
        LOG.debug("request parameters: {}", searchParameters);
        return service.search(searchParameters);
    }

    @GetMapping("{id}")
    public HorseDetailDto getById(@PathVariable long id) {
        LOG.info("GET " + BASE_PATH + "/{}", id);
        try {
            return service.getById(id);
        } catch (NotFoundException e) {
            HttpStatus status = HttpStatus.NOT_FOUND;
            logClientError(status, "Horse to get details of not found", e);
            throw new ResponseStatusException(status, e.getMessage(), e);
        }
    }


    @PutMapping("{id}")
    public HorseDetailDto update(@PathVariable long id, @RequestBody HorseDetailDto toUpdate) throws ValidationException, ConflictException {
        LOG.info("PUT " + BASE_PATH + "/{}", toUpdate);
        LOG.debug("Body of request:\n{}", toUpdate);
        try {
            return service.update(toUpdate.withId(id));
        } catch (NotFoundException e) {
            HttpStatus status = HttpStatus.NOT_FOUND;
            logClientError(status, "Horse to update not found", e);
            throw new ResponseStatusException(status, e.getMessage(), e);
        }
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable long id) throws ValidationException, ConflictException, NotFoundException {
        LOG.info("DELETE " + BASE_PATH + "/{}", id);
        LOG.debug("We need to delete horse with id: {}", id);
        service.delete(id);
    }

*/
    private void logClientError(HttpStatus status, String message, Exception e) {
        LOG.warn("{} {}: {}: {}", status.value(), message, e.getClass().getSimpleName(), e.getMessage());
    }
}
