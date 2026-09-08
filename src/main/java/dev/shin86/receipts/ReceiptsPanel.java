package dev.shin86.receipts;

import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.util.LinkBrowser;

final class ReceiptsPanel extends PluginPanel {
    private static final Color INK = new Color(12, 12, 12), PAPER = new Color(239, 237, 221), ACCENT = new Color(255, 76, 83);
    private static final NumberFormat NUMBER = NumberFormat.getIntegerInstance(Locale.US);
    ReceiptsPanel(int minimumLoot) { waiting(minimumLoot); }
    private void begin() {
        removeAll(); setBackground(INK); setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(20, 14, 20, 14));
        text("RECEIPTS", 30, PAPER, true);
        text("FOLLOW THE MONEY.", 11, ACCENT, true);
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
        text("01 / YOUR LOOT", 10, PAPER, true);
        text(NUMBER.format(gp) + " GP", 27, PAPER, true);
        text(encounter + " · GE estimate", 12, PAPER, false);
        add(Box.createVerticalStrut(14));
        add(new PlayerCard(fact));
        add(Box.createVerticalStrut(10));
        text(fact.period, 11, Color.LIGHT_GRAY, false);
        text("GP and USD; different periods.", 11, Color.LIGHT_GRAY, false);
        JButton source = new JButton("Read the FEC filing ↗");
        source.setAlignmentX(Component.LEFT_ALIGNMENT); source.setBackground(PAPER); source.setForeground(INK);
        source.addActionListener(event -> LinkBrowser.browse(fact.source)); add(source);
        add(Box.createVerticalStrut(10));
        JButton disclosure = new JButton("+ What this means");
        disclosure.setAlignmentX(Component.LEFT_ALIGNMENT); disclosure.setForeground(PAPER); disclosure.setBackground(INK);
        disclosure.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12)); add(disclosure);
        JTextArea details = text("Other political committees is an FEC category, not a corporate-only total. Zero means zero in this category. Color is not an integrity rating.\n\nExact amount: "
            + (fact.committees == null ? "unavailable" : NumberFormat.getCurrencyInstance(Locale.US).format(fact.committees))
            + ".\n\nGP uses a GE estimate. No exchange rate or same-time comparison is implied. Session loot: " + NUMBER.format(total)
            + " GP.\n\nSnapshot: " + fact.retrievedAt.substring(0, 10) + ". Campaign records are bundled, not live.\n\nshin86dev / public facts, private play", 12, PAPER, false);
        details.setVisible(false);
        disclosure.addActionListener(event -> {
            details.setVisible(!details.isVisible());
            disclosure.setText(details.isVisible() ? "− What this means" : "+ What this means"); finish();
        });
        finish();
    }
    private JTextArea text(String value, int size, Color color, boolean bold) {
        JTextArea label = new JTextArea(value); label.setEditable(false); label.setFocusable(false);
        label.setLineWrap(true); label.setWrapStyleWord(true); label.setOpaque(false);
        label.setFont(new Font(Font.SANS_SERIF, bold ? Font.BOLD : Font.PLAIN, size)); label.setForeground(color);
        label.setAlignmentX(Component.LEFT_ALIGNMENT); label.setBorder(new EmptyBorder(0, 0, 8, 0));
        label.setSize(197, Short.MAX_VALUE);
        label.setMaximumSize(new Dimension(Short.MAX_VALUE, label.getPreferredSize().height)); add(label);
        return label;
    }
    private void finish() { revalidate(); repaint(); }
}
