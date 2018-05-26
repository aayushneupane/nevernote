package neupane.me.controllers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import neupane.me.errorHandler.ErrorResponse;
import neupane.me.errorHandler.IdNotFoundException;
import neupane.me.nevernote.Note;
import neupane.me.nevernote.Notebook;

/*
 * /nevernote/rest/...
 */
@RestController
@RequestMapping("/rest")
public class MainController {

	private MainControllerService service;
	private HashMap<Long, Notebook> notebookMap;

	public MainController(MainControllerService service) {
		this.notebookMap = new HashMap<>();
		this.service = service;
	}

	//	public MainController() {
	//		this.notebookMap = new HashMap<>();
	//	}


	//	@PostConstruct
	//	public void loadData() {
	//
	//		notebookMap = new HashMap<>();
	//
	//	}

	/*
	 * Sample to make sure service is working. Service isnt used anywhere else
	 */
	@GetMapping("/")
	public String sayHello() {
		return service.hello();
	}

	/**
	 * GET /notebook should return error. Need ID
	 * @return IdNotFoundException
	 */

	@GetMapping("/notebook")
	public ResponseEntity<String> getNotebook(){
		throw new IdNotFoundException("Notebook ID not passed");
	}

	/**
	 * DELETE /notebook should return error. Need ID passed
	 * @return IdNotFoundException
	 */

	@DeleteMapping("/notebook")
	public ResponseEntity<String> deleteNotebookBad(){
		throw new IdNotFoundException("Notebook ID not passed");
	}

	/**
	 * Search through the tags within a notebook
	 * @param id - notebook ID
	 * @param tag - tag being searched
	 * @return Notebook, 200
	 */
	@GetMapping("/notebook/{id}")
	public ResponseEntity<Notebook> getNotebookById(@PathVariable long id, 
			@RequestParam(value="tag", required=false) String tag){

		if (!isValidNotebookId(id)) {
			throw new IdNotFoundException("Invalid Notebook ID " + id);
		} else {

			//if tag doesnt exist, dont need to do any further computing. Return notebook
			if (tag == null)
				return new ResponseEntity<Notebook> (notebookMap.get(id), HttpStatus.OK);
			else {

				//create a new 'filtered' notebook and fill that with appropriate notes, notebook entities
				Notebook notebook = notebookMap.get(id);
				Notebook filteredNotebook = new Notebook();

				filteredNotebook.setCreatedDate(notebook.getCreatedDate());
				filteredNotebook.setModifiedDate(notebook.getModifiedDate());
				filteredNotebook.setId(notebook.getId());

				//create a new 'filtered' note and fill that with appropriate entities
				HashMap<Long, Note> notes = notebookMap.get(id).getNotes();
				HashMap<Long, Note> filter = new HashMap<Long, Note>();

				//Notes are stored as HashMap for better retrieval time. 
				//tags are stored as arrays. 
				//need to loop through each notes hashmap, convert the tags to arraylist, and check if a tag exists in that arraylist
				//if tag does exist, copy the content of that hashmap to the filtered hashmap. 
				//update the filtered notebook with the filtered hashmap, and return filtered notebook. 

				for (Entry<Long, Note> entry : notes.entrySet()) {
					List<String> list = Arrays.asList(entry.getValue().getTags());
					if (list.contains(tag)) {
						filter.put(entry.getKey(), entry.getValue());
					}
				}
				filteredNotebook.setNotes(filter);
				return new ResponseEntity<Notebook> (filteredNotebook, HttpStatus.OK);
			}
		}
	}



	/**
	 * POST /notebook
	 * @return the created notebook with status - 201 Created
	 */
	@PostMapping("/notebook")
	public ResponseEntity<Notebook> createNotebook() {
		Notebook notebook = new Notebook();
		System.out.println(notebook.toString());
		notebookMap.put(notebook.getId(), notebook);
		return new ResponseEntity<Notebook> (notebook, HttpStatus.CREATED);
	}

	/**
	 * DELETE notebook/{id}
	 * @param id - long notebook identifier 
	 * @return 204 (no data)
	 */ 
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

	/**
	 * POST notebook/{id}/note creates a new note with given requestbody json
	 * sample json: {
						“title”: “Note Title”,
						“body” : “Note body”,
						“tags” : [“one”, “two”]
					}
	 * @param id - long note identifier
	 * @param note - json object
	 * @return Notebook, 201
	 */
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
	
	/**
	 * GET /notebook/{id}/note/{noteId}
	 * @param id - notebook ID
	 * @param noteId - note ID
	 * @return Note, 200
	 */

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

	
	/**
	 * DELETE /notebook/{id}/note/{noteId} - Deletes the specific noteid from the given note
	 * @param id - Notebook ID
	 * @param noteId - Note ID
	 * @return 204 - no data
	 */
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
	
	/**
	 * PUT /notebook/{id}/note/{noteId} - updates the given noteId for given Id
	 * @param id - notebook identifier
	 * @param noteId - note idenfitier
	 * @param note - json object 
	 * @return - 204
	 */

	@PutMapping("/notebook/{id}/note/{noteId}")
	public ResponseEntity<String> modifyNote(@PathVariable long id,
			@PathVariable long noteId, @RequestBody Note note ) {

		HashMap<Long, Note> noteMap; 
		Notebook notebook;
		Note oldNote;
		long timeStamp = System.currentTimeMillis();

		if (!isValidNotebookId(id)) {
			throw new IdNotFoundException("Invalid Notebook ID " + id);
		} else {
			notebook = notebookMap.get(id);
			noteMap = notebook.getNotes();

			if (!(isValidNoteId(noteMap, noteId))) {
				throw new IdNotFoundException("Invalid Note ID " + id);
			} else {
				oldNote = noteMap.get(noteId);
			}
		}

		System.out.println(note);
		note.setId(oldNote.getId());
		note.setModifiedDate(timeStamp);
		notebook.setModifiedDate(timeStamp);
		System.out.println(note);

		System.out.println(notebook.toString());
		noteMap.put(noteId, note);

		return new ResponseEntity<String> (HttpStatus.OK);
	}

	/**
	 * Check if a notebook ID is valid or not. 
	 * not valid if:
	 * ID is bigger than the size of notebookMap. Notebook map uses hashmap so every new entry is auto updated to +1
	 * ID is negative. 
	 * Notebook in given ID is null
	 * @param id - notebook identifier
	 * @return boolean
	 */
	private boolean isValidNotebookId(long id) {
		if (id > notebookMap.size() || id <= 0 || notebookMap.get(id) == null) {
			return false;
		} else 
			return true;
	}
	
	/**
	 * Check if a note ID is valid or not. 
	 * not valid if:
	 * ID isof  bigger than the size noteMap. Note uses hashmap so every new entry is auto updated to +1
	 * ID is negative. 
	 * Note in given ID is null
	 * @param id - notebook identifier
	 * @param noteMap - HashMap that contains <Long, Note> object
	 * @return boolean
	 */
	private boolean isValidNoteId(HashMap<Long, Note> noteMap, long id) {
		if (id > noteMap.size() || id <= 0 || noteMap.get(id) == null || noteMap == null) {
			return false;
		} else 
			return true;
	}
	
	/*
	 * Exception Handlers
	 */
	
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
