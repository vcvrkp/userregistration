/**
 * 
 */
package com.example.user;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.user.domain.User;
import com.example.user.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 * @author vcvr
 *
 */
@RefreshScope
@RestController
@RequestMapping(value="/user")
@Api(value="user",description="This is a dataservice operation on Uesr",tags="user")
public class UserController {

	Logger logger = (Logger) LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	UserService userService;
	
	@GetMapping
	@ApiOperation(value="Get All Users registered",nickname="getUsers")
	public List<User> getUsers() {
		logger.info("Got inside GET /user" );
		logger.info("Coming out of GET /user" );
		return userService.getAllUsers();
	}
	
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value="Get User by Id",nickname="findOne")
    public User findOne(@PathVariable(name="id")long id){
		logger.info("Got inside GET /user/id" );
		logger.info("Coming out of GET /user/id" );
        return this.userService.findUserById(id);
    }
    
    @PostMapping(consumes="application/json")
    @ApiOperation(value="Save new User",nickname="saveOne")
    public User saveOne(@RequestBody User user) {
    	logger.info("Got inside POST /user" );
    	User savedObject = userService.saveUser(user);
		logger.info("Coming out of POST /user" );
    	return savedObject;
    }
    
}
