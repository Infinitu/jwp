package next.controller;

import core.mvc.AbstractController;
import core.mvc.ModelAndView;
import next.dao.AnswerDao;
import next.model.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by infinitu on 14. 12. 23..
 */
public class AddAnswerController extends AbstractController {
    private static final Logger logger = LoggerFactory.getLogger(ShowController.class);
    private AnswerDao answerDao = new AnswerDao();


    private static ModelAndView nullModelAndView = new ModelAndView((model, request, response) -> {/*DO Nothing method*/});

    @Override
    public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

        long qId = Long.parseLong(request.getParameter("questionId"));
        String writer = request.getParameter("writer");
        String contents = request.getParameter("contents");

        Answer ans = new Answer(writer,contents,qId);

        answerDao.insert(ans);

        return nullModelAndView;
    }
}
