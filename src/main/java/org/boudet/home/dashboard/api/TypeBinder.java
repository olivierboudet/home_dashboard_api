package org.boudet.home.dashboard.api;

import org.boudet.home.dashboard.api.enums.TypeEnum;
import org.springframework.util.StringUtils;

import java.beans.PropertyEditorSupport;

public class TypeBinder  extends PropertyEditorSupport {
    @Override
    public void setAsText(String s) throws IllegalArgumentException {

        if(StringUtils.hasText(s)){
            final TypeEnum type = TypeEnum.fromString(s);
            setValue(type);
        }
        else{
            setValue(s);
        }
    }
}
