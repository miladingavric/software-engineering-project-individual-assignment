package at.ac.tuwien.sepr.assignment.individual.service;

import at.ac.tuwien.sepr.assignment.individual.dto.HorseDetailDto;
import at.ac.tuwien.sepr.assignment.individual.dto.HorseListDto;
import at.ac.tuwien.sepr.assignment.individual.dto.HorseSearchDto;
import at.ac.tuwien.sepr.assignment.individual.exception.ConflictException;
import at.ac.tuwien.sepr.assignment.individual.exception.NotFoundException;
import at.ac.tuwien.sepr.assignment.individual.exception.ValidationException;
import java.util.stream.Stream;

/**
 * Service for working with horses.
 */
public interface HorseService {
  /**
   * Search for horses in the persistent data store matching all provided fields.
   * The name is considered a match, if the search string is a substring of the field in horse.
   *
   * @param searchParameters the search parameters to use in filtering.
   * @return the horses where the given fields match.
   */
  Stream<HorseListDto> search(HorseSearchDto searchParameters);

  /**
   * Creates new horse  {@code horse}
   * with the data given in {@code horse}
   * in the persistent data store.
   *
   * @param horse the horse to create
   * @return created horse
   * @throws NotFoundException if the horse with given ID does not exist in the persistent data store
   * @throws ValidationException if the update data given for the horse is in itself incorrect (no name, name too long …)
   * @throws ConflictException if the update data given for the horse is in conflict the data currently in the system (breed does not exist, …)
   */
  HorseDetailDto create(HorseDetailDto horse) throws ValidationException, ConflictException;

  /**
   * Updates the horse with the ID given in {@code horse}
   * with the data given in {@code horse}
   * in the persistent data store.
   *
   * @param horse the horse to update
   * @return he updated horse
   * @throws NotFoundException if the horse with given ID does not exist in the persistent data store
   * @throws ValidationException if the update data given for the horse is in itself incorrect (no name, name too long …)
   * @throws ConflictException if the update data given for the horse is in conflict the data currently in the system (breed does not exist, …)
   */
  HorseDetailDto update(HorseDetailDto horse) throws NotFoundException, ValidationException, ConflictException;

  /**
   * Updates the horse with the ID given in {@code horse}
   * with the data given in {@code horse}
   * in the persistent data store.
   *
   * @param id of the horse that needs to be deleted
   * @throws NotFoundException if the horse with given ID does not exist in the persistent data store
   */
  void delete(long id) throws NotFoundException;


  /**
   * Get the horse with given ID, with more detail information.
   * This includes the owner of the horse, and its parents.
   * The parents of the parents are not included.
   *
   * @param id the ID of the horse to get
   * @return the horse with ID {@code id}
   * @throws NotFoundException if the horse with the given ID does not exist in the persistent data store
   */
  HorseDetailDto getById(long id) throws NotFoundException;
}
