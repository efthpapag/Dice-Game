package gr.aueb.representation;

import gr.aueb.domain.User;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "jakarta", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class UserMapper {

    public abstract UserRepresentation toRepresentation(User user);

    public abstract User toModel(UserRepresentation rep);

}
