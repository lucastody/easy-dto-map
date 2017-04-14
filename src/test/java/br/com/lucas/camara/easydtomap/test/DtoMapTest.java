package br.com.lucas.camara.easydtomap.test;

import java.util.ArrayList;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import br.com.lucas.camara.easydtomap.EasyDtoMap;
import br.com.lucas.camara.easydtomap.test.pojo.Cidade;
import br.com.lucas.camara.easydtomap.test.pojo.Endereco;
import br.com.lucas.camara.easydtomap.test.pojo.Pessoa;
import br.com.lucas.camara.easydtomap.test.pojo.Telefone;

public class DtoMapTest {
	Pessoa pessoa = null;
	Gson gson = new Gson();
	
	@Before
	public void init() {
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
		
		this.pessoa = pessoa;
	}
	
	@Test
	public void nome_da_pessoa_igual_a_lucas() {
		try {
			Map<String, Object> pessoaDTO = EasyDtoMap.toDtoMap(pessoa);
			
			JsonElement jsonTree = gson.toJsonTree(pessoaDTO);
			JsonObject asJsonObject = jsonTree.getAsJsonObject();
			JsonElement jsonElement = asJsonObject.get("nome");
			
			Assert.assertEquals("Lucas", jsonElement.getAsString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void telefone_da_pessoa_igual_a_999998888() {
		try {
			Map<String, Object> pessoaDTO = EasyDtoMap.toDtoMap(pessoa);
			
			JsonElement jsonTree = gson.toJsonTree(pessoaDTO);
			JsonObject pessoaJson = jsonTree.getAsJsonObject();
			JsonElement jsonTelefones = pessoaJson.get("telefones");
			JsonArray jsonTelefonesArray = jsonTelefones.getAsJsonArray();
			
			JsonObject telefone1 = jsonTelefonesArray.get(0).getAsJsonObject();
			Assert.assertEquals("999998888", telefone1.get("numero").getAsString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
