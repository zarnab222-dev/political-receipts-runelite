package dev.shin86.receipts;

import net.runelite.client.config.*;

@ConfigGroup("shin86receipts")
public interface ReceiptsConfig extends Config {
    @ConfigItem(keyName="minimumLoot", name="Minimum loot (GP)", description="Ignore smaller drops. Uses the game's coin value or the GE estimate.", position=0)
    @Range(min=0, max=1000000000)
    default int minimumLoot() { return 50000; }

    @ConfigItem(keyName="cooldownSeconds", name="Receipt cooldown (seconds)", description="Minimum time between receipt updates. Loot still adds to the session total.", position=1)
    @Range(min=10, max=3600)
    default int cooldownSeconds() { return 60; }
}
