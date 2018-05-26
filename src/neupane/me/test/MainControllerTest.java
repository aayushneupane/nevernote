package neupane.me.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import neupane.me.config.AppConfig;
import neupane.me.controllers.MainController;
import neupane.me.controllers.MainControllerService;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = AppConfig.class)
public class MainControllerTest {
	
	private MockMvc mockMvc;
	
	@Mock
	private MainControllerService service;
	
	@InjectMocks
	private MainController mainController;
	
	@Before
	public void setUp() throws Exception{
		mockMvc = MockMvcBuilders.standaloneSetup(mainController).build();
	}

	@Test
	public void testSayHello() throws Exception {
		mockMvc.perform(
				MockMvcRequestBuilders.get("/hello"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().string("Hello"));
	}


}
