package com.project.Fabo.service;

import com.project.Fabo.entity.AddSupportAdmin;
import com.project.Fabo.entity.ClientSupport;

public interface SupportMirrorService {

	void addSupportRecordToBothSides(AddSupportAdmin addSupportAdmin);
	
	void addSupportRecordToBothSide(ClientSupport clientSupport);

}
