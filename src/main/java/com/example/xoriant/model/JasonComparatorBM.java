package com.example.xoriant.model;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.xoriant.FlatMapUtil;
import com.example.xoriant.JasonComparatorConstants;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;

@Component
public class JasonComparatorBM {
	private Logger LOGGER = Logger.getLogger(JasonComparatorBM.class.getName());
	
	public String calculateDifference(String leftJson, String rightJson) {
		StringBuilder builder = new StringBuilder();
		try {
            ObjectMapper mapper = new ObjectMapper();
            TypeReference<Map<String, Object>> type = new TypeReference<Map<String, Object>>() {
            };

            Map<String, Object> leftMap = mapper.readValue(leftJson, type);
            Map<String, Object> rightMap = mapper.readValue(rightJson, type);

            Map<String, Object> leftFlatMap = FlatMapUtil.flatten(leftMap);
            Map<String, Object> rightFlatMap = FlatMapUtil.flatten(rightMap);


            MapDifference<String, Object> difference = Maps.difference(leftFlatMap, rightFlatMap);
            difference.entriesDiffering().forEach((key, value) -> builder.append(key + ": " + value));
           

        } catch (Exception e) {
        	LOGGER.log(Level.WARNING,JasonComparatorConstants.EXCEPTION, e);
        }
		return builder.toString();
    	
	}

}
