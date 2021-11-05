package com.bestbuy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.bestbuy.model.ConfigDTO;
import com.bestbuy.repository.ConfigRepository;
import com.bestbuy.utils.DataUtils;

@CrossOrigin (origins = {"*"}, maxAge = 3600)
@RestController
public class ConfigController {
	
	@Autowired
	ConfigRepository configRepository;

	@GetMapping("/config/{type}")
	public ConfigDTO configParam(@PathVariable String type) {
		ConfigDTO configDTO = new ConfigDTO();
				
		configDTO = DataUtils.getConfigObject(configRepository.findAllByType(type));
		return configDTO;
	}
	
	@GetMapping("/configMulti/{type}")
	public List<ConfigDTO> configParamMulti(@PathVariable String type) {
		return configRepository.findAllByType(type);
	}
	
}
