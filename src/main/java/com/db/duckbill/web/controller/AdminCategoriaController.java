package com.db.duckbill.web.controller;

import com.db.duckbill.domain.entity.Categoria;
import com.db.duckbill.service.CategoriaService;
import com.db.duckbill.web.dto.CategoriaForm;
import com.db.duckbill.web.exception.CategoriaEmUsoException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/categorias")
@RequiredArgsConstructor
public class AdminCategoriaController {
    private final CategoriaService categoriaService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("categorias", categoriaService.listar());
        model.addAttribute("form", new CategoriaForm());
        return "admin/categorias";
    }

    @PostMapping
    public String criar(@org.springframework.web.bind.annotation.ModelAttribute("form") @Valid CategoriaForm form,
                        BindingResult result,
                        Model model,
                        RedirectAttributes redirect) {
        if (result.hasErrors()) {
            model.addAttribute("categorias", categoriaService.listar());
            return "admin/categorias";
        }
        try {
            categoriaService.criar(new Categoria(null, form.getNome().trim()));
            redirect.addFlashAttribute("success", "Categoria criada com sucesso.");
        } catch (DataIntegrityViolationException ex) {
            redirect.addFlashAttribute("error", "Categoria já existe.");
        }
        return "redirect:/admin/categorias";
    }

    @PostMapping("/{id}/delete")
    public String deletar(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            categoriaService.deletar(id);
            redirect.addFlashAttribute("success", "Categoria excluída com sucesso.");
        } catch (CategoriaEmUsoException ex) {
            redirect.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/admin/categorias";
    }
}
