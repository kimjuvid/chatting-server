package com.example.chatting.controller.userSearch;

import com.example.chatting.service.userSearch.UserSearchService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserSearchController {

    private final UserSearchService userSearchService;


    @Operation(
            summary = "User Name List Search",
            description = "User Name을 기반으로 Like 검색 실행"
    )
    @GetMapping("/search/{name}")
    public List<String> searchUser(
            @PathVariable("name") String name
    ) {
        String user = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        return userSearchService.searchUser(name, user);
    }
}
