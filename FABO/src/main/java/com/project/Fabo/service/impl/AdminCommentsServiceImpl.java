package com.project.Fabo.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.project.Fabo.entity.AdminComments;
import com.project.Fabo.entity.ClientUser;
import com.project.Fabo.repository.AdminCommentsRepository;
import com.project.Fabo.repository.ClientUserRepository;
import com.project.Fabo.service.AdminCommentService;
@Service
public class AdminCommentsServiceImpl implements AdminCommentService{
	
	 @Autowired
	    private AdminCommentsRepository adminCommentsRepository;

	 @Autowired
	    private ClientUserRepository clientUserRepository;
	 
	 private static final Logger logger = LoggerFactory.getLogger(AdminCommentsServiceImpl.class);


	public AdminCommentsServiceImpl(AdminCommentsRepository adminCommentsRepository) {
		super();
		this.adminCommentsRepository = adminCommentsRepository;
	}

	@Override
	public AdminComments saveComment(AdminComments comment) {
		return adminCommentsRepository.save(comment);
	}

	  public List<String> getNotificationsForCurrentUser() {
	        // Get the currently logged-in user's authentication object
	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	        // Extract the username
	        String username = authentication.getName();

	        logger.debug("Current username: {}", username);

	        // Find the ClientUser entity by username
	        Optional<ClientUser> optionalClientUser = clientUserRepository.findByUserName(username);
	        
	        if (optionalClientUser.isPresent()) {
	            ClientUser clientUser = optionalClientUser.get();
	            // Get the store code from the ClientUser entity
	            String storeCode = clientUser.getStoreCode();
	            logger.debug("Store code for current user: {}", storeCode);

	            // Call the repository method to fetch AdminComments entities for the store code
	            List<AdminComments> adminCommentsList = adminCommentsRepository.findByStoreCode(storeCode);
	            logger.debug("Retrieved AdminComments: {}", adminCommentsList);

	            // Extract notification strings from AdminComments entities
	            List<String> notifications = adminCommentsList.stream()
	                                            .map(AdminComments::getNotification)
	                                            .collect(Collectors.toList());
	            logger.debug("Extracted notifications: {}", notifications);
	            
	            return notifications;
	        } else {
	            // Handle case where ClientUser is not found
	            logger.error("ClientUser not found for username: {}", username);
	            throw new RuntimeException("ClientUser not found for username: " + username);
	        }
	    }
}
