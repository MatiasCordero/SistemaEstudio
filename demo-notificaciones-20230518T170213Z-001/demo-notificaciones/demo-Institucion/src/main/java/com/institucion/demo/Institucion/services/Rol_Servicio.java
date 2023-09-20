package com.institucion.demo.Institucion.services;

import com.institucion.demo.Institucion.Excepciones.MiException;
import com.institucion.demo.Institucion.entities.Permisos;
import com.institucion.demo.Institucion.entities.Rol;
import com.institucion.demo.Institucion.repository.Permisos_Repositorio;
import com.institucion.demo.Institucion.repository.Rol_Repositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class Rol_Servicio {

    @Autowired
    Rol_Repositorio rol_repositorio;
    @Autowired
    Permisos_Repositorio permisos_repositorio;

    @Transactional
    public void crearRol(Rol nuevoRol) throws MiException {
        validate(nuevoRol.getNombre(), nuevoRol.getPermisos());

        Rol role = rol_repositorio.findByNombre(nuevoRol.getNombre());
        if (role == null) {
            role = new Rol("ROL_" + nuevoRol.getNombre().toUpperCase());
        }
        role.setCreated_at(new Date());
        role.setUpdated_at(new Date());
        role.setPermisos(nuevoRol.getPermisos());
        role = rol_repositorio.save(role);
    }

    @Transactional
    public void modificarRol(Rol nuevoRol) throws MiException {
        validatemodi(nuevoRol.getNombre(), nuevoRol.getPermisos());
        Optional<Rol> rol = rol_repositorio.findById(nuevoRol.getId());

        if (rol.isPresent()) {
            Rol viejoRol = rol.get();

            viejoRol.setNombre(nuevoRol.getNombre().toUpperCase());
            viejoRol.setPermisos(nuevoRol.getPermisos());
            viejoRol.setUpdated_at(new Date());

            rol_repositorio.save(viejoRol);
        }
    }

    public List<Rol> listRoles() {
        return rol_repositorio.findAll();
    }

    public List<Permisos> listPermisos() {
        return permisos_repositorio.findAll();
    }

    public Collection<Permisos> permisos_get(Long id) {
        Collection<Permisos> permisos = rol_repositorio.findById(id).get().getPermisos();

        return permisos;
    }

    private void validate(String name, Collection<Permisos> permisos) throws MiException {
        if (rol_repositorio.findByNombre("ROL_" + name.toUpperCase()) != null) {
            throw new MiException("Ya existe un rol con ese nombre");
        }
        if (name.isEmpty() || name == null) {
            throw new MiException("Por favor, Ingrese un nombre");
        }
        if (permisos.isEmpty()) {
            throw new MiException("Debe seleccionar un permiso");
        }
    }

    private void validatemodi(String name, Collection<Permisos> permisos) throws MiException {
        if (name.isEmpty() || name == null) {
            throw new MiException("Por favor, Ingrese un nombre");
        }
        if (permisos.isEmpty()) {
            throw new MiException("Debe seleccionar un permiso");
        }
    }

    @Transactional
    public void delete(Long id) {
        rol_repositorio.deleteById(id);
    }

    public Rol get(Long id) {
        return rol_repositorio.findById(id).get();
    }
}
