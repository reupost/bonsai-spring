package com.bonsainet.species.service;

import com.bonsainet.species.model.Taxon;
import com.bonsainet.species.repository.TaxonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaxonService implements ITaxonService {

    @Autowired
    private TaxonRepository repository;

    @Override
    public List<Taxon> findAll() {
        return (List<Taxon>) repository.findAll();
    }

    @Override
    public Page<Taxon> findAll(Pageable pageable) {
        return (Page<Taxon>) repository.findAll(pageable);
    }


    @Override
    public Taxon save(Taxon t) {
        t.composeFullName();
        return repository.save(t);
    }

    @Override
    public void delete(Taxon t) {
        repository.delete(t);

    }

    @Override
    public Optional<Taxon> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Page<Taxon> findByFullNameContaining(String fullname, Pageable pageable) {
        return repository.findByFullNameContaining(fullname, pageable);
    }

    @Override
    public Long count() {
        return repository.count();
    }
}
