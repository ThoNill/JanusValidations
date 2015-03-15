package org.janus.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * 
 * @author THOMAS NILL Lizenz GPLv3
 * 
 */

@RunWith(Suite.class)
@SuiteClasses({ First.class, Second.class, XMLTest.class, StandardTest.class })
public class AllTests {

}
