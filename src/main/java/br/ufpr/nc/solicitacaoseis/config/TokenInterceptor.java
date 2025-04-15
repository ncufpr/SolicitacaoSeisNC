//package br.ufpr.nc.solicitacaoseis.config;
//
//import jakarta.servlet.http.HttpServletRequest;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ModelAttribute;
//
//@ControllerAdvice
//public class TokenInterceptor {
//
//    @Autowired
//    private TokenService tokenService;
//    private static final Logger log = LoggerFactory.getLogger(TokenInterceptor.class);
//    @ModelAttribute
//    public void checkToken(HttpServletRequest request) {
//        if (request.getRequestURI().contains("/sucesso")) {
//            String token = request.getParameter("token");
//            log.info("Token acessado: {}", token);
//        }
//    }
//}