package microservices.book.multiplication.challenge;

import lombok.Value;

@Value
public class ChallengeSolvedDTO {

  long attemptID;
  boolean correct;
  int factorA;
  int factorB;
  long userId;
  String userAlias;

}
