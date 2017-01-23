package com.poncholay.todolist.controller.task;

import com.poncholay.todolist.R;
import com.quemb.qmbform.annotation.FormValidator;
import com.quemb.qmbform.descriptor.RowDescriptor;
import com.quemb.qmbform.descriptor.RowValidationError;
import com.quemb.qmbform.descriptor.Value;

/**
 * Created by wilmot_g on 16/01/17.
 */

public class BlankValidator implements FormValidator {
	@Override
	public RowValidationError validate(RowDescriptor descriptor) {
		Value value = descriptor.getValue();
		if (value.getValue() != null && value.getValue() instanceof String) {
			String val = (String) value.getValue();
			return !val.isEmpty() ? null : new RowValidationError(descriptor, R.string.validation_invalid_title);
		}
		return new RowValidationError(descriptor, R.string.validation_invalid_title);
	}
}