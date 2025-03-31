//package com.example.chatting.security;
//
//import com.auth0.jwt.JWT;
//import com.auth0.jwt.algorithms.Algorithm;
//import com.auth0.jwt.exceptions.AlgorithmMismatchException;
//import com.auth0.jwt.exceptions.InvalidClaimException;
//import com.auth0.jwt.exceptions.SignatureVerificationException;
//import com.auth0.jwt.exceptions.TokenExpiredException;
//import com.auth0.jwt.interfaces.DecodedJWT;
//import com.example.chatting.common.Constants;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StringUtils;
//
//import java.util.Date;
//
//@Slf4j
//@Component
//public class JWTProvider {
//
//    private static String secretKey;
//    private static String refreshSecretKey;
//    private static long tokenTimeForMinute;
//    private static long refreshTokenTimeForMinute;
//
//    @Value("${token.secret-key}")
//    public void setSecretKey(String key) {
//        JWTProvider.secretKey = key;
//    }
//
//    @Value("${token.refresh-secret-key}")
//    public void setRefreshSecretKey(String key) {
//        JWTProvider.refreshSecretKey = key;
//    }
//
//    @Value("${token.token-time}")
//    public void setTokenTime(long time) {
//        JWTProvider.tokenTimeForMinute = time;
//    }
//
//    @Value("${token.refresh-token-time}")
//    public void setRefreshTokenTime(long time) {
//        JWTProvider.refreshTokenTimeForMinute = time;
//    }
//
//    public static String createToken(String name) {
//        return JWT.create()
//                .withSubject(name)
//                .withIssuedAt(new Date())
//                .withExpiresAt(new Date(System.currentTimeMillis() + tokenTimeForMinute * Constants.ON_MINUTE_TO_MILLIS))
//                .sign(Algorithm.HMAC256(secretKey));
//    }
//
//    public static String createRefreshToken(String name) {
//        return JWT.create()
//                .withSubject(name)
//                .withIssuedAt(new Date())
//                .withExpiresAt(new Date(System.currentTimeMillis() + refreshTokenTimeForMinute * Constants.ON_MINUTE_TO_MILLIS))
//                .sign(Algorithm.HMAC256(refreshSecretKey));
//    }
//
//    public static DecodedJWT checkTokenForRefresh(String token) {
//        try {
//            DecodedJWT decoded = JWT.require(Algorithm.HMAC256(secretKey)).build().verify(token);
//            log.error("Access token is not expired: {}", decoded.getSubject());
//            throw new RuntimeException("Access token is not expired yet");
//        } catch (AlgorithmMismatchException | SignatureVerificationException | InvalidClaimException e) {
//            throw new RuntimeException("Invalid token");
//        } catch (TokenExpiredException e) {
//            return JWT.decode(token);
//        }
//    }
//
//    public static DecodedJWT decodeAccessToken(String token) {
//        return decodeTokenAfterVerify(token, secretKey);
//    }
//
//    public static DecodedJWT decodeRefreshToken(String token) {
//        return decodeTokenAfterVerify(token, refreshSecretKey);
//    }
//
//    private static DecodedJWT decodeTokenAfterVerify(String token, String key) {
//        try {
//            return JWT.require(Algorithm.HMAC256(key)).build().verify(token);
//        } catch (AlgorithmMismatchException | SignatureVerificationException | InvalidClaimException e) {
//            throw new RuntimeException("Invalid token");
//        } catch (TokenExpiredException e) {
//            throw new RuntimeException("Token has expired");
//        }
//    }
//
//    public static DecodedJWT decodedJWT(String token) {
//        return JWT.decode(token);
//    }
//
//    public static String extractToken(String header) {
//        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
//            return header.substring(7);
//        } else {
//            throw new IllegalArgumentException("Invalid Authorization header format");
//        }
//    }
//
//    public static String getUserFromToken(String token) {
//        DecodedJWT jwt = decodedJWT(token);
//        return jwt.getSubject();
//    }
//}


package com.example.chatting.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.*;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.chatting.common.Constants;
import com.example.chatting.common.Role;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;

@Slf4j
@Component
public class JWTProvider {

    @Value("${token.secret-key}")
    private String secretKey;

    @Value("${token.refresh-secret-key}")
    private String refreshSecretKey;

    @Value("${token.token-time}")
    private long accessTokenValidity;

    @Value("${token.refresh-token-time}")
    private long refreshTokenValidity;

    // JWT 서명용 알고리즘 (JWT위조 여부를 확인하기 위한 검증용 서명키)
    private Algorithm accessAlgorithm;
    private Algorithm refreshAlgorithm;

    @PostConstruct
    public void init() {
        this.accessAlgorithm = Algorithm.HMAC256(secretKey);
        this.refreshAlgorithm = Algorithm.HMAC256(refreshSecretKey);
    }

    // Access Token 생성
    public String createAccessToken(String username, Role role) {
        return JWT.create()
                .withSubject(username)
                .withClaim("role", role.name())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + accessTokenValidity * Constants.ON_MINUTE_TO_MILLIS))
                .sign(accessAlgorithm);
    }

    // Refresh Token 생성
    public String createRefreshToken(String username) {
        return JWT.create()
                .withSubject(username)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + refreshTokenValidity * Constants.ON_MINUTE_TO_MILLIS))
                .sign(refreshAlgorithm);
    }

    // Access Token 검증
    public String getUsernameFromAccessToken(String token) {
        return JWT.require(accessAlgorithm).build().verify(token).getSubject();
    }

    // Refresh Token 검증
    public String getUsernameFromRefreshToken(String token) {
        return JWT.require(refreshAlgorithm).build().verify(token).getSubject();
    }

    // Bearer 토큰에서 실제 토큰 추출
    public String extractToken(String header) {
        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        throw new IllegalArgumentException("Invalid Authorization header format");
    }

    public DecodedJWT decodeAccessToken(String token) {
        return JWT.require(accessAlgorithm).build().verify(token);
    }
}