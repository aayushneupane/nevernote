package Test;

import java.util.HashMap;

import org.springframework.http.ResponseEntity;

import neupane.me.controllers.MainController;
import neupane.me.nevernote.Note;
import neupane.me.nevernote.Notebook;

public class notebookTest {
	
	public static void main(String[] args) {
		
//		MainController main = new MainController();
//		main.loadData();
//		main.createNotebook();
//		main.createNotebook();
//		main.createNotebook();
//		
//		long id = 1;
//		
//		try {
//			ResponseEntity<Notebook> response = main.getNotebookById(id);
//		} catch (NullPointerException e) {
//			System.out.println("NPE");
//		}
		
//		
//		Notebook notebook = new Notebook();
//		long id = notebook.getId();
//		
//		String title = "Note title";
//		String body = "These are notes";
//		String[] tags = {"tag1", "apple"};
//		Note note = new Note();
//		note.setTitle(title);
//		note.setBody(body);
//		note.setTags(tags);
//		
//		
//		HashMap<Long, Note> notemap = notebook.getNotes();
//		long noteId = note.getId();
//		System.out.println(noteId);
//		notemap.put(noteId, note);
//		notebook.setNotes(notemap);
		
		HashMap<Integer, String[]> map = new HashMap<Integer, String[]>();
		String[] tags1 = {"first", "second", "third"};
		String[] tags2 = {"Sat", "sun", "mon"};
		String[] tags3 = {"poem", "story", "note"};
		
		
	}

}
