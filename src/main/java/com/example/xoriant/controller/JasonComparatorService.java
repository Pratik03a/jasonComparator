package com.example.xoriant.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import javax.sql.rowset.serial.SerialException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.example.xoriant.JasonComparatorConstants;
import com.example.xoriant.NoBaseIdFoundException;
import com.example.xoriant.model.JasonComparatorBM;
import com.example.xoriant.model.JasonComparatorRequest;
import com.example.xoriant.model.dao.JasonComparatorRepository;

@RestController
public class JasonComparatorService {
	private Logger LOGGER = Logger.getLogger(JasonComparatorService.class.getName());
	
	@Autowired
	JasonComparatorRepository repository;
	
	@Autowired
	JasonComparatorBM jasonComparatorbm;
	
	@PostMapping(value = "/savebasejson")   
	public Integer saveBaseJaonfile(@RequestParam(value = "file", required = true) MultipartFile file) throws IOException, SerialException, SQLException {		
		JasonComparatorRequest comparatorRequest = null;
		try {
		String content = new String(file.getBytes(), StandardCharsets.UTF_8);
		comparatorRequest = new JasonComparatorRequest(content);
	    repository.save(comparatorRequest);

		}catch(Exception e) {
			LOGGER.log(Level.WARNING,JasonComparatorConstants.EXCEPTION, e);
		}
		return comparatorRequest.getBaseId();
	}
	
	@GetMapping("/getallfiles")
	public List<JasonComparatorRequest> getAll(){
	return repository.findAll();
	}
	
	@GetMapping("/getfilesByid/{baseid}")
	public  JasonComparatorRequest getfilesbyid(@PathVariable Integer baseid) throws NoBaseIdFoundException{
	
		Optional<JasonComparatorRequest> jasonComparatorRequest = findRecordsById(baseid);
		if(jasonComparatorRequest.isPresent()) 
			return jasonComparatorRequest.get();
		else throw new NoBaseIdFoundException(JasonComparatorConstants.NO_BASE_ID_FOUND);
	}
	
	@GetMapping("/updaterecord/{baseid}")
	public String getJasonFileByBaseId(@PathVariable Integer baseid){
		try {	
			//repository.findById(baseid).orElseThrow(new NoBaseIdFoundException("No Base id found")); 
		Optional<JasonComparatorRequest> jasonComparatorRequest = repository.findById(baseid);
		if(jasonComparatorRequest.isPresent()) 
			return JasonComparatorConstants.RECORD_UPDATED+baseid;
		}catch (Exception e) {
			LOGGER.log(Level.WARNING,JasonComparatorConstants.EXCEPTION, e); 
		}
		return JasonComparatorConstants.NO_BASE_ID_FOUND+baseid;
		
	}
	
	@GetMapping("/removerecord/{baseid}")
	public String removeFileByBaseId(@PathVariable Integer baseid){
		try {
			Optional<JasonComparatorRequest> jasonComparatorRequest = findRecordsById(baseid);
			if(jasonComparatorRequest.isPresent()) { 
				repository.deleteById(baseid);
			return JasonComparatorConstants.RECORD_DELETED+baseid;
			}
			else throw new NoBaseIdFoundException(JasonComparatorConstants.NO_BASE_ID_FOUND+baseid);
			
		}catch (Exception e) {
			LOGGER.log(Level.WARNING,JasonComparatorConstants.EXCEPTION, e);
		}
		return JasonComparatorConstants.NO_BASE_ID_FOUND+baseid;
	}
	
	
	@PostMapping("/getdifference")
	public String calculateJasonDifference(@RequestParam(value = "file", required = true) MultipartFile file, @RequestParam (value = "baseId", required = true) Integer baseId) {
		
		AtomicReference<String> difference = new AtomicReference<String>();		
		findRecordsById(baseId).ifPresent(t->{
		String rightJson = null;

			try {
				rightJson = new String(file.getBytes(), StandardCharsets.UTF_8);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String leftJson = t.getFileContent();
			
			jasonComparatorbm = new JasonComparatorBM();
			String diff =	jasonComparatorbm.calculateDifference(leftJson, rightJson);
			difference.set(diff);
			
			
		});
		return difference.get();
		
	}
	
	
	private Optional<JasonComparatorRequest> findRecordsById(Integer baseid) {
		return repository.findById(baseid);
	}
	
}
