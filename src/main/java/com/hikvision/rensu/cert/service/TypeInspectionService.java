package com.hikvision.rensu.cert.service;

import com.hikvision.rensu.cert.domain.TypeInspection;
import com.hikvision.rensu.cert.repository.TypeInspectionRepository;
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

    public TypeInspectionService(TypeInspectionRepository typeInspectionRepository) {
        this.typeInspectionRepository = typeInspectionRepository;
    }

    public List<TypeInspection> getInspections() {
        return typeInspectionRepository.findAll();
    }

    public void save(TypeInspection typeInspection) {
        typeInspectionRepository.save(typeInspection);
    }
}
