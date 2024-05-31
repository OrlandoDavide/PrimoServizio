package com.sincon.primoServizio.service;

import com.sincon.primoServizio.dto.DizionarioDto;
import com.sincon.primoServizio.mapperEntityDto.DizionarioMapper;
import com.sincon.primoServizio.model.Dizionario;
import com.sincon.primoServizio.repository.DizionarioRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DizionarioService {
    private final DizionarioRepositoryImpl dizionarioRepoImpl;
    private final DizionarioMapper dizionarioMapper;

    @Autowired
    public DizionarioService(DizionarioRepositoryImpl dizionarioRepoImpl,
                             DizionarioMapper dizionarioMapper) {
        this.dizionarioRepoImpl = dizionarioRepoImpl;
        this.dizionarioMapper = dizionarioMapper;
    }

    // GET dizionario by codifica AND categoria
    public DizionarioDto getDizionarioByCodifica(String codifica, String categoria) {
        try {
            return dizionarioMapper.dizionarioEntityToDto(dizionarioRepoImpl.getDizionarioByCodifica(codifica, categoria));
        } finally {}
    }

    // GET dizionario by ID
    public DizionarioDto getDizionarioById(Long id) {
        try {
            return dizionarioMapper.dizionarioEntityToDto(dizionarioRepoImpl.getDizionarioById(id));
        } finally {}
    }

    //GET dizionario by ID / OBJECT.ID
    public DizionarioDto getDizionario(Object obj) {
        try {
            if(obj instanceof Dizionario dizionario) {
                return dizionarioMapper.dizionarioEntityToDto(dizionarioRepoImpl.getDizionarioById(dizionario.getId()));
            } else if (obj != null) {
                return dizionarioMapper.dizionarioEntityToDto(dizionarioRepoImpl.getDizionarioById((long) obj));
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}
