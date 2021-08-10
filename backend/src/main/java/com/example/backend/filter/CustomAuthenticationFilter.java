package com.example.backend.filter;

import com.example.backend.service.UserLoginRequest;
import com.example.backend.service.UserService;
import com.example.backend.util.JWTUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final UserService userService;

    @Autowired
    public CustomAuthenticationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, UserService userService){
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        // for the url encoded form as input
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // for json input type
        ObjectMapper mapper = new ObjectMapper();
        UserLoginRequest user = null;
        try {
            user = mapper.readValue(request.getInputStream(), UserLoginRequest.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        UsernamePasswordAuthenticationToken authenticationToken;
        if (user == null)
            authenticationToken = new UsernamePasswordAuthenticationToken("", "");
        else
            authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        User user = (User)authResult.getPrincipal();
        String token = jwtUtil.generateToken(user.getUsername());

        com.example.backend.models.User dbUser = userService.getUser(user.getUsername());
        HashMap<String, Object> responseMap = new HashMap<>();
        responseMap.put("token", token);
        responseMap.put("user", dbUser);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        new ObjectMapper().writeValue(response.getOutputStream(), responseMap);
    }
}
