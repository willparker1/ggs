package com.porkerspicks.ggs;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Horses.class)
public class FavouriteLayerTest {

    @Autowired
    private FavouriteLayer favouriteLayer;

    @Test
    public void layBets() {
        favouriteLayer.layBets();
    }

}

