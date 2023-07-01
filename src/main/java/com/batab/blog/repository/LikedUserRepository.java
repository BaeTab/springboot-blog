package com.batab.blog.repository;

import com.batab.blog.domain.LikedUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikedUserRepository extends JpaRepository<LikedUser, Long> {
    boolean existsByEmailAndPostId(String email, Long postId);
    void deleteByPostId(Long postId);

}
