/**
 * 
 */
package com.example.user.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author vcvr
 *
 */
public interface UserRepository extends JpaRepository<User, Long>{
	
	public List<User> findByEmail(String email);

}
