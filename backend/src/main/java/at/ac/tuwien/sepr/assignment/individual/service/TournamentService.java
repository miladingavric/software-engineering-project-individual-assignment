package at.ac.tuwien.sepr.assignment.individual.service;

import at.ac.tuwien.sepr.assignment.individual.dto.*;
import at.ac.tuwien.sepr.assignment.individual.entity.Tournament;
import at.ac.tuwien.sepr.assignment.individual.exception.ConflictException;
import at.ac.tuwien.sepr.assignment.individual.exception.NotFoundException;
import at.ac.tuwien.sepr.assignment.individual.exception.ValidationException;

import java.util.stream.Stream;

public interface TournamentService {

    /**
     * Search for tournaments in the persistent data store matching all provided fields.
     * The name is considered a match, if the search string is a substring of the field in horse.
     *
     * @param searchParameters the search parameters to use in filtering.
     * @return the horses where the given fields match.
     */
    Stream<TournamentListDto> search(TournamentSearchDto searchParameters);

    /**
     * Creates new horse  {@code horse}
     * with the data given in {@code horse}
     * in the persistent data store.
     *
     * @param horse the horse to create
     * @return created horse
     * @throws ValidationException if the update data given for the horse is in itself incorrect (no name, name too long …)
     * @throws ConflictException if the update data given for the horse is in conflict the data currently in the system (breed does not exist, …)

    HorseDetailDto create(HorseDetailDto horse) throws ValidationException, ConflictException;*/




    /**
     * Get the horse with given ID, with more detail information.
     * This includes the owner of the horse, and its parents.
     * The parents of the parents are not included.
     *
     * @param id the ID of the horse to get
     * @return the horse with ID {@code id}
     * @throws NotFoundException if the horse with the given ID does not exist in the persistent data store
     */
    TournamentDetailDto getById(long id) throws NotFoundException;
}
