package Addressbook.Addressbook2;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import de.inmediasp.tutorial.addressbook.service.App;

@RunWith(SpringJUnit4ClassRunner.class)
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
	public void deleteAddress() throws Exception {

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
	public void updateAddress() throws Exception {
		ResultActions mvcResult = mvc.perform(MockMvcRequestBuilders.put(uri).param("id", "12").content(addressXml)
				.contentType(MediaType.APPLICATION_XML));

		mvcResult.andExpect(status().isOk());
	}


	@Test
	public void createAddress() throws Exception {
		ResultActions mvcResult = mvc
				.perform(MockMvcRequestBuilders.post(uri).content(addressXml).contentType(MediaType.APPLICATION_XML));

		mvcResult.andExpect(status().isOk());
	}

	@Test
	public void createAddresses() throws Exception {
		String u = uri + "all";
		String xml = "<addressList>" + addressXml + "</addressList>";

		ResultActions mvcResult = mvc
				.perform(MockMvcRequestBuilders.post(u).content(xml).contentType(MediaType.APPLICATION_XML));

		mvcResult.andExpect(status().isOk());
	}

	@Test
	public void getAddress() throws Exception {
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).param("id", "6").accept(MediaType.TEXT_XML))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();

		assertEquals(200, status);
	}

	@Test
	public void getAddresses() throws Exception {
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.TEXT_XML)).andReturn();

		int status = mvcResult.getResponse().getStatus();

		assertEquals(200, status);
	}

	@Test
	public void getFilteredAddresses() throws Exception {
		String searchAddressXml = "<address>\r\n" + "	<firstname>Helmut</firstname>\r\n"
				+ "	<lastname></lastname>\r\n" + "	<email></email>\r\n" + "</address>";

		getFilteredAddresses(searchAddressXml);
	}

	private void getFilteredAddresses(String xml) throws Exception {
		String u = uri + "search";
		ResultActions mvcResult = mvc.perform(MockMvcRequestBuilders.post(u).content(xml)
				.contentType(MediaType.APPLICATION_XML).accept(MediaType.TEXT_XML));

		mvcResult.andExpect(status().isOk());
		mvcResult.andExpect(content().contentType("text/xml"));
	}
}