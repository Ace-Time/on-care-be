package org.ateam.oncare.matching.query;

import org.ateam.oncare.matching.query.service.MatchingQueryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("dev")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MatchingQueryServiceTest {

    @Autowired
    private MatchingQueryService matchingQueryService;

    @Test
    void selectFinalCandidateCareWorkerIds() {
        matchingQueryService.selectFinalCandidateCareWorkerIds(2L);
    }
}