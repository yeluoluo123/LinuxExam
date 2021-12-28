

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = "/find")
public class FindStudent extends HttpServlet {
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://120.48.15.156/LinuxExam?useUnicode=true"
            + "&characterEncoding=utf8&useSSL=false&serverTimezone=GMT%2B8";
    static final String USER = "root";
    static final String PASS = "Yeluoluo123@";
    static final String SQL_STUDENT_FIND = "SELECT * FROM t_student";

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

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=utf8");
        response.setCharacterEncoding("utf8");
        PrintWriter out = response.getWriter();
        List<Student> student = findStudent();
        Gson gson = new Gson();
        String json = gson.toJson(student, new TypeToken<List<Student>>() {
        }.getType());
        out.println(json);
        out.flush();
        out.close();


    }


    private List<Student> findStudent() {
        List<Student> studentList = new ArrayList<Student>();

        Statement stmt = null;
        try {

            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(SQL_STUDENT_FIND);

            while (rs.next()) {
                Student student = new Student();
                student.setId(rs.getInt("id"));
                student.setName(rs.getString("name"));
                student.setAge(rs.getInt("age"));
                studentList.add(student);
            }
            rs.close();
            stmt.close();

        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return studentList;
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

