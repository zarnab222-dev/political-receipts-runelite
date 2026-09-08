package dev.shin86.receipts;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class ReceiptsLauncher {
    public static void main(String[] args) throws Exception {
        ExternalPluginManager.loadBuiltin(ReceiptsPlugin.class);
        RuneLite.main(args);
    }
}
