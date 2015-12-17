package test.kankanews.search.action;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

import test.kankanews.search.action.base.JUnitActionBase;

public class QueryActionTest extends JUnitActionBase {

	@Test
	public void testQuery() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		request.setServletPath("/search/query");
		request.setParameter("word", "东电将赔付巨额精神损失费");
		request.setParameter("page", "1");
		request.setParameter("rows", "20");
		request.setParameter("highlight", "true");
		final ModelAndView mav = this.excuteAction(request, response);
		response.setCharacterEncoding("UTF-8");
		System.out.println(response.getContentAsString());
	}

	@Test
	public void testQueryGroup() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		request.setServletPath("/query/queryGroup");
		final ModelAndView mav = this.excuteAction(request, response);
	}

}
