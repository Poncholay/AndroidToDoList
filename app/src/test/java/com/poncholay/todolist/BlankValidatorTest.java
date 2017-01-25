package com.poncholay.todolist;

import android.util.Log;

import com.poncholay.todolist.controller.task.BlankValidator;
import com.quemb.qmbform.descriptor.RowDescriptor;
import com.quemb.qmbform.descriptor.Value;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class})
public class BlankValidatorTest {

	public class Tuple<X, Y> {
		public final X x;
		public final Y y;
		public Tuple(X x, Y y) {
			this.x = x;
			this.y = y;
		}
	}

	@Test
	@SuppressWarnings("unchecked")
	public void blankValidatorTest() throws Exception {
		RowDescriptor titleRow = RowDescriptor.newInstance("", RowDescriptor.FormRowDescriptorTypeText, "", null);
		titleRow.addValidator(new BlankValidator());

		Map<String, Tuple<Boolean, String> > values = new HashMap<>();
		int i = 0;

		values.put("", new Tuple<>(false, ""));
		values.put("]¶̡đ€ĸßðł", new Tuple<>(true, "]¶̡đ€ĸßðł"));
		values.put("Test", new Tuple<>(true, "Test"));
		values.put("           ", new Tuple<>(false, ""));
		values.put("      a       ", new Tuple<>(true, "a"));
		values.put("      a    title   ", new Tuple<>(true, "a title"));
		values.put("      \t   ", new Tuple<>(false, ""));
		values.put("\t  a  \t\t    b ", new Tuple<>(true, "a b"));
		values.put(null, new Tuple<>(false, ""));

		for (Map.Entry<String, Tuple<Boolean, String> > entry : values.entrySet()) {

			titleRow.setValue(new Value<>(entry.getKey()));

			if (BuildConfig.DEBUG) {
				System.out.println("Test n°" + ++i);
				System.out.println("Title : " + entry.getKey() + ", expected : " + entry.getValue().x + ", result : " + titleRow.isValid());
				if (entry.getValue().x) {
					System.out.println("Title : " + entry.getKey() + ", expected : " + entry.getValue().y + ", result : " + titleRow.getValue().getValue());
				}
			}

			if (titleRow.isValid() != entry.getValue().x) {
				throw new Exception("Incorrect validation");
			}
			if (entry.getValue().x) {
				if (!Objects.equals(titleRow.getValue().getValue(), entry.getValue().y)) {
					throw new Exception("String not corrected by validator");
				}
			}
		}
	}
}