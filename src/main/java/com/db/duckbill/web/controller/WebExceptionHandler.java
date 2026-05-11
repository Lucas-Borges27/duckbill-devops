package com.db.duckbill.web.controller;

import com.db.duckbill.web.exception.CategoriaEmUsoException;
import com.db.duckbill.web.exception.AcessoNegadoException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice(annotations = Controller.class)
public class WebExceptionHandler {

    @ExceptionHandler(AcessoNegadoException.class)
    public String handleForbidden(AcessoNegadoException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "access-denied";
    }

    @ExceptionHandler({IllegalArgumentException.class, NoSuchElementException.class, CategoriaEmUsoException.class})
    public String handleBusiness(Exception ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneric(Exception ex, Model model) {
        model.addAttribute("error", "Ocorreu um erro inesperado. Tente novamente.");
        return "error";
    }
}
