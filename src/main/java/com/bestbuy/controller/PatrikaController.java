package com.bestbuy.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bestbuy.model.Patrika;
import com.bestbuy.model.PatrikaData;
import com.bestbuy.model.PatrikaDistribution;
import com.bestbuy.model.PatrikaEvent;
import com.bestbuy.repository.PatrikaDataRepository;
import com.bestbuy.repository.PatrikaDistributionRepository;
import com.bestbuy.repository.PatrikaEventRepository;
import com.bestbuy.repository.PatrikaRepository;
import com.bestbuy.repository.UserRepository;

@CrossOrigin (origins = {"*"}, maxAge = 3600)
@RestController
public class PatrikaController {

	@Autowired
	private PatrikaRepository patrikaRepository;
	
	@Autowired
	private PatrikaDataRepository patrikaDataRepository;
	
	@Autowired
	private PatrikaEventRepository patrikaEventRepository;
	
	@Autowired
	private PatrikaDistributionRepository pdRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@RequestMapping(value="/addPatrika/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE, method=RequestMethod.POST)
	public  @Valid Optional<Patrika> savePatrika(@PathVariable Long userId, @Valid @RequestBody Patrika patrika) {
		return userRepository.findById(userId).map(user -> {
			patrika.setUser(user);
			Patrika p = patrikaRepository.save(patrika);
			patrika.getPatrikaData().forEach(pd->{
				pd.setPatrika(p);
			});
			
			patrika.getEventList().forEach(ev->{
				ev.setPatrika(p);
			});
			
			List<PatrikaData> dataList =  patrikaDataRepository.findAllByPatrika(patrika);
			List<PatrikaEvent> eventList =  patrikaEventRepository.findAllByPatrika(patrika);
			
			dataList.forEach(dl->{
				patrikaDataRepository.deleteById(dl.getId());
			});
			
			eventList.forEach(el->{
				patrikaEventRepository.deleteById(el.getId());
			});
			
			patrikaDataRepository.saveAll(patrika.getPatrikaData());
			patrikaEventRepository.saveAll(patrika.getEventList());
			return p;
        });
	}

	@RequestMapping(value="/submitPatrika/{patrikaId}", consumes = MediaType.APPLICATION_JSON_VALUE, method=RequestMethod.POST)
	public @Valid Optional<PatrikaDistribution> submitPatrika(@PathVariable Long patrikaId, @Valid @RequestBody PatrikaDistribution patrikaDistribution) {
		return patrikaRepository.findById(patrikaId).map(patrika -> {
			patrikaDistribution.setPatrika(patrika);
			return pdRepository.save(patrikaDistribution);
        });
	}

	@RequestMapping(value="/searchPatrika", consumes = MediaType.APPLICATION_JSON_VALUE, method=RequestMethod.POST)
	public @Valid List<PatrikaDistribution> searchPatrika(@Valid @RequestBody PatrikaDistribution patrikaDistribution) {
		List<PatrikaDistribution> patrikaDistributionList = pdRepository.findByStateAndDistrictAndTehsilAndNagarAndCastAndShreeContaining(patrikaDistribution.getState(), patrikaDistribution.getDistrict(), patrikaDistribution.getTehsil(), patrikaDistribution.getNagar(), patrikaDistribution.getCast(), patrikaDistribution.getShree());
		patrikaDistributionList.forEach(p->{
			Patrika patrika = patrikaRepository.findById(p.getPatrika().getId()).get();
			patrika.setPatrikaData(patrikaDataRepository.findAllByPatrika(patrika));
			patrika.setEventList(patrikaEventRepository.findAllByPatrika(patrika));
			p.setPatrikaObj(patrika);
		});
		return patrikaDistributionList;
	}
	
	@GetMapping("allPatrika")
	public Iterable<Patrika> allPatrika() {
		Iterable<Patrika> patrikaList =  patrikaRepository.findAll();
		patrikaList.forEach(p->{
			p.setPatrikaData(patrikaDataRepository.findAllByPatrika(p));
			p.setEventList(patrikaEventRepository.findAllByPatrika(p));
		});
		return patrikaList;
	}
	
	@GetMapping("myPatrika/{userId}")
	public Optional<Object> myPatrika(@PathVariable Long userId) {
		return userRepository.findById(userId).map(user -> {
			Iterable<Patrika> patrikaList = patrikaRepository.findAllByUser(user);
			patrikaList.forEach(p->{
				p.setPatrikaData(patrikaDataRepository.findAllByPatrika(p));
				p.setEventList(patrikaEventRepository.findAllByPatrika(p));
			});
			return patrikaList;
		});
	}
}
