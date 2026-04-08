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

    private static final List<String> PUBLIC_PATHS = Arrays.asList(
            "/",
            "/index.html",
            "/main.js",
            "/polyfills.js",
            "/styles.css",
            "/runtime.js",
            "/vendor.js",
            "/common.js",
            "/assets/",
            "/assets",
            "/favicon.ico",
            "/*.ico",
            "/*.png",
            "/*.svg",
            "/*.jpg",
            "/*.jpeg",
            "/auth/login",
            "/auth/refresh",
            "/users/register",
            "/api/public/"
    );

    private static final List<String> STATIC_EXTENSIONS = Arrays.asList(
            ".js", ".css", ".html", ".ico", ".png", ".jpg", ".jpeg", ".svg", ".gif", ".webp",
            ".json", ".xml", ".txt", ".pdf", ".woff", ".woff2", ".ttf", ".eot"
    );

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
        String path = request.getRequestURI();

        return isPublicPath(path);
    }

    private boolean isPublicPath(String path) {
        if (PUBLIC_PATHS.contains(path)) {
            return true;
        }

        if (path.startsWith("/api/public/")) {
            return true;
        }

        for (String extension : STATIC_EXTENSIONS) {
            if (path.endsWith(extension)) {
                return true;
            }
        }

        if (path.startsWith("/assets/")) {
            return true;
        }

        if (path.isEmpty() || path.equals("/")) {
            return true;
        }

        return false;
    }

    @Override
    @NullMarked
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String path = request.getRequestURI();

            if (isPublicPath(path)) {
                filterChain.doFilter(request, response);
                return;
            }

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