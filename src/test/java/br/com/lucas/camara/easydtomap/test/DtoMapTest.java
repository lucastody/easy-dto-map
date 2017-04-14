package br.com.lucas.camara.easydtomap.test;

import java.util.ArrayList;
import java.util.Map;

import br.com.lucas.camara.easydtomap.EasyDtoMap;
import br.com.lucas.camara.easydtomap.test.pojo.Cidade;
import br.com.lucas.camara.easydtomap.test.pojo.Endereco;
import br.com.lucas.camara.easydtomap.test.pojo.Pessoa;
import br.com.lucas.camara.easydtomap.test.pojo.Telefone;

public class DtoMapTest {
	
	public void test() {
		Pessoa pessoa = new Pessoa();
		pessoa.setId(1L);
		pessoa.setNome("Lucas");
		pessoa.setSobrenome("Câmara");
		
		Cidade cidade1 = new Cidade(1L, "Porteirinha");
		
		pessoa.setEnderecos(new ArrayList<>());
		pessoa.getEnderecos().add(new Endereco(1L, "111", "Rua 111", pessoa, cidade1));
		pessoa.getEnderecos().add(new Endereco(2L, "111", "Rua 111", pessoa, cidade1));
		
		pessoa.setTelefones(new ArrayList<>());
		pessoa.getTelefones().add(new Telefone(1L, "11", "999998888", pessoa));
		
		try {
			Map<String, Object> pessoaDTO = EasyDtoMap.toDtoMap(pessoa);
			
			System.out.println(pessoaDTO);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
