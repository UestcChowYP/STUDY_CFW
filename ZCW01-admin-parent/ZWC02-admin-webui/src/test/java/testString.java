import com.uestc.crowd.entity.Role;
import com.uestc.crowd.util.CrowdUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;


public class testString {
    @Test
    public void testString2Md5() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String source = "zyp005";
        String out = passwordEncoder.encode(source);
        System.out.println(out);

    }

    @Test
    public void test() {
        int arr[] = new int[3];
    }

}
