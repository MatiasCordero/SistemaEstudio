package com.institucion.demo.Institucion.controller;

import com.institucion.demo.Institucion.Excepciones.MiException;
import com.institucion.demo.Institucion.entities.Permisos;
import com.institucion.demo.Institucion.entities.Rol;
import com.institucion.demo.Institucion.services.Rol_Servicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/rol")
public class RolController {

    @Autowired
    private Rol_Servicio rol_servicio;


    @PreAuthorize("isAuthenticated() and hasAuthority('Rol_Ver_Privilegios')")
    @RequestMapping("/listar_p/{id}")
    public String listPermisos(@PathVariable(name = "id") Long id, Model model) {
        model.addAttribute("permisos", rol_servicio.permisos_get(id));

        return "roles/permisos_list.html";
    }

    @PreAuthorize("isAuthenticated() and hasAuthority('Mostrar_Rol')")
    @RequestMapping("/listar")
    public String listarUsuario(Model model){

        List<Rol> roles = rol_servicio.listRoles();
        model.addAttribute("roles", roles);

        return "roles/rol_list.html";
    }

    @PreAuthorize("isAuthenticated() and hasAuthority('Crear_Rol')")
    @RequestMapping("/crear")
    public String crearRol(Model model) {

        List<Permisos> listPermisos = rol_servicio.listPermisos();
        model.addAttribute("listPermisos", listPermisos);
        model.addAttribute("rol", new Rol());

        return "roles/crear_roles";
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/creado", method = RequestMethod.POST)
    public String crearRol(@ModelAttribute("rol") Rol rol, ModelMap model) throws MiException {
        try {
            rol_servicio.crearRol(rol);
        } catch (MiException e) {
            List<Permisos> listPermisos = rol_servicio.listPermisos();
            model.addAttribute("listPermisos", listPermisos);
            model.addAttribute("rol", new Rol());
            model.put("error", e.getMessage());
            return "roles/crear_roles";
        }
        return "redirect:../listar";
    }

    @PreAuthorize("isAuthenticated() and hasAuthority('Modificar_Rol')")
    @RequestMapping("/modificar/{id}")
    public String uploadUser(@PathVariable(name = "id") Long id, ModelMap model) {
        model.addAttribute("rol", rol_servicio.get(id));
        List<Permisos> permisosList = rol_servicio.listPermisos();
        model.addAttribute("permisosList", permisosList);
        return "roles/rol_update";
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/modificar/{id}", method = RequestMethod.POST)
    public String uploadUser(@PathVariable(name = "id") Long id, ModelMap model, @ModelAttribute("rol") Rol rol) throws MiException {
        try {
            rol_servicio.modificarRol(rol);

        } catch (MiException ex) {
            model.put("error", ex.getMessage());
            model.addAttribute("rol", rol_servicio.get(id));
            List<Permisos> permisosList = rol_servicio.listPermisos();
            model.addAttribute("permisosList", permisosList);
            return "roles/rol_update";
        }

        return "redirect:../listar";
    }

    @PreAuthorize("isAuthenticated() and hasAuthority('Eliminar_Rol')")
    @GetMapping("/eliminar/{id}")
    public String deleteRol(@PathVariable(name="id") Long id) {
        rol_servicio.delete(id);
        return "redirect:../listar";
    }
}
