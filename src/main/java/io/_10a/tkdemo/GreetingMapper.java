package io._10a.tkdemo;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "cdi")
public interface GreetingMapper {

	@Mapping(source = "lang", target = "language")
	Greeting fromGreetingDTO(GreetingDTO greetingDTO);

}
