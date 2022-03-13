package lcx.service;

/**
 * Create by LCX on 2/18/2022 3:53 PM
 */
public class Test1Servlet extends TestServlet {

    @Override
    public void test2() {

    }
}

class test{
    public static void main(String[] args) {
        Test1Servlet test1Servlet = new Test1Servlet();
        System.out.println(test1Servlet.getClass().getName());
    }
}