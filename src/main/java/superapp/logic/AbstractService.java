package superapp.logic;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public abstract  class AbstractService {
    protected String superappName;

    @Value("${spring.application.name}")
    public final void setSuperappName(String name) { this.superappName = name; }

    public final boolean isValidSuperapp(String superapp) { return this.superappName.equals(superapp); }
}
