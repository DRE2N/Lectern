package de.erethon.lectern;

import de.erethon.lectern.menu.LecternMenu;
import de.erethon.lectern.menu.elements.Changing;
import de.erethon.lectern.menu.elements.Texturable;
import de.erethon.lectern.menu.elements.UIElement;
import net.minecraft.world.phys.Vec2;
import org.bukkit.Bukkit;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImageGenerator {

    // Assuming the image size of my test image, 753x773. We still have to figure out a good size that isn't this random.
    private static final int WIDTH_ONE_SLOT = 50;
    private static final int HEIGHT_ONE_SLOT = 50;
    private static final int BORDER = 10;
    private static final int BORDER_TOP = 45;

    private final LecternMenu menu;
    private final Image background;
    private final int id;

    private final List<GenerationEntry> entries = new ArrayList<>();

    public ImageGenerator(LecternMenu menu, Image background, int id) {
        this.menu = menu;
        this.background = background;
        this.id = id;
    }

    public void generate() {
        int slotIndex = 0;
        for (UIElement element : menu.getElements()) {
            if (element instanceof Texturable texturable && texturable.getTexture() != null) {
                Image texture = texturable.getTexture();
                int x = element.x();
                int y = element.y();
                int width = element.width();
                int height = element.height();
                Vec2 size = Lectern.slotToXY(slotIndex);
                int adjustedX = (int) ((x + size.x) * WIDTH_ONE_SLOT + BORDER);
                int adjustedY = (int) ((y + size.y) * HEIGHT_ONE_SLOT + BORDER_TOP);
                entries.add(new GenerationEntry(texture, adjustedX, adjustedY, width * WIDTH_ONE_SLOT, height * HEIGHT_ONE_SLOT));
            }
            slotIndex++;
        }
        Image image = makeImage();
        try {
            saveImage(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Adds all entries on the background
    private Image makeImage() {
        BufferedImage image = new BufferedImage(753, 773, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.drawImage(background, 0, 0, null);
        int count = 0;
        boolean done = false;
        for (GenerationEntry entry : entries) {
            System.out.println("Drawing entry at " + entry.x() + ", " + entry.y() + " with size " + entry.width() + "x" + entry.height() + " and texture " + entry.image() + " to the image.");
            done = g2d.drawImage(entry.image(), entry.x(), entry.y(), entry.width(), entry.height(), null);
            count++;
        }
        return image;
    }

    private void saveImage(Image image) throws IOException {
        File folder = new File(Bukkit.getPluginsFolder(), "Lectern/resourcepack/assets/lectern/textures/gui");
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File file = new File(folder, id + ".png");
        ImageIO.write((BufferedImage) image, "png", file);
    }



}
