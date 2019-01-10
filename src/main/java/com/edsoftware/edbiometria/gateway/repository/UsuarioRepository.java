package com.edsoftware.edbiometria.gateway.repository;

import com.edsoftware.edbiometria.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
}