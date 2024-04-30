package com.project.Fabo.service;

import java.util.List;

import com.project.Fabo.entity.Creatives;

public interface CreativesService {

	void save(Creatives creatives);

	List<Creatives> findByServiceType(String string);

	byte[] getBaseImageById(Long creativeId);

}
