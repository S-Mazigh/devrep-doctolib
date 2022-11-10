package devrep.projet.devmed.entities;

import java.util.stream.Stream;

import javax.persistence.AttributeConverter;

public class DomaineConverter implements AttributeConverter<Domaine,String>{

    @Override
    public String convertToDatabaseColumn(Domaine attribute) {
        if(attribute.equals(null))
            return null;
        return attribute.getCode();
    }

    @Override
    public Domaine convertToEntityAttribute(String dbData) {
        if(dbData.equals(null))
            return null;
        return Stream.of(Domaine.values())
            .filter(p -> p.code.equals(dbData))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }
}
