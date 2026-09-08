package dev.shin86.receipts;

import com.google.inject.Provides;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import javax.inject.Inject;
import javax.swing.SwingUtilities;
import net.runelite.api.ItemID;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.ItemStack;
import net.runelite.client.plugins.*;
import net.runelite.client.plugins.loottracker.LootReceived;
import net.runelite.client.plugins.loottracker.LootTrackerPlugin;
import net.runelite.client.ui.*;

@PluginDescriptor(name="Receipts", description="Sourced campaign receipts beside your loot", tags={"loot", "finance"})
@PluginDependency(LootTrackerPlugin.class)
public class ReceiptsPlugin extends Plugin {
    @Inject private ClientToolbar toolbar;
    @Inject private ItemManager itemManager;
    @Inject private ReceiptsConfig config;
    private NavigationButton navigation;
    private ReceiptsPanel panel;
    private final LootSession session = new LootSession();
    private java.util.List<FinanceFact> facts = Collections.emptyList();
    private int nextFact;

    @Provides ReceiptsConfig provideConfig(ConfigManager manager) { return manager.getConfig(ReceiptsConfig.class); }

    @Override protected void startUp() throws Exception {
        facts = FinanceFact.load();
        session.reset(); nextFact = 0;
        // RuneLite calls plugin lifecycle methods on the Swing event thread.
            panel = new ReceiptsPanel(config.minimumLoot());
            if (facts.isEmpty()) { panel.unavailable(); }
            BufferedImage icon = new BufferedImage(24, 24, BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics = icon.createGraphics();
            graphics.setColor(new Color(239, 237, 221));
            for (int x = 3; x < 22; x += 3) { graphics.fillRect(x, 4, x % 2 + 1, 16); }
            graphics.dispose();
            navigation = NavigationButton.builder().tooltip("Receipts").icon(icon).priority(8).panel(panel).build();
            toolbar.addNavigation(navigation);
    }

    @Override protected void shutDown() {
        toolbar.removeNavigation(navigation);
        session.reset(); facts = Collections.emptyList();
    }

    @Subscribe public void onLootReceived(LootReceived event) {
        long value = 0;
        for (ItemStack item : event.getItems()) {
            int price = item.getId() == ItemID.COINS_995 ? 1 : itemManager.getItemPrice(item.getId());
            value += (long) Math.max(0, price) * Math.max(0, item.getQuantity());
        }
        if (!session.accept(value, System.currentTimeMillis(), config.minimumLoot(), config.cooldownSeconds()) || facts.isEmpty()) { return; }
        FinanceFact fact = facts.get(nextFact++ % facts.size());
        long receiptValue = value, total = session.total();
        String encounter = event.getName();
        ReceiptsPanel target = panel;
        SwingUtilities.invokeLater(() -> target.showReceipt(encounter, receiptValue, total, fact));
    }
}
