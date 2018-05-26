# Nevernote

Nevernote is a REST API that supports creating notebook and notes. Notebooks are essentially collection of notes. 

Tech Used:
  - Java 8
  - Maven 3.3.9
  - Eclipse Oxygen.3a
  - Tomcat 9
  - Postman
  - Frameworks:
      - Spring 5.0.2
      - jUnit 4.12
      - Mockito 1.10

# Installation
Update and launch the project as any other Maven projects. The initial page should be set to {your server}/nevernote/. If you see Nevernote displayed on the screen, the project has been successfully launched. 

# REST endpoints

Base URL: /nevernote/rest/

| URL Structure | Method | Status| Description | Comment
|-|-|-|-|-|
| /notebook | POST | 201 | Creates a new notebook instance
| /notebook/{id} | GET | 200 | Retrieves an existing notebook info | ID has to exist
| /notebook/{id}?tag="" | GET | 200 | Retrieves all the notes from a notebook where the given tag exists | ID has to exist
| /notebook/{id} | DELETE | 204 | Deletes an existing notebook | ID has to exist
| /notebook/{id}/note | POST | 201 | Create a new note for the given notebook ID | Need to send a valid json response body*
| /notebook/{id}/note/{noteId} | GET | 200 | Retrieves the specific note from the notebook | 
| /notebook/{id}/note/{noteId} | DELETE | 204 | Deletes the specific note id for given notebook id
| /notebook/{id}/note/{noteId} | PUT | 204 | Updates the given note id with updated content | Need to send a valid json response body

*response body sample:
{
	"title": "Note Title",
	"body" : "Note body",
	"tags" : ["one", "two"]
}
