/*
 * Copyright © 2009 Sun.Hao(https://www.crazy-coder.cn/)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package lodsve.mybatis.test;

import com.google.common.collect.Lists;
import lodsve.mybatis.dao.DemoMapper;
import lodsve.mybatis.domain.Demo;
import lodsve.mybatis.domain.Sex;
import org.apache.ibatis.exceptions.PersistenceException;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runners.MethodSorters;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import javax.persistence.NoResultException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * .
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2018/9/27 8:32 PM
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BaseDAOTest extends BaseTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private DemoMapper demoMapper;

    @Before
    public void init() {
        demoMapper = session.getMapper(DemoMapper.class);
    }

    /**
     * no
     */
    @Test
    public void test00_count() {
        int count = demoMapper.count();
        Assert.assertEquals(30, count);
    }

    /**
     * 31
     */
    @Test
    public void test01_Insert() {
        Demo demo = new Demo();
        demo.setName("root31");
        demo.setPwd("123456");
        demo.setEnabled(true);

        int i = demoMapper.save(demo);
        // 更新条数
        Assert.assertEquals(1, i);
        // id
        Assert.assertEquals(Long.valueOf(31), demo.getId());

        Demo demo2 = demoMapper.findById(31L);
        // enabled
        Assert.assertTrue(demo2.getEnabled());
    }

    /**
     * 1
     */
    @Test
    public void test02_Delete() {
        boolean result = demoMapper.deleteById(1L);

        // 是否更新成功
        Assert.assertTrue(result);
        // 查询不到
        Assert.assertNull(demoMapper.findById(1L));
    }

    /**
     * 2
     */
    @Test
    public void test03_Update() {

        // 1======================
        Demo demo1 = new Demo();
        demo1.setId(2L);
        demo1.setName("root2-----");
        demo1.setPwd("654321");
        int i1 = demoMapper.update(demo1);
        // 更新条数
        Assert.assertEquals(1, i1);

        // 结果
        Demo demo11 = demoMapper.findById(2L);
        Assert.assertNotNull(demo11);
        Assert.assertEquals("root2-----", demo11.getName());
        Assert.assertEquals("654321", demo11.getPwd());

        Demo d1 = demoMapper.findById(2L);
        Assert.assertEquals(Integer.valueOf(1), d1.getVersion());
    }

    /**
     * 20
     */
    @Test
    public void test04_UpdateAllColumns() {

        // 1======================
        Demo demo1 = new Demo();
        demo1.setId(20L);
        demo1.setName("root2-----");
        int i1 = demoMapper.updateAll(demo1);
        // 更新条数
        Assert.assertEquals(1, i1);

        // 结果
        Demo demo11 = demoMapper.findById(20L);
        Assert.assertNotNull(demo11);
        Assert.assertEquals("root2-----", demo11.getName());
        Assert.assertNull(demo1.getPwd());

        Demo d1 = demoMapper.findById(20L);
        Assert.assertEquals(Integer.valueOf(1), d1.getVersion());
    }

    /**
     * 17
     */
    @Test
    public void test05_Get() {
        Assert.assertTrue(demoMapper.logicDeleteById(17L));

        Demo demo = demoMapper.findById(17L);
        Assert.assertNotNull(demo);

        Demo demo2 = demoMapper.findEnabledById(17L);
        Assert.assertNull(demo2);
    }

    /**
     * 18 30 29
     */
    @Test
    public void test06_LogicDelete() {
        boolean result = demoMapper.logicDeleteById(18L);
        Assert.assertTrue(result);

        Demo demo = demoMapper.findById(18L);
        Assert.assertEquals(Integer.valueOf(1), demo.getVersion());
        Assert.assertFalse(demo.getEnabled());

        session.commit();
        boolean result1 = demoMapper.logicDeleteByIdWithModifiedBy(30L, 10L);
        session.commit();
        boolean result2 = demoMapper.logicDeleteByIdWithModifiedBy(29L, 10L);
        Assert.assertTrue(result1);
        Assert.assertTrue(result2);

        Demo demo1 = demoMapper.findById(30L);
        Assert.assertEquals(Integer.valueOf(1), demo1.getVersion());
        Assert.assertFalse(demo1.getEnabled());
        Assert.assertEquals(Long.valueOf(10), demo1.getLastModifiedBy());
    }

    /**
     * no
     */
    @Test
    public void test07_countEnabled() {
        int count = demoMapper.countEnabled();
        Assert.assertEquals(25, count);
    }

    /**
     * 10
     */
    @Test
    public void test08_Version() {
        boolean result = demoMapper.updateNameById(10L, "test-version");
        Assert.assertTrue(result);

        Demo demo = demoMapper.findById(10L);
        Assert.assertEquals("test-version", demo.getName());
    }

    /**
     * no
     */
    @Test
    public void test09_Pagination() {
        Sort sort = new Sort(Sort.Direction.DESC, "id", "name");

        Page<Demo> page = demoMapper.list(new PageRequest(0, 10, sort), "root", 0);
        // 总条数
        Assert.assertEquals(29, page.getTotalElements());
        // 页数
        Assert.assertEquals(3, page.getTotalPages());
        // 每页条数
        Assert.assertEquals(10, page.getSize());
        // 是否有下一页
        Assert.assertTrue(page.hasNext());
        // 是否有上一页
        Assert.assertFalse(page.hasPrevious());
    }

    /**
     * 13 14 15
     */
    @Test
    public void test10_GetByIds() {
        List<Long> ids = Lists.newArrayList(13L, 14L, 15L);
        List<Demo> demos = demoMapper.findByIds(ids);
        Assert.assertEquals(Integer.valueOf(3), Integer.valueOf(demos.size()));
        demoMapper.logicDeleteById(13L);
        List<Demo> demos2 = demoMapper.findEnabledByIds(ids);
        Assert.assertEquals(2, demos2.size());
        Assert.assertEquals(Integer.valueOf(2), Integer.valueOf(demos2.size()));
    }

    /**
     * 11
     */
    @Test
    public void test11_UpdateAll() {
        Demo demo = demoMapper.findById(11L);

        demo.setName("hello world!");
        Assert.assertTrue(demoMapper.updateAllColumnByEntity(demo));
    }

    /**
     * no
     */
    @Test
    public void test12_Enum() {
        // insert
        Demo demo1 = new Demo();
        demo1.setEnabled(true);
        demo1.setName("demo111111111111");
        demo1.setAge("222");
        demo1.setDisabledDate(LocalDateTime.now());
        demo1.setLastModifiedDate(LocalDateTime.now());
        demo1.setPwd("123");
        demo1.setLastModifiedBy(1L);
        demo1.setSex(Sex.MALE);
        demo1.setVersion(0);

        int i = demoMapper.save(demo1);
        session.commit();
        Assert.assertEquals(1, i);
        Long id = demo1.getId();

        // get
        Demo demo2 = demoMapper.findById(id);
        Assert.assertNotNull(demo2);
        Assert.assertEquals("demo111111111111", demo2.getName());

        // update
        Demo demo3 = new Demo();
        demo3.setId(id);
        demo3.setSex(Sex.FEMALE);
        int count = demoMapper.update(demo3);
        session.commit();
        Assert.assertEquals(1, count);

        // get
        Demo demo4 = demoMapper.findById(id);
        Assert.assertEquals("demo111111111111", demo4.getName());

        // logicDelete
        Assert.assertTrue(demoMapper.logicDeleteById(id));
        session.commit();

        // get
        Demo demo5 = demoMapper.findEnabledById(id);
        Assert.assertNull(demo5);

        // query
        Page<Demo> page = demoMapper.list(new PageRequest(0, 10), "root", 0);
        Assert.assertEquals(10, page.getContent().size());

        // delete
        Assert.assertTrue(demoMapper.deleteById(id));
    }

    /**
     * no
     */
    @Test
    public void test13_BatchSave() {
        List<Demo> demos = Lists.newArrayList();
        int size = 5;
        for (int i = 1; i <= size; i++) {
            Demo demo = new Demo();
            demo.setName("root" + i);
            demo.setPwd("pwd" + i);
            demo.setEnabled(true);

            demos.add(demo);
        }

        int result = demoMapper.batchSave(demos);
        Assert.assertEquals(5, result);
    }

    /**
     * no
     */
    @Test
    public void test14_logicDeleteNonRecord() {
        String expect = "Can't find a record for 'ID = 100' in table 't_demo'!";

        thrown.expect(new TypeSafeMatcher<PersistenceException>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("expects exception: ").appendValue(NoResultException.class.getName());
                description.appendText(" and expects exception message: ").appendValue(expect);
            }

            @Override
            protected boolean matchesSafely(PersistenceException exception) {
                Throwable throwable = exception.getCause();
                return throwable instanceof NoResultException && expect.equals(throwable.getMessage());
            }
        });

        demoMapper.logicDeleteByIdWithModifiedBy(100L, 10L);
    }

    @Test
    public void test15_exist() {
        Assert.assertTrue(demoMapper.isExist(2L));
        Assert.assertFalse(demoMapper.isExist(100L));

        demoMapper.logicDeleteById(16L);
        Assert.assertTrue(demoMapper.isExist(16L));
        Assert.assertFalse(demoMapper.isExistEnabled(16L));
    }

    @Test
    public void test16_listAll() {
        List<Demo> demos = demoMapper.listAll();
        Assert.assertTrue(0 < demos.size());
    }
}
