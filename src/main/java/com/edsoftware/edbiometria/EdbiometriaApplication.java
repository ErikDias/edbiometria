package com.edsoftware.edbiometria;

import com.edsoftware.edbiometria.ui.MainForm;
import com.edsoftware.edbiometria.service.UsuarioService;
import com.futronic.SDKHelper.FutronicException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import javax.swing.*;

@SpringBootApplication
@RequiredArgsConstructor
public class EdbiometriaApplication {

    private final UsuarioService tUsuarioService;

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = new SpringApplicationBuilder(EdbiometriaApplication.class).headless(false).run(args);
        EdbiometriaApplication bean = ctx.getBean(EdbiometriaApplication.class);
        java.awt.EventQueue.invokeLater(() -> {
            JFrame.setDefaultLookAndFeelDecorated(true);
            JDialog.setDefaultLookAndFeelDecorated(true);
            JFrame jFrame = new JFrame();
            try {
                jFrame.setContentPane(new MainForm(bean.tUsuarioService).getPanel1());
            } catch (FutronicException e) {
                throw new RuntimeException(e.getMessage());
            }
            jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            jFrame.pack();
            jFrame.setVisible(true);
        });
    }
}