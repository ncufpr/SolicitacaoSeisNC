package br.ufpr.nc.solicitacaoseis.controller;


import br.ufpr.nc.solicitacaoseis.dto.RespostaSolicitacaoDTO;
import br.ufpr.nc.solicitacaoseis.dto.SolicitacaoDTO;
import br.ufpr.nc.solicitacaoseis.entity.RespostaSolicitacao;
import br.ufpr.nc.solicitacaoseis.entity.Solicitacao;
import br.ufpr.nc.solicitacaoseis.service.*;
import br.ufpr.nc.solicitacaoseis.util.FilePaths;
import br.ufpr.nc.solicitacaoseis.util.Mapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Controller
@RequestMapping("/resposta")
public class RespostaController {

    @Autowired
    private RespostaSolicitacaoService respostaSolicitacaoService;
    @Autowired
    private ConcursoService concursoService;
    @Autowired
    private TipoAssuntoService tipoAssuntoService;
    @Autowired
    private EstadosService estadosService;
    @Autowired
    private SolicitacaoService solicitacaoService;
    @Autowired
    private Mapper mapper;

    @GetMapping("/{codigo}")
    public String getResposta(@PathVariable String codigo, HttpSession session, Model model) {
        model.addAttribute("concursos", concursoService.findAllConcursosAtivos());
        model.addAttribute("assuntos", tipoAssuntoService.findAll());
        model.addAttribute("estados", estadosService.findAll());
//        System.out.println(codigo);

        Optional<RespostaSolicitacaoDTO> respostaDTOOpt = respostaSolicitacaoService.buscarDtoPorCodigo(codigo);

        if (respostaDTOOpt.isPresent()) {
            Solicitacao solicitacao = solicitacaoService.findById(respostaDTOOpt.get().getIdSolicitacao());
            SolicitacaoDTO solicitacaoDTO = mapper.toSolicitacaoToDto(solicitacao);

            model.addAttribute("solicitacao", solicitacaoDTO);
            model.addAttribute("respostaDTO", respostaDTOOpt.get());
//            System.out.println(respostaDTOOpt);
            return "resposta";
        }



        return "error/error";
    }

    @PostMapping("/avaliar")
    public String avaliarAtendimento(@RequestParam Long idRespostaSolicitacao, @RequestParam int nota, RedirectAttributes redirectAttributes) {
        // Salvar avaliação no banco (associar à solicitação)
//        atendimentoService.salvarAvaliacao(idSolicitacao, nota);
        Optional<RespostaSolicitacao> respostaSolicitacao = respostaSolicitacaoService.findById(idRespostaSolicitacao);


        boolean sucesso = respostaSolicitacaoService.avaliarResposta(idRespostaSolicitacao, nota);
        if (sucesso) {
            redirectAttributes.addFlashAttribute("msgSuccess", "Avaliação registrada com sucesso!");
        } else {
            redirectAttributes.addFlashAttribute("msgError", "Erro ao registrar avaliação.");
        }

        System.out.println(idRespostaSolicitacao);
        System.out.println(nota);
        return "redirect:/resposta/" + respostaSolicitacao.get().getCodigoResposta();
    }

}
