package com.db.duckbill.web.controller;

import com.db.duckbill.service.CurrentUserService;
import com.db.duckbill.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.YearMonth;

@Controller
@RequestMapping("/app")
@RequiredArgsConstructor
public class AppDashboardController {
    private final DashboardService dashboardService;
    private final CurrentUserService currentUserService;

    @GetMapping("/dashboard")
    public String dashboard(@RequestParam(required = false) String mes, Model model) {
        var usuario = currentUserService.getUsuarioAtual();
        YearMonth ym = (mes == null || mes.isBlank()) ? YearMonth.now() : YearMonth.parse(mes);

        model.addAttribute("usuario", usuario);
        model.addAttribute("mes", ym.toString());
        model.addAttribute("totalMes", dashboardService.totalMes(usuario.getId(), ym));
        model.addAttribute("top3", dashboardService.top3Mes(usuario.getId(), ym));
        model.addAttribute("insights", dashboardService.insightsBasicos(usuario.getId(), ym));

        return "app/dashboard";
    }
}
