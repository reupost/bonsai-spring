package com.bonsainet.species.service;

import com.bonsainet.species.model.Taxon;
import java.util.List;
import java.util.Optional;

public interface ITaxonService {

    List<Taxon> findAll();

    Taxon save(Taxon t);

    void delete(Taxon t);

    Optional<Taxon> findById(Integer id);

    Long count();
}
