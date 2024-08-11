import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        // 注意 hasNext 和 hasNextLine 的区别
        int empLen = Integer.parseInt(in.nextLine());
        Set<String> emp = new HashSet<>();
        for (int i=0; i<empLen; i++) {
            emp.add(in.nextLine());
        }
        int idLen = Integer.parseInt(in.nextLine());
        for (int i=0; i < idLen; i++) {
            String cur = process(emp, in.nextLine());
            if ("error".equals(cur)) System.out.println(cur);
        }
        System.out.println("ok");
    }
    public static String process(Set<String> emp, String id) {
        String empId = id.substring(0, 6);
        if (!emp.contains(empId)) return "error";
        String birth = id.substring(6, 14);
        int year = Integer.parseInt(birth.substring(0, 4));
        int month = Integer.parseInt(birth.substring(4, 6));
        int day = Integer.parseInt(birth.substring(6, 8));
        if (!(year >= 1900 && year <= 2023 &&
                month>=1 && month <= 12 && day <= 31 && day>=1)) return "error";
        int sum = 0;
        for (int i=0; i<id.length(); i++) {
            sum += id.charAt(i) - '0';
        }
        if (! (sum % 8 == 1)) return "error";
        return "ok";
    }
}
