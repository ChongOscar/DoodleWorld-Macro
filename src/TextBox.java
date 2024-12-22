import org.w3c.dom.css.Rect;

import javax.imageio.ImageIO;
import javax.print.attribute.standard.MediaSize;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class TextBox {
    private BufferedImage textBoxImage;
    private int x;
    private int y;
    private boolean isFocused;
    private Rectangle rectangle;
    private String string;
    private String focusedBlink;
    private int type;
    public static final int BIG = 1;
    public static final int NAME = 0;

    public TextBox(int x, int y, int type) {
        this.x = x;
        this.y = y;
        this.type = type;
        isFocused = false;
        try {
            switch (type) {
                case NAME -> {
                    textBoxImage = ImageIO.read(new File("assets/name-textbox.png"));
                }
                case BIG -> {
                    textBoxImage = ImageIO.read(new File("assets/big-textbox.png"));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        rectangle = new Rectangle(x, y, textBoxImage.getWidth(), textBoxImage.getHeight());
        string = "";
        focusedBlink = "|";
        Utils.startThread(() -> {
            while (true) {
                Utils.wait(500);
                if (focusedBlink.isEmpty()) {
                    focusedBlink = "|";
                } else {
                    focusedBlink = "";
                }
            }
        });
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public void render(Graphics g, int scrollOffset) {
        int displayY = y + scrollOffset;
        rectangle.y = y + scrollOffset;
        g.setFont(new Font("Courier New", Font.BOLD, 24));
        g.setColor(Color.black);
        g.drawImage(textBoxImage, x, displayY, null);
        if (!isFocused) {
            focusedBlink = "";
            if (string.isEmpty()) {
                g.setColor(Color.gray);
                if (type == NAME) {
                    g.drawString("Doodle Name", x + 15, displayY + 30);
                } else if (type == BIG) {
                    g.setFont(new Font("Courier New", Font.BOLD, 18));
                    g.drawString("Doodle exceptions", x + 15, displayY + 30);
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
                if (i == newStr.length - 1) {
                    g.drawString(newStr[i] + focusedBlink, x + 15, displayY + 30 + (i * g.getFontMetrics().getHeight()));
                } else {
                    g.drawString(newStr[i], x + 15, displayY + 30 + (i * g.getFontMetrics().getHeight()));
                }
            }
        } else {
            g.drawString(string + focusedBlink, x + 15, displayY + 30);
        }
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

    public void addChar(char newChar) {
        if (type == NAME) {
            if (string.length() <= 11) {
                string += newChar;
            }
        } else if (type == BIG) {
            if (string.length() <= 44) {
                string += newChar;
            }
        }
    }

    public void removeChar() {
        if (!string.isEmpty()) {
            string = string.substring(0, string.length() - 1);
        }
    }
}
