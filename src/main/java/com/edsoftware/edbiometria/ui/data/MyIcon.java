package com.edsoftware.edbiometria.ui.data;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class MyIcon implements Icon {

    private Image m_Image = null;

    public void paintIcon(Component c, Graphics g, int x, int y) {
        if (m_Image != null)
            g.drawImage(m_Image, x, y, getIconWidth(), getIconHeight(), null);
        else
            g.fillRect(x, y, getIconWidth(), getIconHeight());
    }

    public int getIconWidth() {
        return 160;
    }

    public int getIconHeight() {
        return 210;
    }

    public boolean LoadImage(String path) {
        boolean bRetCode = false;
        Image newImg;
        try {
            File f = new File(path);
            newImg = ImageIO.read(f);
            bRetCode = true;
            setImage(newImg);
        } catch( IOException ignored) {}
        return bRetCode;
    }

    public void setImage(Image Img) {
        if (Img != null)
            m_Image = Img.getScaledInstance(getIconWidth(), getIconHeight(), Image.SCALE_FAST);
        else
            m_Image = null;
    }
}