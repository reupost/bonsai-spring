package com.bonsainet.species.repository;

import com.bonsainet.species.model.Taxon;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface TaxonRepository extends CrudRepository<Taxon, Integer> {

}
