/*
 *  Copyright (C) Avinash Ramana - All Rights Reserved
 *  * Unauthorized copying of this file, via any medium is strictly prohibited
 *  * Proprietary and confidential
 *  * Written by Avinash Ramana <bitorius@gent00.com>, 2020
 *
 */

import com.gent00.datapoint.filter.model.IOStatFilter;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class RunMyTest {

    @Test
    public void testInvoke() throws IOException {
        InputStream is = RunMyTest.class.getResourceAsStream("randwrite.4k.combi.out");
        if (is == null) {
            Assertions.fail("Cannot find resource file");
        }
        File f = File.createTempFile("io.viz", "iostat");
        f.deleteOnExit();
        OutputStream os = new FileOutputStream(f);
        IOUtils.copy(is, os);
        is.close();
        os.close();
        IOStatFilter.main(new String[]{f.getAbsolutePath()});

    }
}