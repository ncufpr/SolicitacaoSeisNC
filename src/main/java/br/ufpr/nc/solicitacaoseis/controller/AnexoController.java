package br.ufpr.nc.solicitacaoseis.controller;

import br.ufpr.nc.solicitacaoseis.util.FilePaths;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.nio.file.Path;

@RestController
@RequestMapping("/anexos")
public class AnexoController {
    @GetMapping("anexosResposta/{id}/{nomeArquivo}")
    public ResponseEntity<Resource> baixarAnexoResposta(@PathVariable String id, @PathVariable String nomeArquivo) {
        try {
            Path caminhoArquivo = Path.of(FilePaths.BASE_PATH_RESPOSTA + id, nomeArquivo);
            Resource resource = new UrlResource(caminhoArquivo.toUri());

            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .header("Content-Disposition", "attachment; filename=\"" + nomeArquivo + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
