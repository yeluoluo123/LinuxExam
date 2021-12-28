

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet(urlPatterns = "/delete")
public class DeleteStudent extends HttpServlet {

    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://120.48.15.156/LinuxExam?useUnicode=true"
            + "&characterEncoding=utf8&useSSL=false&serverTimezone=GMT%2B8";
    static final String USER = "root";
    static final String PASS = "Yeluoluo123@";

    static final String SQL_STUDENT_DELETA = "DELETE FROM t_student WHERE id = ? ";
    static Connection conn = null;

    public void init() {
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void destory() {
        try {
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Student req = getRequestBody(request);
        getServletContext().log(req.toString());
        PrintWriter out = response.getWriter();

        out.println(deleteStudent(req));
        out.flush();
        out.close();

    }

    private int deleteStudent(Student req) {
        PreparedStatement stmt = null;
        int retcode = -1;
        try {
            stmt = conn.prepareStatement(SQL_STUDENT_DELETA);


            stmt.setInt(1, req.id);

            int row = stmt.executeUpdate();
            if (row > 0) {
                retcode = row;
            } else {
                retcode = 0;
            }
            stmt.close();

        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return retcode;
    }

    private Student getRequestBody(HttpServletRequest request) throws IOException {
        Student student = new Student();
        StringBuffer bodyj = new StringBuffer();
        String line = null;
        BufferedReader reader = request.getReader();
        while ((line = reader.readLine()) != null) {
            bodyj.append(line);
            Gson gson = new Gson();
            student = gson.fromJson(bodyj.toString(), new TypeToken<Student>() {
            }.getType());
        }
        return student;
    }
}
class Student {
    int id;
    String name;
    int age;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
}
