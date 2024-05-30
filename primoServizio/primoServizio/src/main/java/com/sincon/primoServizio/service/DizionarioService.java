package com.sincon.primoServizio.service;

import com.sincon.primoServizio.model.Dizionario;
import com.sincon.primoServizio.repository.DizionarioRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DizionarioService {
    private final DizionarioRepositoryImpl dizionarioRepoImpl;

    @Autowired
    public DizionarioService(DizionarioRepositoryImpl dizionarioRepoImpl) {
        this.dizionarioRepoImpl = dizionarioRepoImpl;
    }

    // GET dizionario by codifica AND categoria
    public Dizionario getDizionarioByCodifica(String codifica, String categoria) {
        try {
            return dizionarioRepoImpl.getDizionarioByCodifica(codifica, categoria);
        } finally {}
    }

    // GET dizionario by ID
    public Dizionario getDizionarioById(Long id) {
        try {
            return dizionarioRepoImpl.getDizionarioById(id);
        } finally {}
    }
}
