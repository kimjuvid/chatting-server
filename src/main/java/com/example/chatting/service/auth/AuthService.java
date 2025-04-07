package com.example.chatting.service.auth;

import com.example.chatting.common.Role;
import com.example.chatting.common.exception.CustomException;
import com.example.chatting.common.exception.ErrorCode;
import com.example.chatting.model.auth.response.CreateUserResponse;
import com.example.chatting.model.auth.response.LoginResponse;
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
    public CreateUserResponse createUser(CreateUserRequest createUserRequest){

        Optional<User> user = userRepository.findByName(createUserRequest.name());

        System.out.println(createUserRequest.name());
        System.out.println(createUserRequest.password());

        if (user.isPresent()){
            throw new CustomException(ErrorCode.USER_ALREADY_EXISTS);
        }

        try{
            User newUser = this.newUser(createUserRequest.name());
            UserCredentials newUserCredentials = this.newUserCredentials(createUserRequest.password(),newUser);
            newUser.setUserCredentials(newUserCredentials);
            newUser.setRole(Role.ROLE_USER);
            User saveUser = userRepository.save(newUser);

            if (saveUser == null){
                throw new CustomException(ErrorCode.USER_SAVED_FAILED);
            }
        }catch (Exception e){
            throw new CustomException(ErrorCode.USER_SAVED_FAILED, e.getMessage());
        }

        return new CreateUserResponse(ErrorCode.SUCCESS);
    }

    public LoginResponse login(LoginRequest request){
        Optional<User> user = userRepository.findByName(request.name());

        if (!user.isPresent()) {
            throw new CustomException(ErrorCode.NOT_EXIST_USER);
        }

        User u = user.get();
        String hashedValue = hashing.getHashingValue(request.password());

        if (!u.getUserCredentials().getHashed_password().equals(hashedValue)) {
            throw new CustomException(ErrorCode.MIS_MATCH_PASSWORD);
        }

        String token = jwtProvider.createAccessToken(request.name(), u.getRole());
        System.out.println(token);
        return new LoginResponse(ErrorCode.SUCCESS, token);
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
