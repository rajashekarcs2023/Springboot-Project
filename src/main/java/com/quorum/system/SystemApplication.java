package com.quorum.system;


import ch.qos.logback.core.model.Model;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Collections;
import java.util.List;

@SpringBootApplication
public class SystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(SystemApplication.class, args);
	}

}

@Document(collection = "notes")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
class Note {
	@Id
	private String id;
	private String description;

	@Override
	public String toString() {
		return description;
	}
}


interface NotesRepository extends MongoRepository<Note, String> {

}

@Controller
class KNoteController {

	@Autowired
	private NotesRepository notesRepository;

	@GetMapping("/")
	public String index(ModelMap model) {
		getAllNotes(model);
		return "index";
	}

	private void getAllNotes(ModelMap model) {
		List<Note> notes = notesRepository.findAll();
		Collections.reverse(notes);
		model.addAttribute("notes", notes);
	}

	private void saveNote(String description, ModelMap model) {
		if (description!=null && !description.trim().isEmpty()) {
			notesRepository.save(new Note(null, description.trim()));
			model.addAttribute("description", "");
		}
	}

}