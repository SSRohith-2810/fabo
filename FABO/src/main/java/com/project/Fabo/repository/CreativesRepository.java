package com.project.Fabo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.Fabo.entity.Creatives;

@Repository
public interface CreativesRepository extends JpaRepository <Creatives, Long>{

	List<Creatives> findByServiceType(String string);

	 //Optional<byte[]> findBaseImageByCreativeId(Long creativeId);

}
