package dev.shin86.receipts;
import org.junit.Test;
import static org.junit.Assert.*;

public class LootSessionTest {
    @Test public void cooldownSuppressesCardsButPreservesSessionLoot() {
        LootSession session = new LootSession();
        assertTrue(session.accept(78000, 0, 50000, 60));
        assertFalse(session.accept(90000, 1000, 50000, 60));
        assertFalse(session.accept(1000, 60000, 50000, 60));
        assertTrue(session.accept(100000, 60000, 50000, 60));
        assertEquals(269000, session.total());
        session.reset(); assertEquals(0, session.total());
        assertTrue(session.accept(78000, 60001, 50000, 60));
    }
}
