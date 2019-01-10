package com.edsoftware.edbiometria;

import javax.swing.*;

public class CreateImageIcon {

    public static ImageIcon execute(String path) {
        java.net.URL imgURL = MainForm.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL );
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
}