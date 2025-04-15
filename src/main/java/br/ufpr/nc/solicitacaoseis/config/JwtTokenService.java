package br.ufpr.nc.solicitacaoseis.config;//package br.ufpr.nc.solicitacaoseis.config;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cache.Cache;
//import org.springframework.cache.CacheManager;
//import org.springframework.cache.annotation.CachePut;
//import org.springframework.cache.annotation.Cacheable;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;
//import java.util.UUID;
//import java.util.concurrent.TimeUnit;
//
//import java.util.Optional;
//import java.util.UUID;
//
//@Service
//public class TokenService {
//
//    private final CacheManager cacheManager;
//    private static final Logger log = LoggerFactory.getLogger(TokenService.class);
//
//    @Autowired
//    public TokenService(CacheManager cacheManager) {
//        this.cacheManager = cacheManager;
//    }
//
//    // Método modificado para operação atômica
//    public String gerarEArmazenarToken(Long idSolicitacao) {
//        String token = "SOLIC-" + UUID.randomUUID().toString();
//        Cache cache = cacheManager.getCache("tokensAcesso");
//        if (cache != null) {
//            cache.put(token, idSolicitacao);
//            log.debug("Token armazenado - Token: {}, ID: {}", token, idSolicitacao);
//
//            // Verificação imediata para debug
//            Cache.ValueWrapper verification = cache.get(token);
//            log.debug("Verificação pós-armazenamento: {}", verification != null ? "SUCESSO" : "FALHA");
//        }
//        return token;
//    }
//
//
//    public Optional<Long> recuperarIdPorToken(String token) {
//        try {
//            Cache cache = cacheManager.getCache("tokensAcesso");
//            if (cache != null) {
//                // Método alternativo sem ValueWrapper
//                Long id = cache.get(token, Long.class);
//                if (id != null) {
//                    log.debug("Token recuperado - Token: {}, ID: {}", token, id);
//                    return Optional.of(id);
//                }
//            }
//            log.warn("Token não encontrado - Token: {}", token);
//            return Optional.empty();
//        } catch (Exception e) {
//            log.error("Erro ao recuperar token", e);
//            return Optional.empty();
//        }
//    }
//
//    public String gerarNovoToken() {
//        String token = "SOLIC-" + UUID.randomUUID();
//        log.debug("Novo token gerado: {}", token);
//        return token;
//    }
//}

//package br.ufpr.nc.solicitacaoseis.config;//package br.ufpr.nc.solicitacaoseis.config;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cache.Cache;
//import org.springframework.cache.CacheManager;
//import org.springframework.cache.annotation.CachePut;
//import org.springframework.cache.annotation.Cacheable;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;
//import java.util.UUID;
//import java.util.concurrent.TimeUnit;
//
//import java.util.Optional;
//import java.util.UUID;
//
//@Service
//public class TokenService {
//
//    private final CacheManager cacheManager;
//    private static final Logger log = LoggerFactory.getLogger(TokenService.class);
//
//    @Autowired
//    public TokenService(CacheManager cacheManager) {
//        this.cacheManager = cacheManager;
//    }
//
//    // Método modificado para operação atômica
//    public String gerarEArmazenarToken(Long idSolicitacao) {
//        String token = "SOLIC-" + UUID.randomUUID().toString();
//        Cache cache = cacheManager.getCache("tokensAcesso");
//        if (cache != null) {
//            cache.put(token, idSolicitacao);
//            log.debug("Token armazenado - Token: {}, ID: {}", token, idSolicitacao);
//
//            // Verificação imediata para debug
//            Cache.ValueWrapper verification = cache.get(token);
//            log.debug("Verificação pós-armazenamento: {}", verification != null ? "SUCESSO" : "FALHA");
//        }
//        return token;
//    }
//
//
//    public Optional<Long> recuperarIdPorToken(String token) {
//        try {
//            Cache cache = cacheManager.getCache("tokensAcesso");
//            if (cache != null) {
//                // Método alternativo sem ValueWrapper
//                Long id = cache.get(token, Long.class);
//                if (id != null) {
//                    log.debug("Token recuperado - Token: {}, ID: {}", token, id);
//                    return Optional.of(id);
//                }
//            }
//            log.warn("Token não encontrado - Token: {}", token);
//            return Optional.empty();
//        } catch (Exception e) {
//            log.error("Erro ao recuperar token", e);
//            return Optional.empty();
//        }
//    }
//
//    public String gerarNovoToken() {
//        String token = "SOLIC-" + UUID.randomUUID();
//        log.debug("Novo token gerado: {}", token);
//        return token;
//    }
//}

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

@Service
public class JwtTokenService {
    private static final Logger log = LoggerFactory.getLogger(JwtTokenService.class);

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration; // Tempo de expiração em segundos

    private Key key;
    private SecretKey aesKey;
    private static final String AES_ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String AES_KEY_SPEC = "AES";
    private static final byte[] IV = "1234567890123456".getBytes(); // IV fixo (idealmente, gere um aleatório)

    @PostConstruct
    public void init() {
//        if (secret.length() < 48) throw new IllegalStateException("Chave muito curta para HS384");
        this.key = Keys.hmacShaKeyFor(secret.getBytes());

        // Pegamos os primeiros 16 bytes da chave secreta para AES (ajuste conforme necessário)
        this.aesKey = new SecretKeySpec(secret.substring(0, 16).getBytes(), AES_KEY_SPEC);
    }

    public String gerarToken(Long id) {
        try {
            LocalDateTime agora = LocalDateTime.now();
            LocalDateTime expiracao = agora.plusSeconds(expiration); // Expira em X segundos
            Date dataExpiracao = Date.from(expiracao.atZone(ZoneId.systemDefault()).toInstant());

            String encryptedId = encryptAES(id.toString());

            return Jwts.builder()
                    .setSubject(encryptedId)
                    .setExpiration(dataExpiracao)
                    .signWith(key)
                    .compact();
        } catch (Exception e) {
            log.error("Erro ao criptografar ID", e);
            return null;
        }
    }

    public Optional<Long> validarToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith((SecretKey) key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            if (claims.getExpiration().before(new Date())) {
                log.warn("Token expirado");
                return Optional.empty();
            }

            String decryptedId = decryptAES(claims.getSubject());

            return Optional.of(Long.parseLong(decryptedId));
        } catch (Exception e) {
            log.error("Falha ao validar token: " + e.getMessage());
            return Optional.empty();
        }
    }

    private String encryptAES(String value) throws Exception {
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, aesKey, new IvParameterSpec(IV));
        byte[] encrypted = cipher.doFinal(value.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    private String decryptAES(String encrypted) throws Exception {
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, aesKey, new IvParameterSpec(IV));
        byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encrypted));
        return new String(decrypted);
    }
}