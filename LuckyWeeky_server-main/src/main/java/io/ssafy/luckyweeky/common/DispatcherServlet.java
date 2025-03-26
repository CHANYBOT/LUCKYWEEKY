package io.ssafy.luckyweeky.common;

import com.google.gson.JsonObject;
import io.ssafy.luckyweeky.common.config.XmlBeanFactory;
import io.ssafy.luckyweeky.common.implement.Controller;
import io.ssafy.luckyweeky.common.util.stream.ImageStreamUtil;
import io.ssafy.luckyweeky.common.util.url.RequestUrlPath;
import io.ssafy.luckyweeky.common.infrastructure.s3.S3Fileloader;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

public class DispatcherServlet extends HttpServlet {
    private static String webInfPath;

    public static String getWebInfPath(){
        return webInfPath;
    }


    @Override
    public void init() throws ServletException {

        webInfPath = getServletContext().getRealPath("/WEB-INF");
        try {
            String[] xmlPaths = new String[2];
            xmlPaths[0] = getServletContext().getRealPath("/WEB-INF/beans/model.xml");
            xmlPaths[1] = getServletContext().getRealPath("/WEB-INF/beans/controller.xml");
            new XmlBeanFactory(xmlPaths);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to init DispatcherServlet", e);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        S3Fileloader s3Fileloader = S3Fileloader.getInstance();
        s3Fileloader.close();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String uri = request.getRequestURI();
        if(uri.equals("/")){
            // health check, HTTP OK return
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        String path = RequestUrlPath.getURI(request.getRequestURI())[0];
        if(path.equals("qweSJqwo")){
            try {
                ImageStreamUtil.streamImage(request,response);
            } catch (Exception e) {
                JsonObject respJson = new JsonObject();
                respJson.addProperty("result","false");
                respJson.addProperty("error", e.getMessage());
                response.getWriter().append(respJson.toString());
                response.getWriter().close();
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {// CLOVA STT를 위한 설정
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }


        PrintWriter out = response.getWriter();
        response.setStatus(HttpServletResponse.SC_OK);
        JsonObject respJson = new JsonObject();
        respJson.addProperty("result","true");
        try {
            String path = RequestUrlPath.getURI(request.getRequestURI())[0]; //aB12Xz
            Controller controller = (Controller) XmlBeanFactory.getBean(path);
            if (controller == null) {
                throw new IllegalArgumentException("C02");
            }
            controller.handleRequest(request, response, respJson);
        } catch (Exception e) {
            respJson.addProperty("result","false");
            respJson.addProperty("errCode",e.getMessage());
        }finally {
            out.append(respJson.toString());
            out.close();
        }
    }
}
