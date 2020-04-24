package ut.confluence.plugins.pages;

import org.junit.Test;
import confluence.plugins.pages.api.MyPluginComponent;
import confluence.plugins.pages.impl.MyPluginComponentImpl;

import static org.junit.Assert.assertEquals;

public class MyComponentUnitTest
{
    @Test
    public void testMyName()
    {
        MyPluginComponent component = new MyPluginComponentImpl(null);
        assertEquals("names do not match!", "myComponent",component.getName());
    }
}