
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import redis.clients.jedis.Jedis;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet(urlPatterns = "/getName")
public class GetName extends HttpServlet {
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://120.48.15.156/LinuxExam?useUnicode=true"
            + "&characterEncoding=utf8&useSSL=false&serverTimezone=GMT%2B8";
    static final String USER = "root";
    static final String PASS = "Yeluoluo123@";
    static final String SQL_STUDENT_GETNAME = "SELECT * FROM t_student where id = ?";

    static Connection conn = null;
    static Jedis jedis = null;

    public void init() {
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            jedis = new Jedis("127.0.0.1");

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
        getServletContext().log(request.getParameter("id"));
        String json = jedis.get(request.getParameter("id"));
        if (json == null) {
            Student student = getName(Integer.parseInt(request.getParameter("id")));
            Gson gson = new Gson();
            json = gson.toJson(student, new TypeToken<Student>() {
            }.getType());
            jedis.set(request.getParameter("id"), json);

            out.println(json);
        } else {
            out.println(json);
        }

    }


    private Student getName(int id) {
        Student student = new Student();
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(SQL_STUDENT_GETNAME);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                student.id = rs.getInt("id");
                student.name = rs.getString("name");
                student.age = rs.getInt("int");
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

