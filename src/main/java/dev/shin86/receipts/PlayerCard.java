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
        shield.moveTo(4, 18); shield.lineTo(98, 3); shield.lineTo(193, 18); shield.lineTo(190, 300); shield.lineTo(98, 327); shield.lineTo(7, 300); shield.closePath();
        g.setPaint(new GradientPaint(0, 0, new Color(241, 225, 166), 197, 332, new Color(171, 145, 82)));
        g.fill(shield); g.setColor(new Color(250, 236, 179)); g.setStroke(new BasicStroke(2)); g.draw(shield);
        g.setClip(shield);
        g.setColor(new Color(255,255,255,28));
        for (int x = -260; x < 260; x += 25) { g.drawLine(x, 0, x + 332, 332); }
        Color ink = new Color(32, 37, 32);
        centered(g, "PUBLIC RECORD / 2026", 33, 9, ink);
        g.setColor(new Color(40, 59, 44)); g.fillOval(68, 45, 62, 62);
        String[] names = fact.name.split(" ");
        centered(g, names[0].substring(0, 1) + names[names.length - 1].substring(0, 1), 85, 24, new Color(238, 225, 177));
        centered(g, fact.name.toUpperCase(Locale.US), 128, fact.name.length() > 20 ? 12 : 16, ink);
        centered(g, fact.party.toUpperCase(Locale.US) + " / " + fact.state, 145, 9, ink);
        centered(g, fact.chamber.toUpperCase(Locale.US), 160, 9, ink);
        g.setColor(new Color(32,37,32,65)); g.drawLine(22, 173, 175, 173);
        Color amountColor = fact.committees == null ? new Color(80,80,80) : fact.committees == 0 ? new Color(21,91,49) : new Color(147,35,34);
        centered(g, fact.committees == null ? "UNKNOWN" : money(fact.committees), 210, fact.committees == null ? 24 : 34, amountColor);
        centered(g, "OTHER POLITICAL COMMITTEES", 228, 9, ink);
        centered(g, "REPORTED CONTRIBUTIONS / USD", 243, 8, ink);
        g.setColor(new Color(32,37,32,65)); g.drawLine(22, 254, 175, 254);
        centered(g, money(fact.receipts) + " TOTAL    " + money(fact.individual) + " INDIV.", 277, 10, ink);
        centered(g, "CAMPAIGN RECEIPTS / FEC", 292, 8, ink);
        g.dispose();
    }
    private void centered(Graphics2D g, String text, int y, int size, Color color) {
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, size)); g.setColor(color);
        g.drawString(text, (197 - g.getFontMetrics().stringWidth(text)) / 2, y);
    }
}
