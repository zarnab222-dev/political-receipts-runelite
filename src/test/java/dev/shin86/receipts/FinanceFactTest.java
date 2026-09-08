package dev.shin86.receipts;
import org.junit.Test;
import java.util.List;
import static org.junit.Assert.*;

public class FinanceFactTest {
    @Test public void loadsOnlyAvailableFactsWithOfficialSources() throws Exception {
        List<FinanceFact> facts = FinanceFact.load();
        assertFalse(facts.isEmpty());
        for (FinanceFact fact : facts) {
            assertTrue(fact.source.startsWith("https://www.fec.gov/data/candidate/"));
            assertTrue(Double.isFinite(fact.receipts));
            assertFalse(fact.period.contains("null"));
        }
    }
}
