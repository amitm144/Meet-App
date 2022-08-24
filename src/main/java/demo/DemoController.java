package demo;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {
	@RequestMapping(
		path= {"/demo/{name}"},
		method = {RequestMethod.GET},
		produces = {MediaType.APPLICATION_JSON_VALUE})
	public DemoBoundary hello (@PathVariable("name") String name) {
		if (name == null
			|| name.trim().isEmpty()) {
			name = "Anonymous";
		}
		return new DemoBoundary("Hello " + name);
	}
}
