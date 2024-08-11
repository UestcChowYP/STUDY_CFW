import com.uestc.crowd.entity.Role;
import com.uestc.crowd.mapper.RoleMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:mybatis-presist-config.xml", "classpath:spring-presist-tx.xml"})
public class testRole {
    @Autowired
    private RoleMapper roleMapper;
    @Test
    public void testAddRole() {
        for (int i=0; i < 235; i++) {
            roleMapper.insert(new Role(null, "testName" + i));
        }
    }
}
