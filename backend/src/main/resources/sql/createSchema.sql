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
    id INT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS tournamentParticipants
(
    tournamentID INT,
    horseID INT,
    PRIMARY KEY (tournamentID, horseID),
    FOREIGN KEY (tournamentID) REFERENCES tournament(id),
    FOREIGN KEY (horseID) REFERENCES horse(id)
);

CREATE TABLE IF NOT EXISTS tournamentResult
(
    tournamentID INT PRIMARY KEY,
    winnerHorseID INT,
    FOREIGN KEY (tournamentID) REFERENCES tournament(id),
    FOREIGN KEY (winnerHorseID) REFERENCES horse(id)
);

CREATE TABLE IF NOT EXISTS tournamentMatch
(
    matchID INT PRIMARY KEY,
    tournamentID INT,
    matchRound INT,
    horse1ID INT,
    horse2ID INT,
    FOREIGN KEY (tournamentID) REFERENCES tournament(id),
    FOREIGN KEY (horse1ID) REFERENCES horse(id),
    FOREIGN KEY (horse2ID) REFERENCES horse(id)
);

CREATE TABLE IF NOT EXISTS tournamentMatchResult
(
    matchID INT PRIMARY KEY,
    winnerHorseID INT,
    FOREIGN KEY (matchID) REFERENCES tournamentMatch(matchID),
    FOREIGN KEY (winnerHorseID) REFERENCES horse(id)
);