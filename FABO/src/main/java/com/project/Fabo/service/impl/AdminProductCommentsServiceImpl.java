package com.project.Fabo.service.impl;

import org.springframework.stereotype.Service;

import com.project.Fabo.entity.AdminProductComments;
import com.project.Fabo.repository.AdminProductCommentsRepository;
import com.project.Fabo.service.AdminProductCommentService;


@Service
public class AdminProductCommentsServiceImpl implements AdminProductCommentService{
	
	private AdminProductCommentsRepository adminProductCommentsRepository;

	public AdminProductCommentsServiceImpl(AdminProductCommentsRepository adminProductCommentsRepository) {
		super();
		this.adminProductCommentsRepository = adminProductCommentsRepository;
	}

	@Override
	public AdminProductComments saveComment(AdminProductComments comment) {
		return adminProductCommentsRepository.save(comment);
	}

}
