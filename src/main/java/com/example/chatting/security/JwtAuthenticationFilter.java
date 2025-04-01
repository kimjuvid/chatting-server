package com.example.chatting.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JWTProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
//            String token = authHeader.substring(7);
            String token = jwtProvider.extractToken(authHeader);
            try {
                DecodedJWT decodedJWT = jwtProvider.decodeAccessToken(token);
                String username = decodedJWT.getSubject();
                String role = decodedJWT.getClaim("role").asString();

                // Spring 권한 객체로 변환
                GrantedAuthority authority = new SimpleGrantedAuthority(role);
                // UsernamePasswordAuthenticationToken의 생성자가 리스트 형식으로 권한을 받기때문에 권한이 한개여도 넣어줘야됨
                List<GrantedAuthority> authorities = List.of(authority);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(username, null, authorities);

                // 요청한 사용자의 IP, 세션ID 등 추가 정보를 authentication에 붙여 사용하기 위한 코드(보안 로그 기록, 요청 추적 같은 곳에서 사용)
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (JWTVerificationException ex) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("JWT 토큰이 유효하지 않습니다.");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
