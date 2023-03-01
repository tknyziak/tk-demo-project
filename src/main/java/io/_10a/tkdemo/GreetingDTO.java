package io._10a.tkdemo;

import java.util.Collection;

public record GreetingDTO(String lang, String greeting, Collection<GreeterDTO> greeters) {
}
