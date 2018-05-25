package neupane.me.nevernote;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.http.HttpStatus;

public class Notebook {

	private static final AtomicInteger count = new AtomicInteger(0); 
	private long id;
	private long createdDate;
	private long modifiedDate;
	private HashMap<Long, Note> notes;
		
	public Notebook() {
		this.id = count.incrementAndGet();
		this.createdDate = System.currentTimeMillis();
		this.notes = new HashMap<Long, Note>();
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(long createdDate) {
		this.createdDate = createdDate;
	}
	public long getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(long modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	
	public HashMap<Long, Note> getNotes() {
		return notes;
	}

	public void setNotes(HashMap<Long, Note> notes) {
		this.notes = notes;
	}

	@Override
	public String toString() {
		return "Notebook [id=" + id + ", createdDate=" + createdDate + ", modifiedDate=" + modifiedDate + ", notes="
				+ notes + "]";
	}
}
