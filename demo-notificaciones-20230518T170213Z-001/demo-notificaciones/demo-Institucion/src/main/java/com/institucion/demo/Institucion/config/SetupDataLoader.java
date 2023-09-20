package com.institucion.demo.Institucion.config;

import com.institucion.demo.Institucion.entities.Permisos;
import com.institucion.demo.Institucion.entities.Rol;
import com.institucion.demo.Institucion.entities.Usuario;
import com.institucion.demo.Institucion.enums.Permisos_Enum;
import com.institucion.demo.Institucion.repository.Permisos_Repositorio;
import com.institucion.demo.Institucion.repository.Rol_Repositorio;
import com.institucion.demo.Institucion.repository.Usuario_Repositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {


    private boolean alreadySetup = false;

    @Autowired
    private Usuario_Repositorio usuario_repositorio;

    @Autowired
    private Rol_Repositorio rol_repositorio;

    @Autowired
    private Permisos_Repositorio permisos_repositorio;




    // API

    @Override
    @Transactional
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }

        // == create initial privileges
        createPrivilegeIfNotFound();

        // == create initial roles
        final List<Permisos> adminPrivileges = permisos_repositorio.findAll();
        final List<Permisos> maestroPrivileges = permisos_repositorio.findByNameList("carga_Notas");

        final Rol adminRole = createRoleIfNotFound("ADMIN", adminPrivileges);
        final Rol maestroRole = createRoleIfNotFound("MAESTRO", maestroPrivileges);

        // == create initial user
        createUserIfNotFound("test@test.com", "Test", "543764235182", "test", new ArrayList<>(Arrays.asList(adminRole)));

        alreadySetup = true;
    }

    @Transactional
    Permisos createPrivilegeIfNotFound() {
        for (Permisos_Enum name: Permisos_Enum.values()) {
            Permisos privilege = permisos_repositorio.findByName(name.toString());
            if (privilege == null) {
                privilege = new Permisos(name.toString());
                privilege = permisos_repositorio.save(privilege);
            }
            return privilege;
        }
        return null;
    }

    @Transactional
    Rol createRoleIfNotFound(final String name, final Collection<Permisos> privileges) {
        Rol role = rol_repositorio.findByNombre(name);
        if (role == null) {
            role = new Rol(name);
        }
        role.setCreated_at(new Date());
        role.setUpdated_at(new Date());
        role.setPermisos(privileges);
        role = rol_repositorio.save(role);
        return role;
    }

    @Transactional
    Usuario createUserIfNotFound(final String email, final String nombre, final String telefono, final String contraseña, final Collection<Rol> roles) {
        Usuario user = usuario_repositorio.findByEmail(email);
        if (user == null) {
            user = new Usuario();
            user.setNombre(nombre);
            user.setTelefono(telefono);
            user.setContraseña(new BCryptPasswordEncoder().encode(contraseña));
            user.setEmail(email);
            user.setEstado(true);
            user.setCreated_at(new Date());
            user.setUpdated_at(new Date());
        }
        user.setRoles(roles);
        user = usuario_repositorio.save(user);
        return user;
    }

}