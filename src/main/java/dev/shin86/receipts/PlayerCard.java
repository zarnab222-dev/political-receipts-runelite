package dev.shin86.receipts;

import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.JComponent;

/** Original trading-card artwork. Amounts are finance facts, never player ratings. */
final class PlayerCard extends JComponent {
    private final FinanceFact fact;
    PlayerCard(FinanceFact fact) {
        this.fact = fact;
        setPreferredSize(new Dimension(197, 280));
        setMinimumSize(new Dimension(197, 280));
        setMaximumSize(new Dimension(197, 280));
        setAlignmentX(Component.LEFT_ALIGNMENT);
        setToolTipText("Contributions from other political committees: " + (fact.committees == null ? "unavailable" : NumberFormat.getCurrencyInstance(Locale.US).format(fact.committees)));
    }
    private String money(double amount) {
        if (Math.abs(amount) >= 1000000) { return String.format(Locale.US, "$%.2fM", amount / 1000000); }
        if (Math.abs(amount) >= 1000) { return String.format(Locale.US, "$%.1fK", amount / 1000); }
        return NumberFormat.getCurrencyInstance(Locale.US).format(amount);
    }
    @Override protected void paintComponent(Graphics original) {
        Graphics2D g = (Graphics2D) original.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setColor(new Color(239, 238, 232)); g.fillRoundRect(2, 2, 193, 274, 18, 18);
        g.setColor(new Color(199, 200, 194)); g.drawRoundRect(2, 2, 193, 274, 18, 18);
        Color ink = new Color(35, 39, 42);
        centered(g, "Public record · 2026", 28, 10, new Color(99, 103, 106));
        g.setColor(new Color(216, 217, 214)); g.fillOval(67, 44, 64, 64);
        String[] names = fact.name.split(" ");
        centered(g, names[0].substring(0, 1) + names[names.length - 1].substring(0, 1), 88, 28, ink);
        centered(g, fact.name, 128, fact.name.length() > 20 ? 12 : 18, ink);
        centered(g, fact.party + " · " + fact.state, 146, 11, new Color(90, 94, 97));
        centered(g, fact.chamber, 161, 11, new Color(90, 94, 97));
        g.setColor(new Color(210,211,205)); g.drawLine(18, 174, 179, 174);
        Color amountColor = fact.committees == null ? new Color(100,104,108) : fact.committees == 0 ? new Color(44,108,71) : new Color(160,69,72);
        centered(g, fact.committees == null ? "UNKNOWN" : money(fact.committees), 210, fact.committees == null ? 24 : 34, amountColor);
        centered(g, "Contributions from", 231, 12, ink);
        centered(g, "other political committees", 247, 11, ink);
        g.dispose();
    }
    private void centered(Graphics2D g, String text, int y, int size, Color color) {
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, size)); g.setColor(color);
        g.drawString(text, (197 - g.getFontMetrics().stringWidth(text)) / 2, y);
    }
}
