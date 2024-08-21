package com.sakhayadeep.social.social_media.user.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sakhayadeep.social.social_media.user.User;

public interface UserRepository extends JpaRepository<User, Integer> {

}
