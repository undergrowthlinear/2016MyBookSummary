##2016书单总结--看透SpringMvc源代码分析与实践-概述
    主要从Servlet、Tomcat、SpringMvc启动、SpringMvc组件几个方面进行阐述
    
    
1. Servlet相关知识点(3个主要类)
	![这里写图片描述](http://img.blog.csdn.net/20170123235431230?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdW5kZXJncm93dGg=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)
2. Tomcat相关知识点(8个主要概念)
![这里写图片描述](http://img.blog.csdn.net/20170123235500812?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdW5kZXJncm93dGg=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

![这里写图片描述](http://img.blog.csdn.net/20170123235517827?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdW5kZXJncm93dGg=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)
3. SpringMvc启动知识点(3个主要类)
   ![这里写图片描述](http://img.blog.csdn.net/20170123235548062?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdW5kZXJncm93dGg=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

   ![这里写图片描述](http://img.blog.csdn.net/20170123235608796?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdW5kZXJncm93dGg=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)
   ![这里写图片描述](http://img.blog.csdn.net/20170124001035975?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdW5kZXJncm93dGg=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)
4. SpringMvc组件相关知识点(9大组件--HHHRRRRRF)
	* ![这里写图片描述](http://img.blog.csdn.net/20170123235646343?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdW5kZXJncm93dGg=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)
	* ![这里写图片描述](http://img.blog.csdn.net/20170123235757813?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdW5kZXJncm93dGg=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)
	* ![这里写图片描述](http://img.blog.csdn.net/20170123235810313?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdW5kZXJncm93dGg=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)
	* ![这里写图片描述](http://img.blog.csdn.net/20170123235837283?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdW5kZXJncm93dGg=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)
	* ![这里写图片描述](http://img.blog.csdn.net/20170124000042724?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdW5kZXJncm93dGg=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)
	* ![这里写图片描述](http://img.blog.csdn.net/20170124000246117?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdW5kZXJncm93dGg=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)
	* ![这里写图片描述](http://img.blog.csdn.net/20170124000508473?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdW5kZXJncm93dGg=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)
	* ![这里写图片描述](http://img.blog.csdn.net/20170124000552417?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdW5kZXJncm93dGg=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)
	* ![这里写图片描述](http://img.blog.csdn.net/20170124000640505?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdW5kZXJncm93dGg=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)
5. 请求代码路径---->
    *  org.springframework.web.servlet.FrameworkServlet.doGet
    * org.springframework.web.servlet.FrameworkServlet.processRequest
    * org.springframework.web.servlet.DispatcherServlet.doService
    * org.springframework.web.servlet.DispatcherServlet.doDispatch
    * SpringMvc重写了HttpServlet的service的重定向方法
    * javax.servlet.http.HttpServlet.service(javax.servlet.ServletRequest, javax.servlet.ServletResponse)
    * javax.servlet.http.HttpServlet.service(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
    * javax.servlet.http.HttpServlet.doGet
6. 示例代码位于-- https://github.com/undergrowthlinear/2016MyBookSummary.git
7. 示例代码位于-- https://github.com/undergrowthlinear/springmvcinaction.git 