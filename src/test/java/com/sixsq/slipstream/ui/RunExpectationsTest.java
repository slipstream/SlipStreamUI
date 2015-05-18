package com.sixsq.slipstream.ui;

import expectations.junit.ExpectationsTestRunner;
import org.junit.runner.RunWith;

@RunWith(expectations.junit.ExpectationsTestRunner.class)
public class RunExpectationsTest implements ExpectationsTestRunner.TestSource {

    public String testPath() {
        return "clj/test/";
    }
}
