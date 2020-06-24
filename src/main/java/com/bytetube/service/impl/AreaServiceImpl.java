package com.bytetube.service.impl;

import com.bytetube.dao.AreaDao;
import com.bytetube.entity.Area;
import com.bytetube.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AreaServiceImpl implements AreaService {

	@Autowired
	private AreaDao areaDao;



	@Override
	public List<Area> getAreaList() {

			return areaDao.queryArea();

	}


}
