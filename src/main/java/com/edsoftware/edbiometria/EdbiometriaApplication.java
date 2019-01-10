package com.edsoftware.edbiometria;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import javax.annotation.PostConstruct;
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
			jFrame.setContentPane(new MainForm(bean.tUsuarioService).getPanel1());
			jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			jFrame.pack();
			jFrame.setVisible(true);
		});
	}
}