package com.project.Fabo.service.impl;

import org.springframework.stereotype.Service;

import com.project.Fabo.entity.InvoiceComments;
import com.project.Fabo.repository.InvoiceCommentsRepository;
import com.project.Fabo.service.InvoiceCommentsService;

@Service
public class InvoiceCommentsServiceImpl implements InvoiceCommentsService{
	
	private InvoiceCommentsRepository invoiceCommentsRepository;

	public InvoiceCommentsServiceImpl(InvoiceCommentsRepository invoiceCommentsRepository) {
		super();
		this.invoiceCommentsRepository = invoiceCommentsRepository;
	}

	@Override
	public InvoiceComments saveComment(InvoiceComments comment) {
		return invoiceCommentsRepository.save(comment);
	}

}
