package ru.kata.spring.boot_security.demo.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.models.User;


@Repository
public interface UsersRepository extends JpaRepository <User,Integer> {
    User findByUsername(String username);
}
