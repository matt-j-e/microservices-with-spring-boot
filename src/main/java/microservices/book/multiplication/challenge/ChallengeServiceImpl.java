package microservices.book.multiplication.challenge;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import microservices.book.multiplication.user.User;
import microservices.book.multiplication.user.UserRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChallengeServiceImpl implements ChallengeService{

  private final UserRepository userRepository;
  private final ChallengeAttemptRepository attemptRepository;
  private final ChallengeEventPub challengeEventPub;

  @Transactional
  @Override
  public ChallengeAttempt verifyAttempt(ChallengeAttemptDTO attemptDTO) {
    // Check if user exists with that alias, create it if not
    User user = userRepository.findByAlias(attemptDTO.getUserAlias())
            .orElseGet(() -> {
              log.info("Creating new user with alias {}",
                      attemptDTO.getUserAlias());
              return userRepository.save(
                      new User(attemptDTO.getUserAlias())
              );
            });

    // Check if answer is correct
    boolean isCorrect = attemptDTO.getGuess() == attemptDTO.getFactorA() * attemptDTO.getFactorB();

    // We don't use identifiers for now
    // User user = new User(null, attemptDTO.getUserAlias());

    // Builds the domain object. Null id since it will be generated by the Db
    ChallengeAttempt checkedAttempt = new ChallengeAttempt(null,
            user,
            attemptDTO.getFactorA(),
            attemptDTO.getFactorB(),
            attemptDTO.getGuess(),
            isCorrect);

    // Stores the attempt
    ChallengeAttempt storedAttempt = attemptRepository.save(checkedAttempt);

    // Publishes an event to notify potentially interested subscribers
    challengeEventPub.challengeSolved(storedAttempt);

    return storedAttempt;
  }

  @Override
  public List<ChallengeAttempt> getLatestAttemptsForUser(final String userAlias) {
    return attemptRepository.findTop10ByUserAliasOrderByIdDesc(userAlias);
  }
}
