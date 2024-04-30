package com.project.Fabo.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.project.Fabo.dao.RoleDao;
import com.project.Fabo.dao.UserDao;
import com.project.Fabo.entity.Client;
import com.project.Fabo.entity.ClientUser;
import com.project.Fabo.entity.Role;
import com.project.Fabo.entity.User;
import com.project.Fabo.repository.ClientRepository;
import com.project.Fabo.repository.ClientUserRepository;
import com.project.Fabo.repository.UserRepository;
import com.project.Fabo.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	private UserDao userDao;

	private RoleDao roleDao;
	
	private ClientUserRepository clientUserRepository;
	
	private UserRepository userRepository;

	public UserServiceImpl(UserDao userDao, RoleDao roleDao, ClientUserRepository clientUserRepository,
			UserRepository userRepository) {
		super();
		this.userDao = userDao;
		this.roleDao = roleDao;
		this.clientUserRepository = clientUserRepository;
		this.userRepository = userRepository;
	}

	@Override
	public User findByUserName(String userName) {
		// check the database if the user already exists
		return userDao.findByUserName(userName);
	}

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		User user = userDao.findByUserName(userName);
		if (user == null) {
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(),
				mapRolesToAuthorities(user.getRoles()));
	}

	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
	}

	@Override
    public String getUserStoreCodeByEmail(String userEmail) {
        // Retrieve the client entity from the database based on the email
        Optional<ClientUser> clientUserOptional = clientUserRepository.findByEmail(userEmail);

        // Check if the client exists
        if (clientUserOptional.isPresent()) {
            ClientUser clientUser = clientUserOptional.get();
            
            // Return the store code associated with the client
            return clientUser.getStoreCode();
        } else {
            // Handle case where client with the given email is not found
            throw new IllegalArgumentException("ClientUser with email " + userEmail + " not found");
        }
    }

	@Override
	public User getUserById(Long id) {
		// TODO Auto-generated method stub
		return userRepository.findById(id).get();
	}

	@Override
	public User saveUser(User existingUser) {
		// TODO Auto-generated method stub
		return userRepository.save(existingUser);
	}

	@Override
	public String getUserStoreCodeByUserName(String username) {
		  Optional<ClientUser> clientUserOptional = clientUserRepository.findByUserName(username);

	        // Check if the client exists
	        if (clientUserOptional.isPresent()) {
	            ClientUser clientUser = clientUserOptional.get();
	            
	            // Return the store code associated with the client
	            return clientUser.getStoreCode();
	        } else {
	            // Handle case where client with the given email is not found
	            throw new IllegalArgumentException("ClientUser with email " + username + " not found");
	        }
	}

	@Override
	public User getUserByUserName(String userName) {
		// TODO Auto-generated method stub
		return userRepository.findByUserName(userName);
	}

	@Override
	public boolean isUsernameDuplicate(String userName) {
	    // Check if there is any user with the given username in the database
	    return userRepository.existsByUserName(userName);
	}
	
	 public List<User> getUsersByName(String name) {
	        return userRepository.findByName(name);
	    }
	 
	 public void printUsers(List<User> users) {
	        for (User user : users) {
	            System.out.println("User ID: " + user.getId());
	            System.out.println("Username: " + user.getUserName());
	            // Print other user details as needed
	        }
	    }

	@Override
	public String getEmailByUserName(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User getUserByToken(String token) {
		// TODO Auto-generated method stub
		return userRepository.findByToken(token);
	}
	
}
