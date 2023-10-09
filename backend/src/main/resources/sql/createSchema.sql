CREATE TABLE IF NOT EXISTS breed
(
  id BIGINT PRIMARY KEY,
  name VARCHAR(32) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS horse
(
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  -- Instead of an ENUM (H2 specific) this could also be done with a character string type and a check constraint.
  sex ENUM ('MALE', 'FEMALE') NOT NULL,
  date_of_birth DATE NOT NULL,
  height NUMERIC(4,2),
  weight NUMERIC(7,2),
  breed_id BIGINT REFERENCES breed(id)
);

CREATE TABLE IF NOT EXISTS tournament
(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    winnerHorseID INT,
    FOREIGN KEY (winnerHorseID) REFERENCES horse(id) ON DELETE cascade
);

CREATE TABLE IF NOT EXISTS tournamentParticipants
(
    tournamentID INT,
    horseID INT,
    PRIMARY KEY (tournamentID, horseID),
    FOREIGN KEY (tournamentID) REFERENCES tournament(id) ON DELETE CASCADE,
    FOREIGN KEY (horseID) REFERENCES horse(id) ON DELETE cascade
);


CREATE TABLE IF NOT EXISTS tournamentMatch
(
    matchID INT PRIMARY KEY AUTO_INCREMENT,
    tournamentID INT,
    matchRound INT,
    horse1ID INT,
    horse2ID INT,
    winnerHorseID INT,
    FOREIGN KEY (tournamentID) REFERENCES tournament(id),
    FOREIGN KEY (horse1ID) REFERENCES horse(id),
    FOREIGN KEY (horse2ID) REFERENCES horse(id),
    FOREIGN KEY (winnerHorseID) REFERENCES horse(id)
);
