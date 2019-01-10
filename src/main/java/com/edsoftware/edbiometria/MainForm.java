package com.edsoftware.edbiometria;

import com.futronic.SDKHelper.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class MainForm implements IEnrollmentCallBack , IVerificationCallBack, IIdentificationCallBack {

    private final UsuarioService usuarioService;

    private FutronicSdkBase m_Operation;

    @Getter
    private JPanel panel1;
    private JButton salvarDigitalButton;
    private JButton identificarButton;
    private JTextField txtMessage;
    private JLabel fingerImageLbl;
    private MyIcon myIcon;
    private Usuario usuario;

    @Autowired
    public MainForm(UsuarioService usuarioService) {

        myIcon = new MyIcon();
        ImageIcon imageIcon = CreateImageIcon.execute("/image/Futronic.png");
        if (Objects.nonNull(imageIcon)) {
            myIcon.setImage(imageIcon.getImage());
            fingerImageLbl.setIcon(myIcon);
        }
        this.usuarioService = usuarioService;
        salvarDigitalButton.addActionListener(e -> {
            try {
                usuario = new Usuario();

                m_Operation = new FutronicEnrollment();

                m_Operation.setFakeDetection(false);
                m_Operation.setFFDControl(true);
                m_Operation.setFARN(166);
                m_Operation.setFastMode(false);
                ((FutronicEnrollment)m_Operation).setMIOTControlOff(true);
                ((FutronicEnrollment)m_Operation).setMaxModels(1);
                m_Operation.setVersion(VersionCompatible.ftr_version_previous );

                ((FutronicEnrollment)m_Operation).Enrollment( this );
            } catch (FutronicException e1) {
                e1.printStackTrace();
            }
        });
        identificarButton.addActionListener(e -> {
            try {
                m_Operation = new FutronicEnrollment();
                m_Operation.setFakeDetection(false);
                m_Operation.setFFDControl(true);
                m_Operation.setFARN(166);
                m_Operation.setFastMode(false);
                ((FutronicEnrollment)m_Operation).setMIOTControlOff(true);
                ((FutronicEnrollment)m_Operation).setMaxModels(1);
                m_Operation.setVersion(VersionCompatible.ftr_version_previous );

                ((FutronicIdentification)m_Operation).GetBaseTemplate( this );

            } catch (FutronicException e1) {
                e1.printStackTrace();
            }
        });
    }

    @Override
    public void OnEnrollmentComplete(boolean b, int i) {
        if (b) {
            usuario.setFingerPrint(((FutronicEnrollment)m_Operation).getTemplate());
            usuarioService.save(usuario);
            txtMessage.setText("Digital registrada.");
        } else
            txtMessage.setText("Digital nao registrada.");
        usuario = null;
    }

    @Override
    public void OnGetBaseTemplateComplete(boolean b, int nResult) {
        if (b) {
            int size = (int) usuarioService.count();
            FtrIdentifyRecord[] rgRecords = new FtrIdentifyRecord[size];
            List<Usuario> users = usuarioService.findAll();


            FtrIdentifyResult result = new FtrIdentifyResult();
            nResult = ((FutronicIdentification)m_Operation).Identification(rgRecords, result);
            if (nResult == FutronicSdkBase.RETCODE_OK)
            {
                txtMessage.setText("Digital identificada.");
            } else {
                txtMessage.setText("Digital no identificada.");
            }
        }
    }

    @Override
    public void OnVerificationComplete(boolean b, int i, boolean b1) {

    }

    @Override
    public void OnPutOn(FTR_PROGRESS ftr_progress) {
        txtMessage.setText( "Colocar finger into device, please ..." );

    }

    @Override
    public void OnTakeOff(FTR_PROGRESS ftr_progress) {
        txtMessage.setText( "Iirar finger into device, please ..." );

    }

    @Override
    public void UpdateScreenImage(BufferedImage bufferedImage) {
        myIcon.setImage(bufferedImage);
        fingerImageLbl.repaint();
    }

    @Override
    public boolean OnFakeSource(FTR_PROGRESS ftr_progress) {
        return false;
    }
}