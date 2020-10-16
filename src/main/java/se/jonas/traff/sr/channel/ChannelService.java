package se.jonas.traff.sr.channel;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonNode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import se.jonas.traff.sr.domain.Artist;
import se.jonas.traff.sr.domain.RecordLabel;

@Service
public class ChannelService {

	private final RestTemplate restTemplate;
	private final String baseUrl;

	@Autowired
	private ChannelService(RestTemplate restTemplate, 
							@Value("${sr.default.baseurl:http://api.sr.se/api/v2}") String baseUrl) {
		this.restTemplate = restTemplate;
		this.baseUrl = baseUrl;
	}

	public Set<RecordLabel> getRecordLabelsOfChannel(String channelId, String startDate, String endDate, Integer limit) {
		String url = UriComponentsBuilder.fromUriString(this.baseUrl)
			.pathSegment("playlists", "getplaylistbychannelid")
			.queryParam("id", channelId)
			.queryParam("startdatetime", startDate)
			.queryParam("enddatetime", endDate)
			.queryParam("size", limit)
			.queryParam("format", "json")
			.toUriString();

		ResponseEntity<JsonNode> response = restTemplate.getForEntity(url, JsonNode.class);
		if (response.getStatusCode() == HttpStatus.OK) {
			if (!response.hasBody()) return Collections.emptySet();
			return parseResponse(response);
		} else {
			throw new HttpClientErrorException(response.getStatusCode());
		}
	}

	private Set<RecordLabel> parseResponse(ResponseEntity<JsonNode> response) {
		JsonNode body = response.getBody();
		Map<String, Set<Artist>> artistsGroupedByReordLabel = new TreeMap<>((s1, s2) -> s1.compareTo(s2));
		body.findPath("song").forEach(songNode -> {
			String recordLabel = songNode.findPath("recordlabel").asText();
			Set<Artist> artists = artistsGroupedByReordLabel.get(recordLabel);
			if (artists == null) {
				artists = new TreeSet<>((s1, s2) -> s1.compareTo(s2));
			}
			artists.add(new Artist(songNode.findPath("artist").asText()));
			artistsGroupedByReordLabel.put(recordLabel, artists);
		});
		return artistsGroupedByReordLabel.entrySet()
			.stream()
			.map(entry -> new RecordLabel(entry.getValue(), entry.getKey()))
			.collect(Collectors.toCollection(() -> new TreeSet<>((s1, s2) -> s1.compareTo(s2))));
	}
}
