package com.daengdaeng_eodiga.project.user.repository;

import com.daengdaeng_eodiga.project.oauth.OauthProvider;
import com.daengdaeng_eodiga.project.user.dto.UserPets;
import com.daengdaeng_eodiga.project.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmailAndOauthProviderAndDeletedAtIsNull(String email,OauthProvider oauthProvider);
    boolean existsByNickname(String nickname);
    Optional<User> findByEmailAndOauthProvider(String email, OauthProvider oauthProvider);

    @Query("select u.userId as userId ,u.nickname as nickname ,p.petId as petId ,p.name as petName,p.image as petImage from User u left join Pet p on u.userId = p.user.userId where u.userId in :userIds")
    List<UserPets> findByIdInUserIds(List<Integer> userIds);
}

