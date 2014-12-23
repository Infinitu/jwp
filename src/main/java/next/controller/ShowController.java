package next.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import core.mvc.JsonView;
import next.dao.AnswerDao;
import next.dao.QuestionDao;
import next.model.Answer;
import next.model.Question;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.mvc.AbstractController;
import core.mvc.ModelAndView;
import core.utils.ServletRequestUtils;

public class ShowController extends AbstractController {
	private static final Logger logger = LoggerFactory.getLogger(ShowController.class);
	
	private QuestionDao questionDao = new QuestionDao();
	private AnswerDao answerDao = new AnswerDao();
	
	@Override
	public ModelAndView execute(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Question question;
		List<Answer> answers;
		long questionId = ServletRequestUtils.getRequiredLongParameter(request, "questionId");
		logger.debug("questionId : {}", questionId);
		question = questionDao.findById(questionId);
		answers = answerDao.findAllByQuestionId(questionId);
		return createModelAndView(request.getHeader("Accept"),question, answers);
	}

	ModelAndView createModelAndView(String accept, Question question, List<Answer> answers) {
		ModelAndView mav = jstlView("/show.jsp");
		mav.addObject("question", question);
		mav.addObject("answers", answers);
		return mav;
	}

	public static class api extends ShowController{

		@Override
		ModelAndView createModelAndView(String accept,Question question, List<Answer> answers) {
			if(accept==null)
				return super.createModelAndView(accept, question, answers);
			if(isJsonAccept(accept)){

				List<Map> aList = new ArrayList<>(answers.size());
				answers.forEach((q) -> aList.add(q.toMap()));
				Map<String,Object> model = new HashMap<>(1);
				model.put("answerSize", aList.size());
				model.put("answers",aList);
				model.put("question", question.toMap());

				return new ModelAndView(new JsonView(),model);
			}
			else if(false/*TODO XML*/){
				//Do Nothing
				return null;
			}
			return super.createModelAndView(accept, question, answers);
		}
	}
}
