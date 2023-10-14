package br.com.npgsoftwares.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.npgsoftwares.todolist.user.IUserRepository;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {

    @Autowired
    private IUserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

                var servletPath = request.getServletPath();

                if (servletPath.startsWith("/tasks/")){

                    var authorization = request.getHeader("Authorization");
    
                    var passEncode = authorization.substring(5).trim();
    
                    byte[] passDecode = Base64.getDecoder().decode(passEncode);
    
                    var authString = new String(passDecode);
    
                    String[] credentials = authString.split(":");
                    String username = credentials[0];
                    String password = credentials[1];
    
                    // Verifica se o usuário existe
                    var user = this.userRepository.findByUsername(username);
                    if(user == null){
                        response.sendError(401, "Usuário não encontrado no sistema");
                    } else {
                        
                        // Verifica se a senha existe
                        var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
                        if(passwordVerify.verified){
                            request.setAttribute("idUser", user.getId());
                            filterChain.doFilter(request, response);
                        } else {
                            response.sendError(401, "A senha está incorreta");
                        }
                    }

                } else {
                    filterChain.doFilter(request, response);
                }       
    }
}
