//package com.kaladevi.shield.config;
//
//import com.kaladevi.shield.entity.UserDetailsEntity;
//import com.kaladevi.shield.model.UserDetailsResponse;
//import com.kaladevi.shield.service.JwtTokenManager;
//import com.kaladevi.shield.service.LoginService;
//import io.jsonwebtoken.Claims;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.web.filter.OncePerRequestFilter;
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.Objects;
//
//public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {
//
//    private final JwtConfig jwtConfig;
//    private JwtTokenManager tokenProvider;
//    private LoginService userService;
//    private UserDetailsEntity userDetailsEntity;
//
//    public JwtTokenAuthenticationFilter(
//            JwtConfig jwtConfig,
//            JwtTokenManager tokenProvider,
//            LoginService userService,UserDetailsEntity userDetailsEntity) {
//
//        this.jwtConfig = jwtConfig;
//        this.tokenProvider = tokenProvider;
//        this.userService = userService;
//        this.userDetailsEntity=userDetailsEntity;
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
//            throws ServletException, IOException {
//
//        // 1. get the authentication header. Tokens are supposed to be passed in the authentication header
//        String header = request.getHeader(jwtConfig.getHeader());
//
//        // 2. validate the header and check the prefix
//        if(header == null || !header.startsWith(jwtConfig.getPrefix())) {
//            chain.doFilter(request, response);  		// If not valid, go to the next filter.
//            return;
//        }
//
//        // If there is no token provided and hence the user won't be authenticated.
//        // It's Ok. Maybe the user accessing a public path or asking for a token.
//
//        // All secured paths that needs a token are already defined and secured in config class.
//        // And If user tried to access without access token, then he won't be authenticated and an exception will be thrown.
//
//        // 3. Get the token
//        String token = header.replace(jwtConfig.getPrefix(), "");
//
//        if(tokenProvider.validateToken(token)) {
//            Claims claims = tokenProvider.getClaimsFromJWT(token);
//            String username = claims.getSubject();
//          //  UsernamePasswordAuthenticationToken auth1 = new UsernamePasswordAuthenticationToken(userDetailsEntity, null, null);
//            //auth1.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//
//
//            UsernamePasswordAuthenticationToken auth =
//                    userService.findByUsername(username)
//                            .map(UserDetailsEntity::new)
//                            .map(userDetails -> {
//                                UsernamePasswordAuthenticationToken authentication =
//                                        new UsernamePasswordAuthenticationToken(
//                                                userDetails, null, userDetails.getAuthorities());
//                                authentication
//                                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//
//                                return authentication;
//                            })
//                            .orElse(null);
//
//            SecurityContextHolder.getContext().setAuthentication(auth);
//        } else {
//            SecurityContextHolder.clearContext();
//        }
//
//        // go to the next filter in the filter chain
//        chain.doFilter(request, response);
//    }
//
//}
