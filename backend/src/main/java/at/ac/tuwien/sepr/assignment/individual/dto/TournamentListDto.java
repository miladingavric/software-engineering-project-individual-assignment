package at.ac.tuwien.sepr.assignment.individual.dto;

import at.ac.tuwien.sepr.assignment.individual.entity.Horse;
import at.ac.tuwien.sepr.assignment.individual.type.Sex;

import java.time.LocalDate;

public record TournamentListDto(
        Long id,
        String name,
        LocalDate startDate,
        LocalDate endDate
) {
}
