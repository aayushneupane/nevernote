package neupane.me.nevernote;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class Note {
	
	private long id;
	private long createdDate;
	private long modifiedDate;
	private String title;
	private String[] tags;
	private String body;
	private static final AtomicInteger count = new AtomicInteger(0);
	
	public Note() {
		this.id = count.incrementAndGet();
		this.createdDate = System.currentTimeMillis();
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String[] getTags() {
		return tags;
	}

	public void setTags(String[] tags) {
		this.tags = tags;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public static AtomicInteger getCount() {
		return count;
	}

	@Override
	public String toString() {
		return "Note [id=" + id + ", createdDate=" + createdDate + ", modifiedDate=" + modifiedDate + ", title=" + title
				+ ", tags=" + Arrays.toString(tags) + ", body=" + body + "]";
	}
	
	
	
	

	

}
