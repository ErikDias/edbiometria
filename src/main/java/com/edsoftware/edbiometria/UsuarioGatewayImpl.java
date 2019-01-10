package com.edsoftware.edbiometria;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UsuarioGatewayImpl implements UsuarioGateway {

    private final UsuarioRepository usuarioRepository;

    @Override
    public Usuario save(Usuario obj) {
        return usuarioRepository.save(obj);
    }
}