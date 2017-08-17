package wayback;

import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.ArrayList;
import java.util.List;

public class MappingAnyJsonHttpMessageConverter extends MappingJackson2HttpMessageConverter {

    public MappingAnyJsonHttpMessageConverter() {
        List list = new ArrayList(1);
        list.add(MediaType.ALL);
        this.setSupportedMediaTypes(list);
    }
}
