package se.jonas.traff.sr.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Artist implements Comparable<Artist> {
	@JsonProperty
	private final String name;

	public Artist(String name) {
		this.name = name;
	}

	@Override
	public int compareTo(Artist o) {
		return this.name.compareTo(o.name);
	}
}