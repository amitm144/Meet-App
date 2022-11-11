package demo;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AnotherController {
	@RequestMapping(
			path= {"/message"},
			method = {RequestMethod.GET},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public DemoBoundary message () {
		DemoBoundary rv =  new DemoBoundary("Hello");
		System.err.println(rv);
		return rv;
	}
	
	@RequestMapping(
			path= {"/messages"},
			method = {RequestMethod.GET},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public DemoBoundary[] allMessages () {
		return new DemoBoundary[] {
			new DemoBoundary("object #1"),
			new DemoBoundary("object #2"),
			new DemoBoundary("last object")
		};
	}

}
