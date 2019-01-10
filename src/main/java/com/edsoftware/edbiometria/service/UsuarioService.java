package com.edsoftware.edbiometria.service;

import com.edsoftware.edbiometria.domain.Usuario;
import com.edsoftware.edbiometria.gateway.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public Page<Usuario> findAll(int currentPage, int pageSize) {
        Pageable pageable = new PageRequest(currentPage, pageSize);
        return usuarioRepository.findAll(pageable);
    }
}