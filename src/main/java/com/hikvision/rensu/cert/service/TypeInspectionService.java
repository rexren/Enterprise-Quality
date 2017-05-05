package com.hikvision.rensu.cert.service;

import com.hikvision.rensu.cert.domain.TypeInspection;
import com.hikvision.rensu.cert.repository.TypeInspectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by rensu on 17/4/21.
 */
@Service
@Transactional
public class TypeInspectionService {

    private final TypeInspectionRepository typeInspectionRepository;

    @Autowired
    public TypeInspectionService(TypeInspectionRepository typeInspectionRepository) {
        this.typeInspectionRepository = typeInspectionRepository;
    }

    public Page<TypeInspection> getInspectionByPage(int start, int size) {
        Pageable page = new PageRequest(start, size);
        return typeInspectionRepository.findAll(page);
    }

    public List<TypeInspection> getInspections() {
        return typeInspectionRepository.findAll();
    }

    public void save(TypeInspection typeInspection) {
        typeInspectionRepository.save(typeInspection);
    }

    public TypeInspection get(Long id) {
        return typeInspectionRepository.findOne(id);
    }
}
