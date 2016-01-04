package test.kankanews.search.action;

import java.util.Date;

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
		request.setParameter("word2", "上海“十三五”规划建议亮点解读：强调“约束性”发展指标");
		// request.setParameter("author", "天脉素材");
		request.setParameter("page", "100");
		request.setParameter("rows", "20");
		request.setParameter("type", "1");
		request.setParameter("highlight", "true");
		request.setParameter("isduplicate", "false");
		System.out.println(new Date());
		final ModelAndView mav = this.excuteAction(request, response);
		response.setCharacterEncoding("UTF-8");
		System.out.println(response.getContentAsString());
		System.out.println(new Date());
	}

	@Test
	public void testQueryGroup() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		request.setServletPath("/query/queryGroup");
		final ModelAndView mav = this.excuteAction(request, response);
	}

}