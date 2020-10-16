package se.jonas.traff.sr.domain;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RecordLabel implements Comparable<RecordLabel> {
	@JsonProperty
	private final Set<Artist> artists;
	@JsonProperty
	private final String name;

	public RecordLabel(Set<Artist> artists, String name) {
		this.artists = artists;
		this.name = name;
	}

	@Override
	public int compareTo(RecordLabel o) {
		return this.name.compareTo(o.name);
	}	
}
