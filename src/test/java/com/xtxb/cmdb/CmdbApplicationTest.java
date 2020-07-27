package com.xtxb.cmdb;


import com.xtxb.cmdb.service.ModelService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(
        locations = {"classpath:application.properties"})
public abstract class CmdbApplicationTest {


    @Before
    public void beforAll(){

    }

    @After
    public void afterAll(){

    }
}
