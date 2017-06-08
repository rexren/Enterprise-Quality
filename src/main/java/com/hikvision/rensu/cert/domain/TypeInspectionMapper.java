package com.hikvision.rensu.cert.domain;

import com.hikvision.rensu.cert.search.IndexMapper;

/**
 * Created by rensu on 2017/6/1.
 */
public class TypeInspectionMapper implements IndexMapper<TypeInspection> {

    @SuppressWarnings("unchecked")
	@Override
    public TypeInspectionSearchEntry map(TypeInspection item) {
        TypeInspectionSearchEntry t = new TypeInspectionSearchEntry();
        t.setTitle(item.getName());
        t.setId(item.getDocNo());
        t.setRemark(item.getRemarks());
        t.setType("inspection");
        return t;
    }
}
