package com.sakhayadeep.social.social_media.post.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sakhayadeep.social.social_media.post.Post;

public interface PostRepository extends JpaRepository<Post, Integer> {

}
