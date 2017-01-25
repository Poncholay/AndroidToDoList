package com.poncholay.todolist.controller.task;

import com.poncholay.todolist.R;
import com.quemb.qmbform.annotation.FormValidator;
import com.quemb.qmbform.descriptor.RowDescriptor;
import com.quemb.qmbform.descriptor.RowValidationError;
import com.quemb.qmbform.descriptor.Value;

public class BlankValidator implements FormValidator {
	@Override
	@SuppressWarnings("unchecked")
	public RowValidationError validate(RowDescriptor descriptor) {
		Value value = descriptor.getValue();
		if (value.getValue() != null && value.getValue() instanceof String) {
			String val = (String) value.getValue();
			val = val.trim().replaceAll("\t", "").replaceAll(" +", " ");
			boolean ret = !val.isEmpty();
			if (ret) {
				value.setValue(val);
			}
			return ret ? null : new RowValidationError(descriptor, R.string.validation_invalid_title);
		}
		return new RowValidationError(descriptor, R.string.validation_invalid_title);
	}
}