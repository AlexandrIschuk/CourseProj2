package ru.ssau.carshwebcourse.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NullMarked;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import ru.ssau.carshwebcourse.exceptionHandler.InvalidTokenException;
import ru.ssau.carshwebcourse.exceptionHandler.TokenCreatedTimeException;
import ru.ssau.carshwebcourse.service.CustomUserDetailsService;
import ru.ssau.carshwebcourse.service.TokenService;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class JwtFilter extends OncePerRequestFilter {
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String HEADER_NAME = "Authorization";

    private final TokenService tokenService;
    private CustomUserDetailsService customUserDetailsService;
    private final HandlerExceptionResolver resolver;



    public JwtFilter(TokenService tokenService,
                     @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.tokenService = tokenService;
        this.resolver = resolver;
    }

    @Autowired
    public void setCustomUserDetailsService(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();

        return !path.startsWith("/api/") ||
                path.equals("/api/auth/login") ||
                path.equals("/api/auth/refresh") ||
                path.equals("/api/users/register");
    }



    @Override
    @NullMarked
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {

            String authHeader = request.getHeader(HEADER_NAME);

            if (authHeader == null) {
                throw new InvalidTokenException("Token is missing or not Access token");
            }

            if (!authHeader.startsWith(BEARER_PREFIX)) {
                throw new InvalidTokenException("Invalid authorization header format");
            }

            String jwt = authHeader.substring(BEARER_PREFIX.length());
            Map<String, Object> payload = tokenService.getDecodePayload(jwt);

            if (!payload.containsKey("roles")) {
                throw new InvalidTokenException("Token is not Access token");
            }

            String email = payload.get("email").toString();
            long iat = Long.parseLong(payload.get("iat").toString());

            if (tokenService.getCreateTime() == null) {
                throw new TokenCreatedTimeException("Server restart, please login again");
            }

            if (tokenService.getCreateTime() != iat) {
                throw new InvalidTokenException("The token is old");
            }

            UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );
            context.setAuthentication(authToken);
            SecurityContextHolder.setContext(context);

            filterChain.doFilter(request, response);

        } catch (InvalidTokenException | NoSuchAlgorithmException | InvalidKeyException |
                 TokenCreatedTimeException | ArrayIndexOutOfBoundsException e) {
            resolver.resolveException(request, response, null, e);
        }
    }
}