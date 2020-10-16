package se.jonas.traff.sr.channel;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import se.jonas.traff.sr.domain.RecordLabel;

@RestController
@RequestMapping("/api/v1/channels")
@Validated
public class ChannelController {
	
	private static final String DATE_REGEX = "[0-9]{4}-[0-9]{2}-[0-9]{2}";
	private static final String YYYY_MM_DD = "yyyy-MM-dd";

	private final ChannelService channelService;
	private final int defaultNumberOfSongs;

	@Autowired
	public ChannelController(ChannelService channelService, 
							@Value("${sr.default.numberofsongs:100}") int defaultNumberOfSongs) {
		this.channelService = channelService;
		this.defaultNumberOfSongs = defaultNumberOfSongs;
	}

	@ExceptionHandler
	public ResponseEntity<String> handleHttpClientExceptions(HttpClientErrorException e) {
		return ResponseEntity.status(e.getStatusCode()).body(e.getStatusText());
	}

	@ExceptionHandler
	public ResponseEntity<Map<Object, Object>> handleConstraintViolationException(ConstraintViolationException e) {
		Map<Object, Object> errors = e.getConstraintViolations()
			.stream()
			.collect(Collectors.toMap(ConstraintViolation::getPropertyPath, ConstraintViolation::getMessage));
		return ResponseEntity.badRequest().body(errors);
	}

	@GetMapping(value = "/{channelId}/recordlabels")
	public Set<RecordLabel> getRecodLabelsOfChannel(
			@PathVariable("channelId") String channelId,
			@Pattern(regexp = DATE_REGEX) @RequestParam(value = "fromDate", required = false) String fromDate,
			@Pattern(regexp = DATE_REGEX) @RequestParam(value = "toDate", required = false) String toDate,
			@Min(1) @Max(100) @RequestParam(value = "limit", required = false) Integer limit) {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(YYYY_MM_DD);
		return channelService.getRecordLabelsOfChannel(channelId, 
			Optional.ofNullable(fromDate).orElse(getDefaultStartDate(formatter)), 
			Optional.ofNullable(toDate).orElse(getDefaultEndDate(formatter)), 
			Optional.ofNullable(limit).orElse(defaultNumberOfSongs));
	}

	private String getDefaultEndDate(DateTimeFormatter formatter) {
		return LocalDate.now().plusDays(1).format(formatter);
	}

	private String getDefaultStartDate(DateTimeFormatter formatter) {
		return LocalDate.now().format(formatter);
	}
}
