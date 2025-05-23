package br.ufpr.nc.solicitacaoseis.controller;


import br.ufpr.nc.solicitacaoseis.config.JwtTokenService;
import br.ufpr.nc.solicitacaoseis.dto.SolicitacaoDTO;
import br.ufpr.nc.solicitacaoseis.en.TipoEventoSolicitacao;
import br.ufpr.nc.solicitacaoseis.entity.*;
import br.ufpr.nc.solicitacaoseis.service.*;
import br.ufpr.nc.solicitacaoseis.util.Mapper;
import br.ufpr.nc.solicitacaoseis.util.Util;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/form")
@RequiredArgsConstructor
public class SolicitacaoController {

    private final ConcursoService concursoService;
    private final TipoAssuntoService tipoAssuntoService;
    private final EstadosService estadosService;
    private final StatusService statusService;
    private final PrioridadeService prioridadeService;
    private final SolicitacaoService solicitacaoService;
    private final Mapper mapper;
    private final Util util;
    private final JwtTokenService tokenService;

    private final SolicitacaoRespostaHistService solicitacaoRespostaHistService;

    @GetMapping
    public String showForm(Model model) {
        model.addAttribute("solicitacao", model.getAttribute("solicitacao") != null ? model.getAttribute("solicitacao") : new SolicitacaoDTO());
        model.addAttribute("concursos", concursoService.findAllConcursosAtivos());
        model.addAttribute("assuntos", tipoAssuntoService.findAll());
        model.addAttribute("estados", estadosService.findAll());
        return "form";
    }

    @PostMapping
    public String postForm(@Valid @ModelAttribute("solicitacao") SolicitacaoDTO solicitacao, BindingResult result,
                           @RequestParam(required = false) Long idSolicitacao,
                           HttpSession session, RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("solicitacao", solicitacao);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.solicitacao", result);
            return "redirect:/form";
        }
        System.out.println(idSolicitacao);
        Optional<Status> status = statusService.getStatus(6);
        Optional<Concurso> concurso = concursoService.findConcursoById(Long.parseLong(solicitacao.getConcursoId()));
        Optional<TipoAssunto> tipoAssunto = tipoAssuntoService.findById(Long.parseLong(solicitacao.getTipoAssunto()));
        Optional<Prioridade> prioridade = prioridadeService.findById(1);

        if (status.isEmpty() || concurso.isEmpty() || tipoAssunto.isEmpty() || prioridade.isEmpty()) {
            log.error("Erro ao criar solicitação: um dos dados obrigatórios está ausente");
            redirectAttributes.addFlashAttribute("erro", "Erro ao criar solicitação.");
            return "redirect:/form";
        }

        Solicitacao novaSolicitacao = mapper.toSolicitacaoToEntity(solicitacao, status.get(), tipoAssunto.get(), prioridade.get(), concurso.get());
        novaSolicitacao.setDataSolicitacao(LocalDateTime.now());

        if (!solicitacaoService.findByCpfSolicitacaoidConcursoidTipoAssuntoidStatus(novaSolicitacao).isEmpty()) {
            redirectAttributes.addFlashAttribute("solicitacao", solicitacao);
            redirectAttributes.addFlashAttribute("solicitacaoJaEnviada", "Essa solicitação já foi enviada! Por favor aguarde!");
            return "redirect:/form";
        }

        Solicitacao solicitacaoSalva = solicitacaoService.salvar(novaSolicitacao);
        String token = tokenService.gerarToken(solicitacaoSalva.getIdSolicitacao());
        session.setAttribute("token", token);

//        solicitacaoRespostaHist.save(solicitacaoSalva, new RespostaSolicitacao(), String.valueOf(TipoEventoSolicitacao.SOLICITACAO_ENVIADA), LocalDateTime.now());

        SolicitacaoRespostaHist sr = new SolicitacaoRespostaHist();
        sr.setSolicitacao(solicitacaoSalva);
        sr.setRespostaSolicitacao(new RespostaSolicitacao());
        sr.setStatus(String.valueOf(TipoEventoSolicitacao.SOLICITACAO_ENVIADA));
        boolean solicitacaoBoolean = false;
        if(idSolicitacao != null) {
            solicitacaoBoolean = true;
        }


        if(solicitacaoBoolean) {
            solicitacaoBoolean = true;
            Solicitacao solicitacao1 = solicitacaoService.findById(idSolicitacao);
            solicitacaoRespostaHistService.registrarEventoSolicitacao(solicitacao1, solicitacaoSalva, solicitacaoBoolean);
        } else {
            solicitacaoRespostaHistService.registrarEventoSolicitacao(novaSolicitacao, solicitacaoSalva, solicitacaoBoolean);
        }





        return "redirect:/form/solicitacao/sucesso";
    }


    @GetMapping("/solicitacao/sucesso")
    public String showSuccessPage(HttpSession session, Model model) {
        String token = (String) session.getAttribute("token");

            log.info("Token: {}", token);

        if (token == null || token.isBlank()) {
            log.warn("Tentativa de acesso sem token válido");
            return "redirect:/form";
        }

        Optional<Long> idOptional = tokenService.validarToken(token);
        log.info("idOptional: {}", idOptional);
        if (idOptional.isEmpty()) {
            log.warn("Token inválido ou expirado");
            return "redirect:/form";
        }

        Optional<Solicitacao> solicitacaoOpt = Optional.ofNullable(solicitacaoService.findById(idOptional.get()));
        if (solicitacaoOpt.isEmpty()) {
            log.warn("Solicitação não encontrada para ID: {}", idOptional.get());
            return "redirect:/form";
        }

        Solicitacao solicitacao = solicitacaoOpt.get();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String dataFormatada = solicitacao.getDataSolicitacao().format(formatter);
        String cpfFormatado = util.formatarCPF(String.valueOf(solicitacao.getCpf()));

        SolicitacaoDTO solicitacaoDTO = mapper.toSolicitacaoToDto(solicitacao);
        solicitacaoDTO.setCpf(cpfFormatado);

        model.addAttribute("solicitacao", solicitacaoDTO);
        model.addAttribute("id", solicitacao.getIdSolicitacao());
        model.addAttribute("data", dataFormatada);

        return "solicitacao";
    }

    @GetMapping("/reenviar/{id}")
    public String reenviarSolicitacao(@PathVariable Long id, Model model) {
        // Buscar a solicitação anterior no banco
        Optional<Solicitacao> solicitacaoAntiga = Optional.ofNullable(solicitacaoService.findById(id));
        if (solicitacaoAntiga.isEmpty()) {
            // Redireciona com mensagem de erro ou retorna página de erro
            return "redirect:/erro";
        }

        Solicitacao anterior = solicitacaoAntiga.get();
        SolicitacaoDTO dto = new SolicitacaoDTO();
        dto.setNome(anterior.getNome());
        dto.setEmail(anterior.getEmail());
        dto.setSolicitacao(anterior.getSolicitacao());
        // Outros campos conforme necessário

        model.addAttribute("solicitacao", dto);
        model.addAttribute("concursos", concursoService.findAllConcursosAtivos());
        model.addAttribute("assuntos", tipoAssuntoService.findAll());
        model.addAttribute("estados", estadosService.findAll());

        return "form";
    }



}
