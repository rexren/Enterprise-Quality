package com.hikvision.rensu.cert.service;

import com.hikvision.rensu.cert.domain.InspectContent;
import com.hikvision.rensu.cert.repository.InspectContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by rensu on 17/4/28.
 */
@Service
public class InspectContentService {

    @Autowired
    private InspectContentRepository inspectContentRepository;

    public InspectContent get(Long id) {
        return inspectContentRepository.findOne(id);
    }

    public void save(InspectContent content) {
        inspectContentRepository.save(content);
    }
}
