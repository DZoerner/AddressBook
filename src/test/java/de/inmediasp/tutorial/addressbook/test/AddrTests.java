package de.inmediasp.tutorial.addressbook.test;

import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import de.inmediasp.test.ordering.OrderedJRunner;
import de.inmediasp.test.ordering.TestFirst;
import de.inmediasp.test.ordering.TestLast;
import de.inmediasp.tutorial.addressbook.service.App;

@RunWith(OrderedJRunner.class)
@SpringBootTest(classes = App.class)
@WebAppConfiguration
public class AddrTests {
	private static final String uri = "/";
	
	protected MockMvc mvc;
	@Autowired
	WebApplicationContext webApplicationContext;

	@Before
	public void setUp() {
		mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	static String addressXml = "<address>\r\n" + "	<firstname>Helmut</firstname>\r\n"
			+ "	<lastname>Rotkohl</lastname>\r\n" + "	<email>hrotk@gmail.com</email>\r\n" + "</address>";

	@Test
	public void testDeleteAddress() throws Exception {

		//test 404 on not present resources
		String u= uri + "0";
		ResultActions mvcResult2 = mvc.perform(MockMvcRequestBuilders.delete(u));
		mvcResult2.andExpect(status().isNotFound());

		// Create sample value for delete-test
		final ResultActions mvcResult = mvc
				.perform(MockMvcRequestBuilders.post(uri).content(addressXml).contentType(MediaType.APPLICATION_XML));

		mvcResult.andExpect(status().isOk());

		mvcResult.andDo(new ResultHandler() {

			@Override
			public void handle(MvcResult result) throws Exception {
				String id = result.getResponse().getContentAsString();
				String u= uri + id;
				ResultActions mvcResult2 = mvc.perform(MockMvcRequestBuilders.delete(u));

				mvcResult2.andExpect(status().isOk());
			}
		});
	}

	@Test
	@TestLast
	public void testDeleteAddresses() throws Exception {

		//test 404 on not present resources
		ResultActions mvcResult = mvc.perform(MockMvcRequestBuilders.delete(uri));
		mvcResult.andExpect(status().isOk());

	}

	@Test
	public void testUpdateAddress() throws Exception {
		ResultActions mvcResult = mvc.perform(MockMvcRequestBuilders.put(uri).param("id", "12").content(addressXml)
				.contentType(MediaType.APPLICATION_XML));

		mvcResult.andExpect(status().isOk());
	}


	@Test
	@TestFirst
	public void testCreateAddress() throws Exception {
		ResultActions mvcResult = mvc
				.perform(MockMvcRequestBuilders.post(uri).content(addressXml).contentType(MediaType.APPLICATION_XML));

		mvcResult.andExpect(status().isOk());
	}

	@Test
	@TestFirst
	public void testCreateAddresses() throws Exception {
		String u = uri + "all";
		String xml = "<addressList>" + addressXml + "</addressList>";

		ResultActions mvcResult = mvc
				.perform(MockMvcRequestBuilders.post(u).content(xml).contentType(MediaType.APPLICATION_XML));

		mvcResult.andExpect(status().isOk());
	}

	@Test
	public void testGetAddress() throws Exception {
		String u= uri + "1";
		ResultActions mvcResult = mvc.perform(MockMvcRequestBuilders.get(u).accept(MediaType.TEXT_XML));

		mvcResult.andExpect(content().contentType("text/xml"));
		mvcResult.andExpect(content().string(containsString("Helmut")));
		mvcResult.andExpect(status().isOk());
	}

	@Test
	public void testGetAddresses() throws Exception {
		ResultActions mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.TEXT_XML));

		mvcResult.andExpect(content().contentType("text/xml"));
		mvcResult.andExpect(content().string(containsString("Helmut")));
		mvcResult.andExpect(status().isOk());
	}

	@Test
	public void testGetFilteredAddresses() throws Exception {

		getFilteredAddresses("firstname", "Helmut");
		getFilteredAddresses("lastname", "Rotkohl");
		getFilteredAddresses("email", "hrotk@gmail.com");
	}

	private void getFilteredAddresses(String attr, String value) throws Exception {
		String searchAddressXml = "<address>\r\n" + "	<" + attr + ">" + value + "</" + attr + ">\r\n"
				+ "</address>";

		getFilteredAddresses(searchAddressXml);
	}

	private void getFilteredAddresses(String xml) throws Exception {
		String u = uri + "search";
		ResultActions mvcResult = mvc.perform(MockMvcRequestBuilders.post(u).content(xml)
				.contentType(MediaType.APPLICATION_XML).accept(MediaType.TEXT_XML));

		mvcResult.andExpect(status().isOk());
		mvcResult.andExpect(content().contentType("text/xml"));
		mvcResult.andExpect(content().string(containsString("Helmut")));
	}
}