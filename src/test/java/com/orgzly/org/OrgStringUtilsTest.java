package com.orgzly.org;

import org.junit.Assert;
import org.junit.Test;

public class OrgStringUtilsTest {
    @Test
    public void testCharacterWidth() {
        String str = "A";
        Assert.assertEquals(1, OrgStringUtils.stringWidth(str));
    }

    @Test
    public void testWideWidth() {
        String str = "漢";
        Assert.assertEquals(2, OrgStringUtils.stringWidth(str));
    }
    @Test
    public void testSurrogatePairWidth() {
        String str = "𥑮";
        Assert.assertEquals(2, OrgStringUtils.stringWidth(str));
    }

    @Test
    public void testStringWidth() {
        String str = "A漢字𥑮";
        Assert.assertEquals(7, OrgStringUtils.stringWidth(str));
    }
}