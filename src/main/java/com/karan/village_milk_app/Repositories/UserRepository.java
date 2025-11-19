package com.karan.village_milk_app.Repositories;

import com.karan.village_milk_app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByPhone(String phone);
    boolean existsByPhone(String phone);
}