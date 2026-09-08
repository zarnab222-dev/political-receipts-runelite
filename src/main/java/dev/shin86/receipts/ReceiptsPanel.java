package dev.shin86.receipts;

import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.util.LinkBrowser;

final class ReceiptsPanel extends PluginPanel {
    private static final Color INK = new Color(21, 29, 28), PAPER = new Color(239, 237, 221), MINT = new Color(186, 226, 121);
    private static final NumberFormat NUMBER = NumberFormat.getIntegerInstance(Locale.US);
    ReceiptsPanel(int minimumLoot) { waiting(minimumLoot); }
    private void begin() {
        removeAll(); setBackground(INK); setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(20, 14, 20, 14));
        text("R E C E I P T S", 20, MINT, true);
        text("YOUR GRIND. THEIR TRANSFER WINDOW.", 9, PAPER, false);
        add(Box.createVerticalStrut(22));
    }
    void waiting(int minimumLoot) {
        begin(); text("The next drop tells a story.", 16, PAPER, true);
        text("Loot worth " + NUMBER.format(minimumLoot) + " GP or more starts a receipt. Set your threshold in settings.", 12, PAPER, false);
        text("Enable Loot Tracker. No chat messages. No account data leaves this plugin.", 11, Color.LIGHT_GRAY, false);
        finish();
    }
    void unavailable() { begin(); text("No verified finance data available.", 14, PAPER, true); finish(); }
    void showReceipt(String encounter, long gp, long total, FinanceFact fact) {
        begin();
        text("01 / YOUR LOOT", 10, MINT, true);
        text(NUMBER.format(gp) + " GP", 27, PAPER, true);
        text(encounter + " · GE estimate", 12, PAPER, false);
        text("Session loot: " + NUMBER.format(total) + " GP", 11, Color.LIGHT_GRAY, false);
        add(Box.createVerticalStrut(14));
        add(new PlayerCard(fact));
        add(Box.createVerticalStrut(10));
        text(fact.committees == null ? "Committee contributions: unavailable" : NumberFormat.getCurrencyInstance(Locale.US).format(fact.committees) + " from other political committees", 11, PAPER, true);
        text(fact.period, 11, Color.LIGHT_GRAY, false);
        add(Box.createVerticalStrut(18));
        text("CHECK THE STAT, THEN THE RECORD.", 10, MINT, true);
        text("Other political committees is an FEC category, not a corporate-only total. Zero means zero in this category, not clean hands. Color shows the amount category, not an integrity rating.", 11, PAPER, false);
        text("TOTAL = campaign receipts. INDIV. = individual contributions. Receipts can double-count authorized committee transfers.", 10, Color.LIGHT_GRAY, false);
        text("Different currencies. Different periods. No exchange rate or same-time claim.", 11, Color.LIGHT_GRAY, false);
        JButton source = new JButton("Read the FEC filing ↗");
        source.setAlignmentX(Component.LEFT_ALIGNMENT); source.setBackground(MINT); source.setForeground(INK);
        source.addActionListener(event -> LinkBrowser.browse(fact.source)); add(source);
        text("Snapshot: " + fact.retrievedAt.substring(0, 10) + " · FEC", 10, Color.LIGHT_GRAY, false);
        text("|||| ||| | |||| || | ||| ||||", 21, MINT, true);
        text("shin86dev / public facts, private play", 10, Color.LIGHT_GRAY, false);
        finish();
    }
    private void text(String value, int size, Color color, boolean bold) {
        JTextArea label = new JTextArea(value); label.setEditable(false); label.setFocusable(false);
        label.setLineWrap(true); label.setWrapStyleWord(true); label.setOpaque(false);
        label.setFont(new Font(Font.SANS_SERIF, bold ? Font.BOLD : Font.PLAIN, size)); label.setForeground(color);
        label.setAlignmentX(Component.LEFT_ALIGNMENT); label.setBorder(new EmptyBorder(0, 0, 8, 0));
        label.setSize(197, Short.MAX_VALUE);
        label.setMaximumSize(new Dimension(Short.MAX_VALUE, label.getPreferredSize().height)); add(label);
    }
    private void finish() { revalidate(); repaint(); }
}
