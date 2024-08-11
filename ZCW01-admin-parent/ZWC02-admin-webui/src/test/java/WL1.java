public class WL1 {
    public static void main(String[] args) {
        int size = (int) Math.pow(2, 2);
        String[] strs = {".##.", "#..#", "##..", "...#"};
        StringBuffer sb = new StringBuffer();
        for (String str : strs) {
            sb.append(str);
        }
        process(sb.toString());
    }
    public static void process(String mix) {
        int idx = 0;
        int ans = 0;
        while (idx < mix.length()) {
            int cur = idx % 4;
            if (mix.charAt(idx) == '#') {
                ans += (int) Math.pow(2, 4-1-cur);
            }
            if (cur == 3) {
                System.out.println(ans);
                ans = 0;
            }
            idx++;
        }
    }
}
