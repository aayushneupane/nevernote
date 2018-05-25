package Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

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
		String[] tags3 = {"poem", "story", "note", "mon"};
		
		map.put(1, tags1);
		map.put(2, tags2);
		map.put(3, tags3);
		
		String searchTag = "mon";
		HashMap<Integer, String[]> filter = new HashMap<Integer, String[]>();
		
		for (Entry<Integer, String[]> entry : map.entrySet()) {
			List<String> list = Arrays.asList(entry.getValue());
			if (list.contains(searchTag)) {
				filter.put(entry.getKey(), entry.getValue());
			}
		}
		
		for (Entry<Integer, String[]> entry : filter.entrySet()) {
			for (int i = 0; i < entry.getValue().length; i++) {
				System.out.println(entry.getValue()[i]);
			} 
			System.out.println("---");
		}
		
		
		
//		HashMap<Integer, String[]> filtered = map.entrySet()
//											.stream()
//											.filter(p -> );
		
		
		
	}

}
