package br.com.lucas.camara.easydtomap.test;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import br.com.lucas.camara.easydtomap.EasyDtoMap;
import br.com.lucas.camara.easydtomap.EasyDtoMapFactory;
import br.com.lucas.camara.easydtomap.TypeConverter;
import br.com.lucas.camara.easydtomap.test.pojo.Cidade;
import br.com.lucas.camara.easydtomap.test.pojo.Endereco;
import br.com.lucas.camara.easydtomap.test.pojo.Pessoa;
import br.com.lucas.camara.easydtomap.test.pojo.Sistema;
import br.com.lucas.camara.easydtomap.test.pojo.Telefone;

public class DtoMapTest {
	private static final Logger LOGGER = Logger.getLogger(DtoMapTest.class.getName());
	
	private Pessoa pessoa = null;
	private List<Pessoa> pessoas = null;
	private Gson gson = new GsonBuilder().setPrettyPrinting().create();
	private static EasyDtoMap easyDtoMap = null;
	
	static {
		easyDtoMap = EasyDtoMapFactory.create();
		
		easyDtoMap.addTypeConverter(LocalDate.class, new TypeConverter<LocalDate>() {
			public Object convertNew(LocalDate value) {
				Instant instant = value.atStartOfDay(ZoneId.systemDefault()).toInstant();
				return instant.toEpochMilli();
			}
		});
		
		easyDtoMap.addTypeConverter(Date.class, new TypeConverter<Date>() {
			public Object convertNew(Date value) {
				return value.getTime();
			}
		});
		
		easyDtoMap.addTypeConverter(Boolean.class, new TypeConverter<Boolean>() {
			public Object convertNew(Boolean value) {
				return value ? "Sim" : "Não";
			}
		});
	}
	
	private void log(Throwable throwable) {
		LOGGER.log(Level.SEVERE, throwable.getMessage(), throwable);
	}
	
	private void imprimirJson(Object obj) {
		System.out.println(gson.toJson(obj));
	}
	
	@Before
	public void inicializarObjeto() {
		Pessoa pessoa = new Pessoa();
		pessoa.setId(1L);
		pessoa.setNome("Fulano");
		pessoa.setSobrenome("Tal");
		pessoa.setNascimento(LocalDate.of(1987, 4, 28));
		
		Cidade cidade1 = new Cidade(1L, "Nome da Cidade");
		
		pessoa.setEnderecos(new ArrayList<>());
		pessoa.getEnderecos().add(new Endereco(1L, "111", "Rua 111", pessoa, cidade1));
		pessoa.getEnderecos().add(new Endereco(2L, "111", "Rua 111", pessoa, cidade1));
		
		pessoa.setTelefones(new ArrayList<>());
		pessoa.getTelefones().add(new Telefone(1L, "99", "999998888", pessoa));
		
		this.pessoa = pessoa;
	}
	
	@Before
	public void inicializarColecao() {
		pessoas = new ArrayList<>();
		
		for(int i = 0; i < 1; i++) {
			inicializarObjeto();
			pessoas.add(pessoa);
		}
	}
	
	@Test
//	@Ignore
	public void cria_dto_de_objeto() {
		LOGGER.info("cria_dto_de_objeto");
		
		try {
			Map<String, Object> pessoaDTO = easyDtoMap.fromObject(pessoa);
			imprimirJson(pessoaDTO);
			Assert.assertNotNull(pessoaDTO);
		} catch (Exception e) {
			log(e);
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
//	@Ignore
	public void cria_dto_de_colecao() {
		LOGGER.info("cria_dto_de_colecao");
		
		try {
			Object[] pessoasDTO = easyDtoMap.fromCollection(pessoas);
			imprimirJson(pessoasDTO);
			Assert.assertNotNull(pessoasDTO);
		} catch (Exception e) {
			log(e);
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
//	@Ignore
	public void cria_dto_de_objeto_simulando_erro_ao_recuperar_enderecos() {
		LOGGER.info("cria_dto_de_objeto_simulando_sessao_fechada_ao_recuperar_enderecos");
		
		try {
			pessoa.error = true;
			Map<String, Object> pessoaDTO = easyDtoMap.fromObject(pessoa);
			imprimirJson(pessoaDTO);
			Assert.assertNotNull(pessoaDTO);
		} catch (Exception e) {
			log(e);
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
//	@Ignore
	public void nome_da_pessoa_igual_a_Fulano() {
		LOGGER.info("nome_da_pessoa_igual_a_Fulano");
		
		try {
			Map<String, Object> pessoaDTO = easyDtoMap.fromObject(pessoa);
			
			JsonElement jsonTree = gson.toJsonTree(pessoaDTO);
			JsonObject asJsonObject = jsonTree.getAsJsonObject();
			JsonElement jsonElement = asJsonObject.get("nome");
			Assert.assertEquals("Fulano", jsonElement.getAsString());
		} catch (Exception e) {
			log(e);
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
//	@Ignore
	public void telefone_da_pessoa_igual_a_999998888() {
		LOGGER.info("telefone_da_pessoa_igual_a_999998888");
		
		try {
			Map<String, Object> pessoaDTO = easyDtoMap.fromObject(pessoa);
			
			JsonElement jsonTree = gson.toJsonTree(pessoaDTO);
			JsonObject pessoaJson = jsonTree.getAsJsonObject();
			JsonElement jsonTelefones = pessoaJson.get("telefones");
			JsonArray jsonTelefonesArray = jsonTelefones.getAsJsonArray();
			
			JsonObject telefone1 = jsonTelefonesArray.get(0).getAsJsonObject();
			Assert.assertEquals("999998888", telefone1.get("numero").getAsString());
		} catch (Exception e) {
			log(e);
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
//	@Ignore
	public void cria_dto_de_objeto_usando_type_converter_para_localdate() {
		LOGGER.info("cria_dto_de_objeto_usando_type_converter_para_localdate");
		
		try {
			Map<String, Object> pessoaDTO = easyDtoMap.fromObject(pessoa);
			imprimirJson(pessoaDTO);
			Assert.assertNotNull(pessoaDTO);
		} catch (Exception e) {
			log(e);
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
//	@Ignore
	public void cria_dto_de_objeto_com_tipos_primitivos() {
		LOGGER.info("cria_dto_de_objeto_com_tipos_primitivos");
		
		Sistema sistema = new Sistema();
		sistema.setAtivo(true);
		sistema.setDataAlteracao(new Date());
		sistema.setDataInclusao(new Date());
		sistema.setDescricao("Sistema ABC");
		sistema.setId(1L);
		sistema.setNome("Sistema ABC");
		
		try {
			Map<String, Object> pessoaDTO = easyDtoMap.fromObject(sistema);
			imprimirJson(pessoaDTO);
			Assert.assertNotNull(pessoaDTO);
		} catch (Exception e) {
			log(e);
			Assert.fail(e.getMessage());
		}
	}
}