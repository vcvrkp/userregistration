/**
 * 
 */
package com.example.user.service;

import java.util.List;

import com.example.user.domain.User;

/**
 * @author vcvr
 *
 */
public interface UserService {

	public List<User> getAllUsers();

	public List<User> getUesrsByEmail(String email);

	public User saveUser(User user);

	public User findUserById(long id);

}
