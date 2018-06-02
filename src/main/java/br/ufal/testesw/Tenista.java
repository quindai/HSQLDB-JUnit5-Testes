package br.ufal.testesw;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@org.hibernate.annotations.Entity(dynamicUpdate = true)
@Table(name="tenista")
public class Tenista {

	@Id
	@GeneratedValue
	@Column(name="ID")
	private Long id;
	
	@Column(length=20,name="nome")
	private String nome;

	public Tenista(){
		id = 1L;
	}
	
	public Tenista(Long id, String nome) {
		this.id=id;
		this.nome = nome;
	}
	
	public Tenista(String t) {
		this();
		this.nome = t;
	}
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	

}
