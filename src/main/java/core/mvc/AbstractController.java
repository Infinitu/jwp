package core.mvc;

public abstract class AbstractController implements Controller {
	protected ModelAndView jstlView(String forwardUrl) {
		return new ModelAndView(new JstlView(forwardUrl));
	}
	
	protected ModelAndView jsonView() {
		return new ModelAndView(new JsonView());
	}

	protected boolean isJsonAccept(String accept) {
		return accept.contains("application/json")||
				accept.contains("application/x-javascript")||
				accept.contains("text/javascript")||
				accept.contains("text/x-json")||
				accept.contains("text/x-javascript");
	}
}
