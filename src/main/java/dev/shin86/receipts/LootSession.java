package dev.shin86.receipts;

/** Holds game values only. It never converts GP into real currency. */
final class LootSession {
    private long total;
    private long lastReceipt = Long.MIN_VALUE;
    boolean accept(long gp, long nowMillis, int minimum, int cooldownSeconds) {
        if (gp <= 0) { return false; }
        total = Math.addExact(total, gp);
        if (gp < minimum || (lastReceipt != Long.MIN_VALUE && nowMillis - lastReceipt < cooldownSeconds * 1000L)) { return false; }
        lastReceipt = nowMillis;
        return true;
    }
    long total() { return total; }
    void reset() { total = 0; lastReceipt = Long.MIN_VALUE; }
}
