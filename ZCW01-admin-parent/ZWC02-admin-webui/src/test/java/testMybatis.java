import com.uestc.crowd.service.api.AdminService;
import com.uestc.crowd.service.impl.AdminServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.uestc.crowd.entity.Admin;
import com.uestc.crowd.mapper.AdminMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:mybatis-presist-config.xml", "classpath:spring-presist-tx.xml"})

public class testMybatis {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private AdminService adminService;

    @Test
    public void TestMybatis() throws IOException, SQLException {
        Connection connection = dataSource.getConnection();
        System.out.println(connection);
        Admin admin = new Admin(null, "Tom", "123123", "汤姆", "tom@qq.com", null);
        int i = adminMapper.insert(admin);
    }
    @Test
    public void testAdminMapper() {
        System.out.println(adminMapper);
    }
    @Test
    public void testLog() {
        // 1.获取Logger对象，这里传入的Class对象就是当前打印日志的类
        Logger logger = LoggerFactory.getLogger(testMybatis.class);
        // 2.根据不同日志级别打印日志
        logger.debug("hello I am Debug level");
        logger.info("info");
        logger.warn("warn");
        logger.error("error");
        logger.debug("hello I am Debug level");
        logger.info("info");
        logger.warn("warn");
        logger.error("error");
        logger.debug("hello I am Debug level");
        logger.info("info");
        logger.warn("warn");
        logger.error("error");
    }
    @Test
    public void saveInsertAdmin() throws Exception {
        adminService.saveAdmin(new Admin(null, "z444", "z123123", "ZYP", "zyp111@qq.com", null));
    }

    @Test
    public void addTestAdmin() {
        for (int i=0; i < 294; i++) {
            adminService.saveAdmin(new Admin(null, "testAdmin" + i, "e10adc3949ba59abbe56e057f20f883e", "testAdmin" + i, "testAdmin" + i + "qq.com", null));
        }

    }
}
