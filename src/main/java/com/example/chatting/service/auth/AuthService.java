package com.example.chatting.service.auth;

import com.example.chatting.repository.entity.User;
import com.example.chatting.repository.entity.UserCredentials;
import com.example.chatting.model.auth.request.CreateUserRequest;
import com.example.chatting.model.auth.request.LoginRequest;
import com.example.chatting.repository.UserRepository;
import com.example.chatting.security.Hashing;
import com.example.chatting.security.JWTProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final Hashing hashing;
    private final JWTProvider jwtProvider;

    @Transactional
    public String createUser(CreateUserRequest createUserRequest){

        Optional<User> user = userRepository.findByName(createUserRequest.name());

        System.out.println(createUserRequest.name());
        System.out.println(createUserRequest.password());

        if (user.isPresent()){
            // 예외 코드 적용
            return "이미 사용중인 아이디가 존재합니다.";
        }

        try{
            User newUser = this.newUser(createUserRequest.name());
            UserCredentials newUserCredentials = this.newUserCredentials(createUserRequest.password(),newUser);
            newUser.setUserCredentials(newUserCredentials);

            User saveUser = userRepository.save(newUser);

//            if (saveUser == null){
//                return "회원생성 실패";
//                // 예외 처리 코드
//            }
        }catch (Exception e){
            System.out.println("예외 : " + e);
        }

        return "Success";
    }

    public String login(LoginRequest request){
        Optional<User> user = userRepository.findByName(request.name());

        if (!user.isPresent()) {
            throw new RuntimeException("존재하지 않는 아이디입니다.");
        }

        User u = user.get();
        String hashedValue = hashing.getHashingValue(request.password());

        if (!u.getUserCredentials().getHashed_password().equals(hashedValue)) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        String token = jwtProvider.createAccessToken(request.name());
        System.out.println(token);
        return token;
    }


    private UserCredentials newUserCredentials(String password, User user) {
        String hashedValue = hashing.getHashingValue(password);
        UserCredentials uc = UserCredentials.builder()
                .user(user)
                .hashed_password(hashedValue)
                .build();
        return uc;
    }

    private User newUser(String name) {
        User newUser = User.builder()
                .name(name)
                .created_at(new Timestamp(System.currentTimeMillis()))
                .build();
        return newUser;
    }
}
