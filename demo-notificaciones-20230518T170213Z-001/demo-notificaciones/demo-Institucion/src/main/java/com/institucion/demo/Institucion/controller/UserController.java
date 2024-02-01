package com.institucion.demo.Institucion.controller;

import com.institucion.demo.Institucion.Excepciones.MiException;
import com.institucion.demo.Institucion.entities.Rol;
import com.institucion.demo.Institucion.entities.Usuario;
import com.institucion.demo.Institucion.services.Rol_Servicio;
import com.institucion.demo.Institucion.services.Usuario_Servicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Controller
@RequestMapping("/usuario")
public class UserController {

    @Autowired
    private Usuario_Servicio usuario_servicio;
    @Autowired
    private Rol_Servicio rol_servicio;




    @PreAuthorize("isAuthenticated() and hasAuthority('Usuario_Ver_Roles')")
    @RequestMapping("/listar_r/{id}")
    public String listRoles(@PathVariable(name = "id") Long id, Model model) {
        Collection<Rol> roles = usuario_servicio.roles_get(id);

        HashSet<Rol> newRoles = new HashSet<>();

        for (Rol rol: roles) {
            newRoles.add(rol);
        }

        model.addAttribute("roles", newRoles);

        return "user/rol_list.html";
    }

    @PreAuthorize("isAuthenticated() and hasAuthority('Mostrar_Usuario')")
    @RequestMapping("/listar")
    public String listarUsuario(Model model){

        List<Usuario> users = usuario_servicio.listAll();
        model.addAttribute("users", users);

        List<Rol> roles = rol_servicio.listRoles();
        model.addAttribute("roles", roles);

        return "user/user_list.html";
    }

    @PreAuthorize("isAuthenticated() and hasAuthority('Modificar_Usuario')")
    @RequestMapping("/modificar/{id}")
    public String uploadUser(@PathVariable(name = "id") Long id, ModelMap model) {
        model.addAttribute("user", usuario_servicio.get(id));
        List<Rol> listRoles = rol_servicio.listRoles();
        model.addAttribute("listRoles", listRoles);
        return "user/user_update";
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/modificar/{id}", method = RequestMethod.POST)
    public String uploadUser(@PathVariable(name = "id") Long id, ModelMap model, @ModelAttribute("user") Usuario user, @RequestParam String contraseña2) throws MiException {
        try {
            usuario_servicio.update(user, contraseña2);

        } catch (MiException ex) {
            model.put("error", ex.getMessage());
            model.addAttribute("user", usuario_servicio.get(id));
            List<Rol> listRoles = rol_servicio.listRoles();
            model.addAttribute("listRoles", listRoles);
            return "user/user_update";
        }
        return "redirect:../listar";
    }

    @PreAuthorize("isAuthenticated() and hasAuthority('Eliminar_Usuario')")
    @GetMapping("/eliminar/{id}")
    public String deleteUser(@PathVariable(name="id") Long id) {
        usuario_servicio.delete(id);
        return "redirect:../listar";
    }

}
