package br.ufpr.nc.solicitacaoseis.controller;


import br.ufpr.nc.solicitacaoseis.dto.RespostaSolicitacaoDTO;
import br.ufpr.nc.solicitacaoseis.dto.SolicitacaoDTO;
import br.ufpr.nc.solicitacaoseis.entity.Solicitacao;
import br.ufpr.nc.solicitacaoseis.service.RespostaSolicitacaoService;
import br.ufpr.nc.solicitacaoseis.util.FilePaths;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Controller
@RequestMapping("/resposta")
public class RespostaController {

    @Autowired
    private RespostaSolicitacaoService respostaSolicitacaoService;

    @GetMapping("/{codigo}")
    public String getResposta(@PathVariable String codigo, HttpSession session, Model model) {
        System.out.println(codigo);
        Optional<RespostaSolicitacaoDTO> respostaDTOOpt = respostaSolicitacaoService.buscarDtoPorCodigo(codigo);
        System.out.println(respostaDTOOpt);
        if (respostaDTOOpt.isPresent()) {
            model.addAttribute("respostaDTO", respostaDTOOpt.get());
            return "resposta";
        }

        return "error/error";
    }



}
