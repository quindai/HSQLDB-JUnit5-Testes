package br.ufal.testesw;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Torneio {

	@Id
	@GeneratedValue
	private Long id;
	
	private List<Tenista> tenistas = new ArrayList<>();

	public Torneio() {
		this.id = 1L;
	}
	public Torneio(Long id, String t) {
		this.id = id;
		tenistas.add(new Tenista(t));
	}
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Tenista> getTenistas() {
		return tenistas;
	}

	public void setTenistas(List<Tenista> tenistas) {
		this.tenistas = tenistas;
	}

	
	
}
