package next.controller;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import core.mvc.JsonView;
import next.dao.QuestionDao;
import next.model.Question;
import core.mvc.AbstractController;
import core.mvc.ModelAndView;

public class ListController extends AbstractController {
	private QuestionDao questionDao = new QuestionDao();

	@Override
	public ModelAndView execute(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		List<Question> questions;
		questions = questionDao.findAll();

		return createModelAndView(request.getHeader("Accept"),questions);
	}

	ModelAndView createModelAndView(String accept, List<Question> questions) {
		ModelAndView mav = jstlView("/list.jsp");
		mav.addObject("questions", questions);
		return mav;
	}

	public static class api extends ListController{
		@Override
		ModelAndView createModelAndView(String accept, List<Question> questions) {
			if(accept==null)
				return super.createModelAndView(accept, questions);
			if(isJsonAccept(accept)){

				List<Map> qList = new ArrayList<>(questions.size());
				questions.forEach((q)->qList.add(q.toMap()));
				Map<String,Object> model = new HashMap<>(1);
				model.put("size",qList.size());
				model.put("list",qList);

				return new ModelAndView(new JsonView(),model);
			}
			else if(false/*TODO XML*/){
				//Do Nothing
				return null;
			}
			return super.createModelAndView(accept, questions);
		}
	}
}
