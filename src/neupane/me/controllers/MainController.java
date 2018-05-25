package neupane.me.controllers;

import java.util.HashMap;

import javax.annotation.PostConstruct;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import neupane.me.errorHandler.ErrorResponse;
import neupane.me.errorHandler.IdNotFoundException;
import neupane.me.nevernote.Note;
import neupane.me.nevernote.Notebook;

@RestController
@RequestMapping("/rest")
public class MainController {

	private HashMap<Long, Notebook> notebookMap;

	@PostConstruct
	public void loadData() {

		notebookMap = new HashMap<>();

	}

	@GetMapping("/")
	public String sayHello() {
		return "Hello World!";
	}

	@GetMapping("/notebook")
	public ResponseEntity<String> getNotebook(){
		throw new IdNotFoundException("Notebook ID not passed");
	}
	
	@DeleteMapping("/notebook")
	public ResponseEntity<String> deleteNotebookBad(){
		throw new IdNotFoundException("Notebook ID not passed");
	}

	/**
	 * Search through the tags within a notebook
	 * @param id - notebook ID
	 * @param tag - tag being searched
	 * @return
	 */
	@GetMapping("/notebook/{id}")
	public ResponseEntity<Notebook> getNotebookById(@PathVariable long id, 
			@RequestParam(value="tag", required=false) String tag){
		System.out.println(notebookMap.size());
		System.out.println(id);
		System.out.println(tag);
		if (!isValidNotebookId(id)) {
			throw new IdNotFoundException("Invalid Notebook ID " + id);
		} else {
			if (tag == null)
				return new ResponseEntity<Notebook> (notebookMap.get(id), HttpStatus.OK);
			else {
				Notebook filteredNotebook = new Notebook();
				return new ResponseEntity<Notebook> (notebookMap.get(id), HttpStatus.OK);
			}
		}
	}
	


	/**
	 * 
	 * @return the created notebook with status - 201 Created
	 */
	@PostMapping("/notebook")
	public ResponseEntity<Notebook> createNotebook() {
		Notebook notebook = new Notebook();
		System.out.println(notebook.toString());
		notebookMap.put(notebook.getId(), notebook);
		return new ResponseEntity<Notebook> (notebook, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/notebook/{id}")
	public ResponseEntity<String> deleteNotebook(@PathVariable long id){
		if (!isValidNotebookId(id)) {
			throw new IdNotFoundException("Invalid Notebook ID " + id);
		}
		Notebook notebook = notebookMap.remove(id);
		if (notebook == null) {
			System.out.println("Notebook " + id + " already deleted");
			throw new IdNotFoundException("Notebook doesnt exist");
		} 
		System.out.println(notebook);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@PostMapping("/notebook/{id}/note")
	public ResponseEntity<Notebook> createNote(@PathVariable long id, @RequestBody Note note ) {
		
		if (!isValidNotebookId(id)) {
			throw new IdNotFoundException("Invalid Notebook ID " + id);
		} 
		
		System.out.println(note);
		
		Notebook notebook = notebookMap.get(id);
		System.out.println(notebook.toString());
		HashMap<Long, Note> noteMap = notebook.getNotes();
		long noteId = note.getId();
		noteMap.put(noteId, note);
		notebook.setNotes(noteMap);
		
		return new ResponseEntity<Notebook> (notebook, HttpStatus.CREATED);
	}
	
	@GetMapping("/notebook/{id}/note/{noteId}")
	public ResponseEntity<Note> getNote(@PathVariable long id, @PathVariable long noteId){
		Note note;
		HashMap<Long, Note> noteMap; 
		
		if (!isValidNotebookId(id)) {
			throw new IdNotFoundException("Invalid Notebook ID " + id);
		} else {
			Notebook notebook = notebookMap.get(id);
			
			noteMap = notebook.getNotes();
			if (!isValidNoteId(noteMap, noteId)){
				throw new IdNotFoundException("Invalid Note ID " + noteId);
			} else {
				note = noteMap.get(noteId);
			}
		}
		
		return new ResponseEntity<Note>(note, HttpStatus.OK);
	}
	
	@DeleteMapping("/notebook/{id}/note/{noteId}")
	public ResponseEntity<String> deleteNote(@PathVariable long id, @PathVariable long noteId){
		Note deletedNote;
		HashMap<Long, Note> noteMap; 

		if (!isValidNotebookId(id)) {
			throw new IdNotFoundException("Invalid Notebook ID " + id);
		} else {
			Notebook notebook = notebookMap.get(id);
			
			noteMap = notebook.getNotes();
			if (!isValidNoteId(noteMap, noteId)){
				throw new IdNotFoundException("Invalid Note ID " + noteId);
			} else {
				deletedNote = noteMap.remove(noteId);
			}
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	private boolean isValidNotebookId(long id) {
		if (id > notebookMap.size() || id <= 0 || notebookMap.get(id) == null) {
			return false;
		} else 
			return true;
	}
	private boolean isValidNoteId(HashMap<Long, Note> noteMap, long id) {
		if (id > noteMap.size() || id <= 0 || noteMap.get(id) == null || noteMap == null) {
			return false;
		} else 
			return true;
	}
	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleException(IdNotFoundException exc) {
		
		System.out.println(exc.getMessage());

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
	
	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleException(NullPointerException exc) {
		
		// create a ErrorResponse
		ErrorResponse error = new ErrorResponse();
		
		error.setStatus(HttpStatus.BAD_REQUEST.value());
		error.setMessage(exc.getMessage());
		error.setTimeStamp(System.currentTimeMillis());
		
		// return ResponseEntity		
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleException(HttpRequestMethodNotSupportedException exc) {
		
		// create a ErrorResponse
		ErrorResponse error = new ErrorResponse();
		
		error.setStatus(HttpStatus.BAD_REQUEST.value());
		error.setMessage(exc.getMessage());
		error.setTimeStamp(System.currentTimeMillis());
		
		// return ResponseEntity		
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}




}
