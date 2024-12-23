import org.w3c.dom.css.Rect;

import javax.imageio.ImageIO;
import javax.print.attribute.standard.MediaSize;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class TextBox extends Interactable {
    private BufferedImage textBoxImage;
    private boolean isFocused;
    private String string;
    private String focusedBlink;
    private int type;

    public static final int BIG = 1;
    public static final int NAME = 0;

    public TextBox(int x, int y, int type) {
        super(x, y);
        this.type = type;
        isFocused = false;
        string = "";
        focusedBlink = "|";
        try {
            textBoxImage = ImageIO.read(new File(type == NAME ? "assets/name-textbox.png" : "assets/big-textbox.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        rectangle = new Rectangle(x, y, textBoxImage.getWidth(), textBoxImage.getHeight());
        Utils.startThread(() -> {
            while (true) {
                Utils.wait(500);
                focusedBlink = focusedBlink.isEmpty() ? "|" : "";
            }
        });
    }

    public void setFocus(boolean focus) {
        isFocused = focus;
    }

    public boolean isFocused() {
        return isFocused;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public void addChar(char newChar) {
        if ((type == NAME && string.length() <= 11) || (type == BIG && string.length() <= 44)) {
            string += newChar;
        }
    }

    public void removeChar() {
        if (!string.isEmpty()) {
            string = string.substring(0, string.length() - 1);
        }
    }

    @Override
    public void render(Graphics g, int scrollOffset) {
        int displayY = y + scrollOffset;
        rectangle.y = displayY;
        g.setFont(new Font("Courier New", Font.BOLD, 24));
        g.setColor(Color.black);
        g.drawImage(textBoxImage, x, displayY, null);

        if (!isFocused) {
            focusedBlink = "";
            if (string.isEmpty()) {
                g.setColor(Color.gray);
                if (type == NAME) {
                    g.drawString("Doodle Name", x + 15, displayY + 30);
                } else {
                    g.setFont(new Font("Courier New", Font.BOLD, 18));
                    g.drawString("Doodle exceptions", x + 15, displayY + 30);
                    g.drawString("Separate with ,", x + 15, displayY + 30 + g.getFontMetrics().getHeight());
                }
            }
        }

        if (type == BIG) {
            StringBuilder sb = new StringBuilder(string);
            for (int i = 1; i <= Math.floor((double) string.length() / 15); i++) {
                sb.insert(i * 15 + (i - 1), "\n");
            }
            String[] newStr = sb.toString().split("\n");
            for (int i = 0; i < newStr.length; i++) {
                g.drawString(newStr[i] + (i == newStr.length - 1 ? focusedBlink : ""), x + 15, displayY + 30 + (i * g.getFontMetrics().getHeight()));
            }
        } else {
            g.drawString(string + focusedBlink, x + 15, displayY + 30);
        }
    }
}

