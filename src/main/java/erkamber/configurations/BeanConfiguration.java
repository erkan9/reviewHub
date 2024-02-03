package erkamber.configurations;

import org.hibernate.collection.internal.PersistentBag;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class BeanConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Register a custom converter for handling PersistentBag to List conversion
        Converter<PersistentBag, List<?>> persistentBagToListConverter = context ->
                context.getSource() != null ? new ArrayList<>(context.getSource()) : null;

        modelMapper.addConverter(persistentBagToListConverter);

        return modelMapper;
    }
}
