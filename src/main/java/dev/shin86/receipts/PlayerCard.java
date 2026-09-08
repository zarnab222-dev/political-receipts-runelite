package dev.shin86.receipts;

import java.awt.*;
import java.awt.geom.Path2D;
import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.JComponent;

/** Original trading-card artwork. Amounts are finance facts, never player ratings. */
final class PlayerCard extends JComponent {
    private final FinanceFact fact;
    PlayerCard(FinanceFact fact) {
        this.fact = fact;
        setPreferredSize(new Dimension(197, 332));
        setMinimumSize(new Dimension(197, 332));
        setMaximumSize(new Dimension(197, 332));
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
        Path2D shield = new Path2D.Double();
        shield.moveTo(3, 7); shield.lineTo(192, 2); shield.lineTo(193, 321);
        for (int x = 183; x > 3; x -= 12) { shield.lineTo(x, x % 5 == 0 ? 326 : 320); }
        shield.lineTo(3, 7); shield.closePath();
        g.setColor(new Color(22, 22, 22));
        g.fill(shield); g.setColor(new Color(226, 222, 211)); g.setStroke(new BasicStroke(2)); g.draw(shield);
        g.setClip(shield);
        g.setColor(new Color(255,255,255,23));
        for (int y = 12; y < 320; y += 6) { g.fillRect(7 + y % 9, y, 3 + y % 7, 1); g.fillRect(181 - y % 7, y, 5, 1); }
        Color ink = new Color(242, 237, 224);
        g.setColor(new Color(167, 18, 29)); g.fillRect(3, 14, 190, 24);
        centered(g, "PUBLIC RECORD / 2026", 30, 10, ink);
        g.rotate(-0.055, 98, 77);
        g.setColor(ink); g.fillRect(64, 46, 68, 59);
        String[] names = fact.name.split(" ");
        centered(g, names[0].substring(0, 1) + names[names.length - 1].substring(0, 1), 88, 38, new Color(19, 19, 19));
        g.rotate(0.055, 98, 77);
        centered(g, fact.name.toUpperCase(Locale.US), 128, fact.name.length() > 20 ? 12 : 16, ink);
        centered(g, fact.party.toUpperCase(Locale.US) + " / " + fact.state, 145, 9, ink);
        centered(g, fact.chamber.toUpperCase(Locale.US), 160, 9, ink);
        g.setColor(new Color(225,220,210)); g.fillRect(15, 172, 167, 2);
        Color amountColor = fact.committees == null ? new Color(160,160,160) : fact.committees == 0 ? new Color(119,215,141) : new Color(255,76,83);
        centered(g, fact.committees == null ? "UNKNOWN" : money(fact.committees), 210, fact.committees == null ? 24 : 34, amountColor);
        centered(g, "OTHER POLITICAL COMMITTEES", 228, 9, ink);
        centered(g, "REPORTED CONTRIBUTIONS / USD", 243, 8, ink);
        g.setColor(new Color(225,220,210)); g.fillRect(15, 254, 167, 2);
        centered(g, money(fact.receipts) + " TOTAL    " + money(fact.individual) + " INDIV.", 277, 10, ink);
        centered(g, "CAMPAIGN RECEIPTS / FEC", 292, 8, ink);
        g.dispose();
    }
    private void centered(Graphics2D g, String text, int y, int size, Color color) {
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, size)); g.setColor(color);
        g.drawString(text, (197 - g.getFontMetrics().stringWidth(text)) / 2, y);
    }
}
