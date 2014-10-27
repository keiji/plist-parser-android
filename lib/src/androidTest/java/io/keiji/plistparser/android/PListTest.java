/*
 * Copyright (C) 20011 Keiji Ariyama C-LIS CO., LTD.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.keiji.plistparser.android;

import android.test.InstrumentationTestCase;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

public class PListTest extends InstrumentationTestCase {
    private static final String TAG = PListTest.class.getSimpleName();

    private static final String FILE_NAME = "test.plist";

    // 2011-11-12T03:14:00Z
    private static Calendar TEST_DATE = Calendar.getInstance();

    static {
        TEST_DATE.set(Calendar.YEAR, 2011);
        TEST_DATE.set(Calendar.MONTH, 11 - 1);
        TEST_DATE.set(Calendar.DAY_OF_MONTH, 12);
        TEST_DATE.set(Calendar.HOUR_OF_DAY, 3);
        TEST_DATE.set(Calendar.MINUTE, 14);
        TEST_DATE.set(Calendar.SECOND, 0);
    }

    private static final String[] NAME_ARRAY = new String[]{
            null, "test1", "test2"
    };

    private static final boolean[] EXIST_ARRAY = new boolean[]{
            false, true, false
    };

    public void testParsePLib() throws Exception {

        PListDict dict = PListParser.parse(
                getInstrumentation().getContext().getAssets().open(FILE_NAME));

        assertEquals(6, dict.size());

        String value1 = dict.getString("value1");
        assertEquals("Keiji_Ariyama", value1);

        String code = dict.getString("code");
        assertEquals("54235582305924389532", code);

        Double val = dict.getReal("score");
        assertEquals(43.42, val);

        Date date = dict.getDate("date");

        java.util.Date tmp = TEST_DATE.getTime();
        assertEquals(tmp.toString(), date.toString());

        PListDict others = dict.getDict("others");
        assertTrue(others.getBool("test"));

        PListArray array = dict.getArray("users");
        assertEquals(2, array.size());

        int len = array.size();
        for (int i = 0; i < len; i++) {
            PListDict user = array.getPListDict(i);
            assertUser(user);
        }
    }

    private void assertUser(PListDict user) {

        int id = user.getInt("id");

        try {
            user.getString("id");
            fail();
        } catch (PListException e) {
        }

        String name = user.getString("name");
        boolean exist = user.getBool("exist");

        Log.d(TAG, String.format("%d : %s", id, name));

        assertEquals(NAME_ARRAY[id], name);
        assertEquals(EXIST_ARRAY[id], exist);
    }
}
