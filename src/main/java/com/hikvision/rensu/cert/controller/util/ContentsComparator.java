package com.hikvision.rensu.cert.controller.util;

import java.util.Comparator;

import com.hikvision.rensu.cert.domain.InspectContent;

@SuppressWarnings("rawtypes")
public class ContentsComparator implements Comparator {

	@Override
	public int compare(Object o1, Object o2) {
		InspectContent c1 = (InspectContent) o1;
		InspectContent c2 = (InspectContent) o2;
		int result = (c1.getId() > c2.getId()) ? 1 : ((c1.getId() == c2.getId()) ? 0 : -1);
		return result;
	}

}
