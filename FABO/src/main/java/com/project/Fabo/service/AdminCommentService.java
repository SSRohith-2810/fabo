package com.project.Fabo.service;

import java.util.List;

import com.project.Fabo.entity.AdminComments;

public interface AdminCommentService {

	AdminComments saveComment(AdminComments comment);

	List<String> getNotificationsForCurrentUser();

}
