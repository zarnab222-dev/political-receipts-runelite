package dev.shin86.receipts;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;

/** Renders the actual Swing component with a clearly identified sample loot event. */
public class ReceiptsPreview {
    public static void main(String[] args) throws Exception {
        FinanceFact fact = FinanceFact.load().stream().filter(f -> f.name.equals("Ted Cruz")).findFirst().orElseThrow();
        SwingUtilities.invokeAndWait(() -> {
            ReceiptsPanel panel = new ReceiptsPanel(50000);
            panel.showReceipt("Tombs of Amascut (sample)", 78000, 78000, fact);
            panel.setSize(225, 900); layout(panel);
            BufferedImage image = new BufferedImage(225, 900, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = image.createGraphics();
            graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            panel.printAll(graphics); graphics.dispose();
            try { ImageIO.write(image, "png", new File("build/receipt-preview.png")); }
            catch (Exception ex) { throw new RuntimeException(ex); }
        });
    }
    private static void layout(Container container) {
        container.doLayout();
        for (Component child : container.getComponents()) { if (child instanceof Container) { layout((Container) child); } }
    }
}
