import org.junit.Test;

public class Hello {
    public static void main(String[] args) {
        System.out.println("hello world");
    }
    @Test
    public void test() {
        String str = "01";
        int a = Integer.parseInt(str);
        System.out.println(a);
    }
}