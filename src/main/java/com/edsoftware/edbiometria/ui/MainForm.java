package com.edsoftware.edbiometria.ui;

import com.edsoftware.edbiometria.usecase.CreateImageIcon;
import com.edsoftware.edbiometria.ui.data.MyIcon;
import com.edsoftware.edbiometria.service.UsuarioService;
import com.edsoftware.edbiometria.domain.Usuario;
import com.futronic.SDKHelper.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

@Slf4j
@Component
public class MainForm implements IEnrollmentCallBack , IVerificationCallBack, IIdentificationCallBack {

    private static final int SIZE_OF_PAGE = 1000;
    private final UsuarioService usuarioService;
    private FutronicSdkBase m_Operation;
    private Object m_OperationObj;

    @Getter
    private JPanel panel1;
    private JButton salvarDigitalButton;
    private JButton identificarButton;
    @Setter
    private JTextField txtMessage;
    private JLabel fingerImageLbl;
    private MyIcon myIcon;

    @Autowired
    public MainForm(UsuarioService usuarioService) throws FutronicException {
        this.usuarioService = usuarioService;
        buildIcon();
        salvarDigitalButton.addActionListener(e -> {
            try {
                buildFutronicSdkBase();
                ((FutronicEnrollment) m_Operation).Enrollment( this );
            } catch (FutronicException e1) {
                e1.printStackTrace();
                txtMessage.setText("Nao foi possivel salvar.");
            }
        });
        identificarButton.addActionListener(e -> {
            try {
                m_OperationObj = null;
                buildFutronicSdkBase();
                ((FutronicIdentification) m_Operation).GetBaseTemplate( this );
            } catch (FutronicException e1) {
                e1.printStackTrace();
                txtMessage.setText("Nao foi possivel identificar.");
            }
        });
    }

    private void buildIcon() {
        myIcon = new MyIcon();
        ImageIcon imageIcon = CreateImageIcon.execute("/image/Futronic.png");
        if (Objects.nonNull(imageIcon)) {
            myIcon.setImage(imageIcon.getImage());
            fingerImageLbl.setIcon(myIcon);
        }
    }

    private void buildFutronicSdkBase() throws FutronicException {
        m_Operation = new FutronicEnrollment();
        m_Operation.setFakeDetection(false);
        m_Operation.setFFDControl(true);
        m_Operation.setFARN(166);
        m_Operation.setFastMode(false);
        ((FutronicEnrollment) m_Operation).setMIOTControlOff(true);
        ((FutronicEnrollment) m_Operation).setMaxModels(1);
        m_Operation.setVersion(VersionCompatible.ftr_version_previous);
    }

    @Override
    public void OnEnrollmentComplete(boolean b, int i) {
        if (b) {
            salvarUsuario();
            txtMessage.setText("Digital registrada.");
        } else
            txtMessage.setText("Digital nao registrada.");
        m_Operation = null;
    }

    private void salvarUsuario() {
        Usuario usuario = new Usuario();
        usuario.setFingerPrint(((FutronicEnrollment) m_Operation).getTemplate());
        usuarioService.save(usuario);
    }

    @Override
    public void OnGetBaseTemplateComplete(boolean b, int nResult) {
        if (b) {
            txtMessage.setText("Iniciando identificação...");
            AtomicBoolean userWasFound = new AtomicBoolean(false);
            AtomicReference<Page<Usuario>> currentPage = new AtomicReference<>(usuarioService.findAll(0, SIZE_OF_PAGE));
            IntStream.range(0, currentPage.get().getTotalPages()).forEach(page -> {
                FtrIdentifyRecord[] users = (FtrIdentifyRecord[]) currentPage.get().map(x -> {
                    FtrIdentifyRecord ftrIdentifyRecord = new FtrIdentifyRecord();
                    ftrIdentifyRecord.m_KeyValue[0] = x.getId().byteValue();
                    ftrIdentifyRecord.m_Template = x.getFingerPrint();
                    return ftrIdentifyRecord;
                }).getContent().toArray();
                FtrIdentifyResult result = new FtrIdentifyResult();
                int indexResult = ((FutronicIdentification) m_Operation).Identification(users, result);
                if (indexResult == FutronicSdkBase.RETCODE_OK) {
                    txtMessage.setText("Usuario encontrado, índice: " + Arrays.toString(users[indexResult].m_KeyValue));
                    userWasFound.set(true);
                    return;
                }
                currentPage.set(usuarioService.findAll(page, SIZE_OF_PAGE));
            });
            if (!userWasFound.get())
                txtMessage.setText("Usuário não encontrado.");
        }
        m_Operation = null;
    }

    @Override
    public void OnVerificationComplete(boolean b, int i, boolean b1) {}

    @Override
    public void OnPutOn(FTR_PROGRESS ftr_progress) {
        txtMessage.setText( "Coloque o dedo no leitor." );
    }

    @Override
    public void OnTakeOff(FTR_PROGRESS ftr_progress) {
        txtMessage.setText( "Tire o dedo do leitor." );
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