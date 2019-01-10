package com.edsoftware.edbiometria;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public long count() {
        return usuarioRepository.count();
    }

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }
}