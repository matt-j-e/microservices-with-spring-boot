package microservices.book.multiplication.challenge;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * The class provides a REST API to POST the attempts from users
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/attempts")
public class ChallengeAttemptController {

  private final ChallengeService challengeService;

  @PostMapping
  ResponseEntity<ChallengeAttempt> postResult(@RequestBody @Valid ChallengeAttemptDTO challengeAttemptDTO) {
    return ResponseEntity.ok(challengeService.verifyAttempt(challengeAttemptDTO));
  }

  @GetMapping
  ResponseEntity<List<ChallengeAttempt>> getLatestAttempts(@RequestParam("alias") String alias) {
    return ResponseEntity.ok(challengeService.getLatestAttemptsForUser(alias));
  }
}
