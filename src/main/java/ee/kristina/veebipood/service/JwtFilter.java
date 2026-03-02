package ee.kristina.veebipood.service;

import ee.kristina.veebipood.entity.Person;
import ee.kristina.veebipood.entity.Role;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header != null && header.startsWith("Bearer ")) {
            //tokeni kontroll
            String token = header.replace("Bearer ", "");
            Person person = jwtService.validateToken(token);

            // rolli kontroll kes mida naeb
            List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
            if (person.getRole().equals(Role.ADMIN)) {
                grantedAuthorities.add(new SimpleGrantedAuthority("ADMIN"));
            }
            if (person.getRole().equals(Role.SUPERADMIN)) {
                grantedAuthorities.add(new SimpleGrantedAuthority("ADMIN"));
                grantedAuthorities.add(new SimpleGrantedAuthority("SUPERADMIN"));
            }

            Authentication authentication = new UsernamePasswordAuthenticationToken(person.getId(), person.getEmail(), grantedAuthorities);
            SecurityContextHolder.getContext().setAuthentication(authentication); // laseb sisse
        }

        filterChain.doFilter(request, response); // on olemas ka originaalis
    }
}
