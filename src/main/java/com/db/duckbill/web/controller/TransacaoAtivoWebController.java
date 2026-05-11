package com.db.duckbill.web.controller;

import com.db.duckbill.service.CurrentUserService;
import com.db.duckbill.service.TransacaoAtivoService;
import com.db.duckbill.domain.repo.AtivoRepository;
import com.db.duckbill.web.dto.TransacaoAtivoDTO;
import com.db.duckbill.web.dto.TransacaoAtivoForm;
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
@RequestMapping("/app/transacoes")
@RequiredArgsConstructor
public class TransacaoAtivoWebController {
    private final TransacaoAtivoService transacaoAtivoService;
    private final AtivoRepository ativoRepository;
    private final CurrentUserService currentUserService;

    @GetMapping("/nova")
    public String nova(Model model) {
        var usuario = currentUserService.getUsuarioAtual();
        model.addAttribute("form", new TransacaoAtivoForm());
        model.addAttribute("ativos", ativoRepository.findAll());
        model.addAttribute("transacoes", transacaoAtivoService.listarPorUsuario(usuario.getId()));
        model.addAttribute("carteira", transacaoAtivoService.resumoCarteira(usuario.getId()));
        return "app/transacoes-nova";
    }

    @PostMapping("/nova")
    public String criar(@org.springframework.web.bind.annotation.ModelAttribute("form") @Valid TransacaoAtivoForm form,
                        BindingResult result,
                        Model model,
                        RedirectAttributes redirect) {
        if (result.hasErrors()) {
            var usuario = currentUserService.getUsuarioAtual();
            model.addAttribute("ativos", ativoRepository.findAll());
            model.addAttribute("transacoes", transacaoAtivoService.listarPorUsuario(usuario.getId()));
            model.addAttribute("carteira", transacaoAtivoService.resumoCarteira(usuario.getId()));
            return "app/transacoes-nova";
        }
        var usuario = currentUserService.getUsuarioAtual();
        TransacaoAtivoDTO dto = new TransacaoAtivoDTO(
            null,
            usuario.getId(),
            form.getAtivoId(),
            form.getTipo().toUpperCase(),
            form.getQtd(),
            form.getPreco(),
            form.getDataNegocio()
        );
        transacaoAtivoService.criar(dto);
        redirect.addFlashAttribute("success", "Transação criada com sucesso.");
        return "redirect:/app/transacoes/nova";
    }
}
