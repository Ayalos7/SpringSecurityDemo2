package com.jb.mySpringSecurity.JWT;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

@RequiredArgsConstructor
public class JwtUserAndPassFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            UserModel userModel = new ObjectMapper().readValue(request.getInputStream(), UserModel.class);
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userModel.getUserName(),userModel.getPassword()
            );
            //check if token is valid
            Authentication returnAuth = getAuthenticationManager().authenticate(authentication);
            return returnAuth;
        } catch (IOException err){
            System.out.println(err.getMessage());
            throw new RuntimeException(err);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        //using JWT library
        String myKey = "UkXp2s5v8y/B?E(H+MbQeThWmYq3t6w9z$C&F)J@NcRfUjXn2r4u7x!A%D*G-KaP";
        String token = Jwts.builder()
                .setSubject(authResult.getName())
                //payload
                .claim("authorities",authResult.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusWeeks(2)))
                .signWith(Keys.hmacShaKeyFor(myKey.getBytes()))
                .compact();
        response.addHeader("Authorization","Bearer "+token);
    }
}
