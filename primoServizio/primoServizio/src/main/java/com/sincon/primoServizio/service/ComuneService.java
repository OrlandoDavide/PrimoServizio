package com.sincon.primoServizio.service;

import com.sincon.primoServizio.dto.ComuneDto;
import com.sincon.primoServizio.mapperEntityDto.ComuneMapper;
import com.sincon.primoServizio.repository.ComuneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ComuneService {

    private final ComuneRepository comuneRepository;
    private final ComuneMapper comuneMapper;

    @Autowired
    public ComuneService(ComuneRepository comuneRepository, ComuneMapper comuneMapper) {
        this.comuneRepository = comuneRepository;
        this.comuneMapper = comuneMapper;
    }

    public ComuneDto getComuneByCodIstat(String codIstat) {
        return comuneMapper.comuneEntityToDto(comuneRepository.findByCodiceIstat(codIstat));
    }
}
