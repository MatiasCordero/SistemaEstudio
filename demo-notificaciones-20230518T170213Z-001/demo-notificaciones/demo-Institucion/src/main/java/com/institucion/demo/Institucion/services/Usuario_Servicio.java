package com.institucion.demo.Institucion.services;

import com.institucion.demo.Institucion.Excepciones.MiException;
import com.institucion.demo.Institucion.entities.Permisos;
import com.institucion.demo.Institucion.entities.Rol;
import com.institucion.demo.Institucion.entities.Usuario;
import com.institucion.demo.Institucion.repository.Rol_Repositorio;
import com.institucion.demo.Institucion.repository.Usuario_Repositorio;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.util.*;

@Service
public class Usuario_Servicio implements UserDetailsService {

    @Autowired
    private Usuario_Repositorio usuario_repositorio;

    @Autowired
    private Rol_Repositorio rol_repositorio;


    @Transactional
    public Usuario update(Usuario userNew, String password2) throws MiException {

        validateModi(userNew.getNombre(), userNew.getEmail(), userNew.getContraseña(), password2, userNew.getTelefono(), userNew.getRoles());

        Optional<Usuario> response = usuario_repositorio.findById(userNew.getId());
        if (response.isPresent()) {
            Usuario userOld = response.get();

            userOld.setNombre(userNew.getNombre());
            userOld.setEmail(userNew.getEmail());
            userOld.setUpdated_at(new Date());
            userOld.setContraseña(new BCryptPasswordEncoder().encode(userNew.getContraseña()));
            userOld.setRoles(userNew.getRoles());

            return usuario_repositorio.save(userOld);
        }
        throw new MiException("El usuario no pudo ser modificado");
    }

    public Usuario get(Long id) {
        return usuario_repositorio.findById(id).get();
    }

    public Collection<Rol> roles_get(Long id) {

        Collection<Rol> roles = usuario_repositorio.findById(id).get().getRoles();

        return roles;
    }

    @Transactional(readOnly = true)
    public List<Usuario> userList() {

        List<Usuario> users = new ArrayList();

        users = usuario_repositorio.findAll();

        return users;
    }

    public List<Usuario> listAll() {
        return usuario_repositorio.findAll();
    }

    private void validate(String name, String email, String password, String password2, String telefono) throws MiException {

        if (name.isEmpty() || name == null) {
            throw new MiException("El nombre no puede ser nulo o estar vacío");
        }
        if (email == null || email.isEmpty() || !email.contains("@") || !email.contains(".")) {
            throw new MiException("Direccion email invalida");
        }
        if (usuario_repositorio.findByEmail(email) != null) {
            throw new MiException("El email registrado ya existe");
        }
        if (password.isEmpty()) {
            throw new MiException("Ingrese una contraseña");
        }
        if (password.length() < 8) {
            throw new MiException("La contraseña debe tener por lo menos 8 caracteres");
        }
        if (!password.equals(password2)) {
            throw new MiException("Las contraseñas ingresadas deben ser iguales");
        }
        if (telefono == null) {
            throw new MiException("Ingrese un Telefono");
        }
        if (telefono.length() < 10) {
            throw new MiException("El telefono ingresado es invalido");
        }

    }

    private void validateModi(String name, String email, String password, String password2, String telefono, Collection<Rol> roles) throws MiException {

        if (name.isEmpty() || name == null) {
            throw new MiException("el nombre no puede ser nulo o estar vacío");
        }
        if (email == null || email.isEmpty() || !email.contains("@") || !email.contains(".")) {
            throw new MiException("Email invalido");
        }
        if (password.isEmpty()) {
            throw new MiException("Ingrese una contraseña");
        }
        if (!password.equals(password2)) {
            throw new MiException("Las contraseñas ingresadas deben ser iguales");
        }
        if (telefono == null) {
            throw new MiException("Ingrese un Telefono");
        }
        if (telefono.length() < 10) {
            throw new MiException("El telefono ingresado es invalido");
        }
        if (roles.isEmpty()) {
            throw new MiException("Debe ingresar un rol para el usuario");
        }
    }

    @Transactional
    public void delete(Long id) {
        usuario_repositorio.deleteById(id);
    }


    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Usuario user = usuario_repositorio.findByEmail(email);

        if (user != null) {

            Set<GrantedAuthority> grantList = new HashSet<GrantedAuthority>();

            //Crear la lista de los roles/accessos que tienen el usuarios
            for (Rol role : user.getRoles()) {
                for (Permisos permisos : role.getPermisos()) {
                    GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(permisos.getPermiso());
                    grantList.add(grantedAuthority);
                }
            }
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

            HttpSession session = attr.getRequest().getSession(true);

            session.setAttribute("usuariosession", user);

            //Crear y retornar Objeto de usuario soportado por Spring Security
            return new User(user.getNombre(), user.getContraseña(), grantList);
        } else {
            return null;
        }
    }
}