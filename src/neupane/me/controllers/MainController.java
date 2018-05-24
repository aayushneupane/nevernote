package neupane.me.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest")
public class MainController {
	
	@GetMapping("/")
	public String sayHello() {
		return "Hello World!";
	}

}
