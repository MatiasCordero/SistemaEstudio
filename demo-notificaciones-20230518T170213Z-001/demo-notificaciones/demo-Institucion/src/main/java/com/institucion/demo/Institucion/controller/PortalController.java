package com.institucion.demo.Institucion.controller;

import com.institucion.demo.Institucion.Excepciones.MiException;
import com.institucion.demo.Institucion.entities.Permisos;
import com.institucion.demo.Institucion.entities.Rol;
import com.institucion.demo.Institucion.entities.Usuario;
import com.institucion.demo.Institucion.repository.BoletinesPDF_Reporitorio;
import com.institucion.demo.Institucion.repository.Rol_Repositorio;
import com.institucion.demo.Institucion.repository.Usuario_Repositorio;
import com.institucion.demo.Institucion.services.Rol_Servicio;
import com.institucion.demo.Institucion.services.Usuario_Servicio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/")
public class PortalController {

    private static final Logger logger = LoggerFactory.getLogger(PortalController.class);
    @Autowired
    BoletinesPDF_Reporitorio boletinesPDFReporitorio;
    @Autowired
    private Usuario_Servicio usuario_servicio;
    @Autowired
    private Usuario_Repositorio usuario_repositorio;
    @Autowired
    private Rol_Servicio rol_servicio;
    @Autowired
    private Rol_Repositorio rol_repositorio;

    @GetMapping("/")
    public String index() {
        return "redirect:/inicio";
    }

    @RequestMapping("/registrar")
    public String register(Model model) {
        model.addAttribute("user", new Usuario());

        return "register.html";
    }

    public void myMethod() {
        logger.info("Este es un mensaje de log de prueba");
    }



    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, ModelMap model) {
        if (error != null) {
            model.put("error", "Usuario o Contraseña invalidos");
        }
        myMethod();

        return "Login/Inicio/singin.html";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/inicio")
    public String inicio( ModelMap model, HttpSession session) {


        List<Usuario> users = usuario_servicio.listAll();
        model.addAttribute("users", users);
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        model.addAttribute("notificar", false);
        model.addAttribute("cargarNotas", false);

        for (Rol r : logueado.getRoles()) {
            for (Permisos p : r.getPermisos()) {
                if(p.getPermiso().equals("Notificar")){
                    model.addAttribute("notificar", true);
                }
                if(p.getPermiso().equals("carga_Notas")){
                    model.addAttribute("cargarNotas", true);
                }
            }
        }

        if(logueado.isEstado() == false){
            model.put("error", "Usuario no habilitado");

            return "signin.html";
        }
        return "Login/Inicio/Inicio.html";
    }

    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }


}

