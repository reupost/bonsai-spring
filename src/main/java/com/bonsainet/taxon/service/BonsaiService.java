package com.bonsainet.taxon.service;

import com.bonsainet.taxon.model.Bonsai;
import com.bonsainet.taxon.repository.BonsaiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BonsaiService implements IBonsaiService {

    @Autowired
    private BonsaiRepository repository;

    @Override
    public List<Bonsai> findAll() {
        return (List<Bonsai>) repository.findAll();
    }

    @Override
    public Page<Bonsai> findAll(Pageable pageable) {
        return (Page<Bonsai>) repository.findAll(pageable);
    }


    @Override
    public Bonsai save(Bonsai b) {
        return repository.save(b);
    }

    @Override
    public void delete(Bonsai t) {
        repository.delete(t);
    }

    @Override
    public Optional<Bonsai> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Page<Bonsai> findByNameContaining(String name, Pageable pageable) {
        return repository.findByNameContaining(name, pageable);
    }

    @Override
    public Long count() {
        return repository.count();
    }
}
