package com.shinhancard.chatbot.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.shinhancard.chatbot.dto.request.EventTypeRequest;
import com.shinhancard.chatbot.dto.response.EventTypeResponse;
import com.shinhancard.chatbot.entity.EventType;

@Configuration
public class ModelMapperConfig {

	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		modelMapper.typeMap(EventTypeRequest.class, EventType.class).addMapping(EventTypeRequest::getPropertiesArray,EventType::setPropertiesArray);
		modelMapper.typeMap(EventTypeRequest.class, EventType.class).addMapping(EventType::getPropertiesArray,EventTypeResponse::setPropertiesArray);
		//modelMapper.typeMap(EventTypeRequest.class, EventType.class).addMapping(EventTypeRequest::getProperties(),EventType::setPropertiesArray);

//		Converter<ArrayList<PropertyCode>, ArrayList<PropertyCode>> propertyCodeConverter = new AbstractConverter<ArrayList<PropertyCode>, ArrayList<PropertyCode>>() {
//			protected ArrayList<PropertyCode> convert(ArrayList<PropertyCode> source) {
//				return source;
//			}
//		};
		
//		TypeMap typeMap;
//		typeMap.addMappings(mapper -> mapper.using(propertyCodeConverter).map(EventTypeRequest.class, EventType.class));
//		
		//modelMapper.typeMap(EventTypeRequest.class, EventType.class).setConverter(converter);
		
		//modelMapper.addConverter(propertyCodeConverter);
		//modelMapper.typeMap(EventTypeRequest.class, EventType.class).setConverter(propertyCodeConverter);
		//modelMapper.typeMap(EventTypeRequest.class, EventType.class).addMappings(mapper -> mapper.using(propertyCodeConverter));
		//modelMapper.typeMap(EventTypeRequest.class, EventType.class).addMapping(mapper -> mapper.using(propertyCodeConverter).map(EventTypeRequest.class, EventType.class));

		return modelMapper;
	}

}