package com.example.chatting.service.userSearch;

import com.example.chatting.common.exception.ErrorCode;
import com.example.chatting.model.userSearch.response.UserSearchResponse;
import com.example.chatting.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserSearchService {

    private final UserRepository userRepository;

    public UserSearchResponse searchUser(String name, String user) {
        System.out.println(name);
        System.out.println(user);
        List<String> names = userRepository.findNameByNameMatch(name,user);
        return new UserSearchResponse(ErrorCode.SUCCESS, names);
    }
}
