### localhost:8080 렌더링 과정.

WEB-INF/web.xml line 31.
```xml
	<welcome-file-list>
		<welcome-file>index.jsp</welcome-file>
	</welcome-file-list>
```

위의 설정에 의해 브라우저에서 ``` localhost:8080 ``` 을 호출 했을때, ```index.jsp``` 가 호출된다.

index.jsp
```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	response.sendRedirect("/list.next");
%>
```

```sendRedirect()```에 의해 list.next로 HTTP Redirect가 이루어 진다.

core.mvc.FrontController.java  line 15
```java
...

@WebServlet("*.next")
public class FrontController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(FrontController.class);

...
```
index.jsp에서 Redirect된 리퀘스트는 ```@WebServlet("*.next")``` 에 의해 ```FrontController``` 서블릿을 호출한다.

```core.mvc.RequestMapping.java```  line 16
```java
...

    public void initMapping() {
		mappings.put("/list.next", new ListController());
		mappings.put("/show.next", new ShowController());
		mappings.put("/form.next", new ForwardController("form.jsp"));

		logger.info("Initialized Mapping Completed!");
	}

...
```

```FrontController``` 의 ```init()```에서 초기화된 ```RequestMapping```은 세가지 컨트롤러를 정의하고 있다.

```core.mvc.FrontController.java``` line 36
```java
Controller controller = rm.findController(urlExceptParameter(req.getRequestURI()));
```
에서 URI에 따라  ```RequestMapping``` 에서 ```Controller```를 받는다.

```core.mvc.FrontController.java``` line 38
```java
    try {
			mav = controller.execute(req, resp);
			View view = mav.getView();
			view.render(mav.getModel(), req, resp);
		} catch (Throwable e) {
			logger.error("Exception : {}", e);
			throw new ServletException(e.getMessage());
		}
```

에서 ```Controller.execute()```를 통해 ```ModelAndView```를 리턴받고, 리턴받은 ```ModelAndView``` 에서 ```Model```과 ```View```를 받을 수 있다. ```View.render()```를 통해 ```resp```에 결과를 출력하는 방식이다.


```next.controller.ListController.java``` line 17
```java
...

	@Override
	public ModelAndView execute(HttpServletRequest request,    HttpServletResponse response)
			throws Exception {
		questions = questionDao.findAll();

		ModelAndView mav = jstlView("list.jsp");
		mav.addObject("questions", questions);
		return mav;
	}

...
```

```RequestMapping.java```에서 정의된 ```ListController``` 의 ```.execute()```라 호출된다. ```ListController``` 는 ```AbstractController.java``` line 4 에 정의된 ```jstlView()```에 의하여 ```core.mvc.JstlView```를 포함한 ```ModelAndView```가 생성된다.

```core.mvc.JstlView.java``` line 19
```java
...

	@Override
	public void render(Map<String, ?> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		if (viewName.startsWith(DEFAULT_REDIRECT_PREFIX)) {
			response.sendRedirect(viewName.substring(DEFAULT_REDIRECT_PREFIX.length()));
			return;
		}

		Set<String> keys = model.keySet();
		for (String key : keys) {
			request.setAttribute(key, model.get(key));
		}

		RequestDispatcher rd = request.getRequestDispatcher(viewName);
		rd.forward(request, response);

...
```

```questtionDao.findAll()```에서 가져온 ```List<Question>```를 ```JstlView``` 에서 ```HttpServletRequest.setAttribute```로 request에 추가하여 ```RequesDispatcher``` 에 의해 해당이름의 JSP 요청으로 forward한다.

해당 ```list.jsp``` 에서는```List<Question>```을 입력받아 Html형태로 출력하게 된다.
