package com.example.demo;

import org.springframework.data.jpa.repository.*;

/*
This interface makes it possible for us to automatically create, update, delete, and find accounts
*/
public interface AccountRepo extends JpaRepository<Account, Long>{
}
