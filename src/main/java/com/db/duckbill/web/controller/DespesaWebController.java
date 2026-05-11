package com.db.duckbill.web.controller;

import com.db.duckbill.domain.entity.Despesa;
import com.db.duckbill.domain.repo.CategoriaRepository;
import com.db.duckbill.service.CurrentUserService;
import com.db.duckbill.service.DespesaService;
import com.db.duckbill.web.dto.DespesaForm;
import com.db.duckbill.web.exception.AcessoNegadoException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/app/despesas")
@RequiredArgsConstructor
public class DespesaWebController {
    private final DespesaService despesaService;
    private final CategoriaRepository categoriaRepository;
    private final CurrentUserService currentUserService;

    @GetMapping("/nova")
    public String nova(@org.springframework.web.bind.annotation.RequestParam(required = false) String mes, Model model) {
        model.addAttribute("form", new DespesaForm());
        model.addAttribute("categorias", categoriaRepository.findAll());
        var usuario = currentUserService.getUsuarioAtual();
        java.time.YearMonth ym = (mes == null || mes.isBlank()) ? java.time.YearMonth.now() : java.time.YearMonth.parse(mes);
        model.addAttribute("mes", ym.toString());
        model.addAttribute("despesas", despesaService.listarMes(usuario.getId(), ym));
        return "app/despesas-nova";
    }

    @PostMapping("/nova")
    public String criar(@org.springframework.web.bind.annotation.ModelAttribute("form") @Valid DespesaForm form,
                        BindingResult result,
                        Model model,
                        RedirectAttributes redirect) {
        if (result.hasErrors()) {
            model.addAttribute("categorias", categoriaRepository.findAll());
            var usuario = currentUserService.getUsuarioAtual();
            java.time.YearMonth ym = java.time.YearMonth.now();
            model.addAttribute("mes", ym.toString());
            model.addAttribute("despesas", despesaService.listarMes(usuario.getId(), ym));
            return "app/despesas-nova";
        }
        var usuario = currentUserService.getUsuarioAtual();
        var categoria = categoriaRepository.findById(form.getCategoriaId())
            .orElseThrow(() -> new IllegalArgumentException("Categoria inválida"));

        Despesa despesa = new Despesa();
        despesa.setUsuario(usuario);
        despesa.setCategoria(categoria);
        despesa.setValor(form.getValor());
        despesa.setMoeda(form.getMoeda().toUpperCase());
        despesa.setDataCompra(form.getDataCompra());
        despesa.setDescricao(form.getDescricao());

        despesaService.criar(despesa);
        redirect.addFlashAttribute("success", "Despesa criada com sucesso.");
        return "redirect:/app/despesas/nova";
    }

    @GetMapping("/{id}/editar")
    public String editarForm(@org.springframework.web.bind.annotation.PathVariable Long id, Model model) {
        var usuario = currentUserService.getUsuarioAtual();
        Despesa despesa = despesaService.buscarPorId(id);
        if (!despesa.getUsuario().getId().equals(usuario.getId())) {
            throw new AcessoNegadoException("Acesso negado à despesa.");
        }
        DespesaForm form = new DespesaForm();
        form.setCategoriaId(despesa.getCategoria().getId());
        form.setValor(despesa.getValor());
        form.setMoeda(despesa.getMoeda());
        form.setDataCompra(despesa.getDataCompra());
        form.setDescricao(despesa.getDescricao());
        model.addAttribute("form", form);
        model.addAttribute("despesaId", id);
        model.addAttribute("categorias", categoriaRepository.findAll());
        return "app/despesas-editar";
    }

    @PostMapping("/{id}/editar")
    public String editar(@org.springframework.web.bind.annotation.PathVariable Long id,
                         @org.springframework.web.bind.annotation.ModelAttribute("form") @Valid DespesaForm form,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirect) {
        if (result.hasErrors()) {
            model.addAttribute("despesaId", id);
            model.addAttribute("categorias", categoriaRepository.findAll());
            return "app/despesas-editar";
        }
        var usuario = currentUserService.getUsuarioAtual();
        var categoria = categoriaRepository.findById(form.getCategoriaId())
            .orElseThrow(() -> new IllegalArgumentException("Categoria inválida"));

        Despesa dados = new Despesa();
        dados.setCategoria(categoria);
        dados.setValor(form.getValor());
        dados.setMoeda(form.getMoeda().toUpperCase());
        dados.setDataCompra(form.getDataCompra());
        dados.setDescricao(form.getDescricao());

        despesaService.atualizar(id, usuario.getId(), dados);
        redirect.addFlashAttribute("success", "Despesa atualizada com sucesso.");
        return "redirect:/app/despesas/nova";
    }

    @PostMapping("/{id}/delete")
    public String deletar(@org.springframework.web.bind.annotation.PathVariable Long id, RedirectAttributes redirect) {
        var usuario = currentUserService.getUsuarioAtual();
        despesaService.deletar(id, usuario.getId());
        redirect.addFlashAttribute("success", "Despesa excluída com sucesso.");
        return "redirect:/app/despesas/nova";
    }
}
