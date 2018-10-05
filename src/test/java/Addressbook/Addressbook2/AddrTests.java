package Addressbook.Addressbook2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import de.inmediasp.tutorial.addressbook.service.App;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = App.class)
@WebAppConfiguration
public class AddrTests {
   protected MockMvc mvc;
   @Autowired
   WebApplicationContext webApplicationContext;

   @Before
   public void setUp() {
      mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
   }

   static String addressXml= "<address>\r\n" + 
     		"	<firstname>Helmut</firstname>\r\n" + 
     		"	<lastname>Rotkohl</lastname>\r\n" + 
     		"	<email>hrotk@gmail.com</email>\r\n" + 
     		"</address>";

   @Test
   public void deleteAddress() throws Exception {
      final String uri = "/";
  
       
      final ResultActions mvcResult = mvc.perform(MockMvcRequestBuilders.put(uri).content(addressXml).contentType(MediaType.APPLICATION_XML));
      
      mvcResult.andExpect(status().isOk());
      mvcResult.andDo(new ResultHandler() {
		
		@Override
		public void handle(MvcResult result) throws Exception {
				String id= result.getResponse().getContentAsString();
			
				ResultActions mvcResult2 = mvc.perform(MockMvcRequestBuilders.delete(uri).param("id", id));
		       
		       mvcResult2.andExpect(status().isOk());
		}
	});
   }

   @Test
   public void createAddress() throws Exception {
      String uri = "/";
      
      ResultActions mvcResult = mvc.perform(MockMvcRequestBuilders.put(uri).content(addressXml).contentType(MediaType.APPLICATION_XML));
      
      mvcResult.andExpect(status().isOk());
   }

   @Test
   public void updateAddress() throws Exception {
      String uri = "/";
      
      ResultActions mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).param("id", "12").content(addressXml).contentType(MediaType.APPLICATION_XML));
      
      mvcResult.andExpect(status().isOk());
   }

   @Test
   public void createAddresses() throws Exception {
      String uri = "/all";
      String xml= "<addressList>" + addressXml + "</addressList>";
      
      ResultActions mvcResult = mvc.perform(MockMvcRequestBuilders.put(uri).content(xml).contentType(MediaType.APPLICATION_XML));
      
      mvcResult.andExpect(status().isOk());
   }

   @Test
   public void getAddress() throws Exception {
      String uri = "/";
      MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).param("id", "6")
         .accept(MediaType.TEXT_XML)).andReturn();
      
      int status = mvcResult.getResponse().getStatus();
      
      assertEquals(200, status);
   }
   
   @Test
   public void getAddresses() throws Exception {
      String uri = "/all";
      MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).
         accept(MediaType.TEXT_XML)).andReturn();
      
      int status = mvcResult.getResponse().getStatus();
      
      assertEquals(200, status);
   }

   @Test
   public void getFilteredAddresses() throws Exception {
	   getFilteredAddresses("firstname", "Max");
	   getFilteredAddresses("lastname", "Rotkohl");
	   getFilteredAddresses("email", "hrotk@gmail.com");
   }

   private void getFilteredAddresses(String attr, String  val) throws Exception {
      String uri = "/search";
      ResultActions mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).param(attr, val).
         accept(MediaType.TEXT_XML));
      
      mvcResult.andExpect(status().isOk());
      mvcResult.andExpect(content().contentType("text/xml"));
   }
}