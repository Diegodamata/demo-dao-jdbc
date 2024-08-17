package model.entities;

import java.io.Serializable;
import java.util.Objects;

//o seriaLizabla é uma interface em Java que marca uma classe cujas instâncias podem ser convertidas em um fluxo de bytes
//A serialização é útil para salvar o estado de um objeto para persistência (por exemplo, gravar em um arquivo trafegar em rede, ou banco de dados)
public class Department implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String name;
	
	public Department() {
	}

	public Department(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	//hashCode e equals para minha classe ser comparada pelo conteudo não por ponteiro
	
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Department other = (Department) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "Department [id=" + id + ", name=" + name + "]";
	}	
}
