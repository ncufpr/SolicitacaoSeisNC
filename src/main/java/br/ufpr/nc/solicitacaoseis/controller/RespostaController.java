package br.ufpr.nc.solicitacaoseis.controller;


import br.ufpr.nc.solicitacaoseis.config.JwtTokenService;
import br.ufpr.nc.solicitacaoseis.dto.RespostaSolicitacaoDTO;
import br.ufpr.nc.solicitacaoseis.dto.SolicitacaoDTO;
import br.ufpr.nc.solicitacaoseis.entity.*;
import br.ufpr.nc.solicitacaoseis.service.*;
import br.ufpr.nc.solicitacaoseis.util.FilePaths;
import br.ufpr.nc.solicitacaoseis.util.Mapper;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.time.LocalDateTime;
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
    private NovaSolicitacaoFacade novaSolicitacaoFacade;

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
            solicitacaoDTO.setSolicitacao("");
            if (!model.containsAttribute("solicitacao")) {
                model.addAttribute("solicitacao", solicitacaoDTO);
            }
            model.addAttribute("respostaDTO", respostaDTOOpt.get());
//            System.out.println(respostaDTOOpt);
            return "resposta";
        }



        return "error/error";
    }

    @PostMapping("/avaliar")
    public String avaliarAtendimento(
            @Valid @ModelAttribute("respostaDTO") RespostaSolicitacaoDTO respostaDTO,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("msgError", "Erro: " +
                    result.getAllErrors().get(0).getDefaultMessage());
            return "redirect:/resposta/" + respostaDTO.getCodigoResposta();
        }

        boolean sucesso = respostaSolicitacaoService.avaliarResposta(
                respostaDTO.getIdRespostaSolicitacao(),
                respostaDTO.getAvaliacao(),
                respostaDTO.getComentario()
        );

        if (sucesso) {
            redirectAttributes.addFlashAttribute("msgSuccess", "Avaliação registrada com sucesso!");
        } else {
            redirectAttributes.addFlashAttribute("msgError", "Erro ao registrar avaliação.");
        }

        return "redirect:/resposta/" + respostaDTO.getCodigoResposta();
    }

    @PostMapping("/respostaSolicitacao")
    public String respostaSolicitacao(@Valid @ModelAttribute("solicitacao") SolicitacaoDTO solicitacao,
                                      BindingResult result,
                                      HttpSession session,
                                      RedirectAttributes redirectAttributes) {

        Optional<RespostaSolicitacao> respostaSolicitacao =
                respostaSolicitacaoService.findByIdSolicitacao(solicitacao.getIdSolicitacao());

        String redirectUrl = "redirect:/resposta/" + respostaSolicitacao.get().getCodigoResposta();

        // Se houver erro de validação no formulário
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("solicitacao", solicitacao);
            redirectAttributes.addFlashAttribute("abrirFormulario", true);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.solicitacao", result);
            return redirectUrl;
        }

        try {
            novaSolicitacaoFacade.processarNovaSolicitacao(solicitacao, session, true);
            return "redirect:/form/solicitacao/sucesso";

        } catch (IllegalArgumentException | IllegalStateException ex) {
            String mensagem;
            if (ex instanceof IllegalStateException) {
                mensagem = "Você já enviou uma solicitação para este concurso e assunto. Aguarde a resposta.";
            } else {
                mensagem = ex.getMessage();
            }

            redirectAttributes.addFlashAttribute("erro", mensagem);
            redirectAttributes.addFlashAttribute("abrirFormulario", true);
            redirectAttributes.addFlashAttribute("solicitacao", solicitacao);
            return redirectUrl;

        } catch (Exception ex) {
            // Falha inesperada → rollback garantido pelo @Transactional
            redirectAttributes.addFlashAttribute("erro", "Erro interno ao processar a solicitação. Tente novamente.");
            return redirectUrl;
        }
    }


}
