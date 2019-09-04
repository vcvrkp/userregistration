/**
 * 
 */
package com.example.user.service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.example.user.domain.User;
import com.example.user.domain.UserRepository;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author vcvr
 *
 */
@Service
@RefreshScope
public class UserServiceImpl implements UserService {
	
	Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	RabbitTemplate rabbitTemplate;

	@Autowired
	StringRedisTemplate redisTemplate;
	
	@Value("${feature.welcomeEmail:false}")
	private boolean welcomeEmail;

	@Value("${feature.welcomeQueueName:WELCOME_MESSAGE}")
	private String welcomeQueueName;
	
	@Autowired
	private MessageConverter jsonMessageConverter;
	
	private final ObjectMapper mapper = new ObjectMapper();
	
	@Override
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	@Override
	public List<User> getUesrsByEmail(String email) {
		return (List<User>)userRepository.findByEmail(email);
	}

	@Override
	public User saveUser(User user) {
		User savedUser = userRepository.save(user);
		
		if (welcomeEmail) {
			logger.info("Sedning MSG to MQ for sending an email");
			rabbitTemplate.setMessageConverter(jsonMessageConverter);
			rabbitTemplate.convertAndSend(welcomeQueueName,savedUser);
		}
		
		try {
			redisTemplate.opsForValue().set(
			        "USER_ID_" + savedUser.getId(),
			        mapper.writeValueAsString(savedUser), 30, TimeUnit.MINUTES);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return savedUser;
	}
	
	@SuppressWarnings("unchecked")
	public User findUserById(long id) {
		String userString = (String) redisTemplate.opsForValue()
                .get("USER_ID_" + id);
		User userFetched = null;
		if (userString != null) {
			logger.info("Fetching from Cache");
			try {
				userFetched = mapper.readValue(userString, new TypeReference<User>() {
				});
			} catch (JsonParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			logger.info("Fetching from Repository");
			userFetched = userRepository.findById(id).orElse(null);
			try {
				redisTemplate.opsForValue().set(
				        "USER_ID_" + userFetched.getId(),
				        mapper.writeValueAsString(userFetched), 30, TimeUnit.MINUTES);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return userFetched;
	}

	 
}
