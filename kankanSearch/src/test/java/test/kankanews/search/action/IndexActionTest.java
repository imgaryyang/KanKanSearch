package test.kankanews.search.action;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

import test.kankanews.search.action.base.JUnitActionBase;

public class IndexActionTest extends JUnitActionBase {

	@Test
	public void testUserShow() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		request.setServletPath("/index/test");
		final ModelAndView mav = this.excuteAction(request, response);
	}
}