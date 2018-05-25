package neupane.me.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import neupane.me.errorHandler.ErrorResponse;
import neupane.me.errorHandler.IdNotFoundException;
import neupane.me.nevernote.Notebook;

@RestController
@RequestMapping("/rest")
public class MainController {

	private List<Notebook> notebookList;

	@PostConstruct
	public void loadData() {

		notebookList = new ArrayList<>();

	}

	@GetMapping("/")
	public String sayHello() {
		return "Hello World!";
	}

	@GetMapping("/notebook")
	public List<Notebook> getNotebook(){
		throw new IdNotFoundException("Notebook ID not passed");
	}

	@GetMapping("/notebook/{id}")
	public ResponseEntity<Notebook> getNotebookById(@PathVariable int id){
		System.out.println(notebookList.size());
		if (id >= notebookList.size() || id < 0) {
			throw new IdNotFoundException("Invalid ID: " + id);
		}
		return new ResponseEntity<Notebook> (notebookList.get(id), HttpStatus.OK);
	}
	/**
	 * 
	 * @return the created notebook with status - 201 Created
	 */
	@PostMapping("/notebook")
	public ResponseEntity<Notebook> createNotebook() {
		Notebook notebook = new Notebook();
		System.out.println(notebook.toString());
		notebookList.add(notebook);
		return new ResponseEntity<Notebook> (notebook, HttpStatus.CREATED);
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleException(IdNotFoundException exc) {

		// create a ErrorResponse

		ErrorResponse error = new ErrorResponse();
		
		error.setStatus(HttpStatus.NOT_FOUND.value());
		error.setMessage(exc.getMessage());
		error.setTimeStamp(System.currentTimeMillis());

		// return ResponseEntity

		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}

	// add another exception handler ... to catch any exception (catch all)

	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleException(Exception exc) {

		// create a ErrorResponse
		ErrorResponse error = new ErrorResponse();

		error.setStatus(HttpStatus.BAD_REQUEST.value());
		error.setMessage(exc.getMessage());
		error.setTimeStamp(System.currentTimeMillis());

		// return ResponseEntity		
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}
	
//	@ExceptionHandler ()



}
