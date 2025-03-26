package com.example.chatting.repository;


import com.example.chatting.repository.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByName(String name);
    boolean existsByName(String name);

    // 대소문자 구분 없이 이름에 검색어가 포함된 유저들을 찾고 로그인한 유저는 제외해서(나 자신을) 이름만 리스트로 가져온다.
    @Query("SELECT u.name FROM User AS u WHERE LOCATE(LOWER(:pattern), LOWER(u.name)) > 0 AND u.name != :user")
    List<String> findNameByNameMatch(@Param("pattern") String pattern, @Param("user") String user);
}
